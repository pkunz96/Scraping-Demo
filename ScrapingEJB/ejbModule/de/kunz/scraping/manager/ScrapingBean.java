package de.kunz.scraping.manager;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.logging.*;

import javax.ejb.*;

import de.kunz.scraping.data.access.ZipCodeAccessEJBLocal;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.mapping.*;
import de.kunz.scraping.reduction.*;
import de.kunz.scraping.sourcing.*;
import de.kunz.scraping.synchronization.*;

@Singleton
public class ScrapingBean implements ScrapingBeanLocal {

	private static Logger LOG = Logger.getLogger(ScrapingBeanLocal.class.getName());
		
	private static final long DELAY = 300000;
	
	@EJB 
	private DatasourceFactoryEJBLocal datasourceFactory;
	
	@EJB
	private ZipCodeAccessEJBLocal zipCodeAccessEJB;
	
	@EJB 
	private SearchEJBLocal searchEJB;
	  
	@EJB
	private MappingEJBLocal mappingEJB;
	
	@EJB
	private ReducerEJBLocal reducerEJB;
	
	@EJB
	private SynchronizerEJBLocal snychronizerEJB;
	
	private Context runningUpdateContext;
	
	private final Map<Long, Context> runningQueriesContextMap;
	
	public ScrapingBean() {
		this.runningUpdateContext = null;
		this.runningQueriesContextMap = new ConcurrentHashMap<>();
	}
	
	@Override
	public synchronized IContext getRunningUpdate() {
		if(isUpdateRunning()) {
			return this.runningUpdateContext;
		}
		else {
			IContext context = this.runningUpdateContext;
			this.runningUpdateContext = null;
			return context;
		}
	}
	
	@Override
	public synchronized IContext startUpdate() {
		if(!isUpdateRunning()) {
			this.runningUpdateContext = new Context(this.searchEJB, this.reducerEJB, this.mappingEJB, this.snychronizerEJB);
			
			return runningUpdateContext;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public Set<IContext> getRunningQueries() {
		return new HashSet<>(this.runningQueriesContextMap.values());
	}

	@Override
	public IContext executeQuery(List<de.kunz.scraping.data.entity.ZipCode> patterns, List<String> datasources) {
		final Context context = new Context(this.searchEJB, this.reducerEJB, this.mappingEJB, this.snychronizerEJB);
		try {
			this.runningQueriesContextMap.put(context.getContextId(), context);
			final List<ZipCode> zipCodeList = filterZipCodesByDistance(getMatchtingZipCodes(patterns), 30);			
			final List<IQuery<Broker>> queryList = new LinkedList<>();
			for(String datasourceName: datasources) {
				final IDatasource<Broker> datasource = this.datasourceFactory.getDatasource(datasourceName);				
				final IQuery<Broker> query = buildQuery(datasource, zipCodeList);
				queryList.add(query);
			}	 
			context.find(queryList);
		} 
		catch(IOException e0) {
			LOG.warning(e0.getMessage());  
		}
		return context;
	}  
	
    @Schedule(minute="0")
	public void cleanup() {
		final long currentTime = System.currentTimeMillis();
		for(Entry<Long, Context> entry : this.runningQueriesContextMap.entrySet()) {
			final IContext context = entry.getValue();
			final long completionTime = context.getCompletionTime();
			if(completionTime != -1 && (currentTime - completionTime) >= DELAY) {
				this.runningQueriesContextMap.remove(entry.getKey());
			}
		}
	} 
	
	private boolean isUpdateRunning() {
		if(this.runningUpdateContext == null) {
			return false;
		}
		else {
			return IContext.UPDATE_RUNNING.equals(this.runningUpdateContext.getState());
		}
	}
	
	private List<ZipCode> getMatchtingZipCodes(List<ZipCode> zipCodePatterns)
			throws IOException { 
		final List<ZipCode> zipCodeList = this.zipCodeAccessEJB.getZipCodes();	
		final List<ZipCode> matchingZipCodes = new LinkedList<>();
		for(ZipCode zipCode : zipCodeList) {
			for(ZipCode pattern : zipCodePatterns) {
				if(compareCountries(zipCode.getCountry(), pattern.getCountry())) {
					final String zipCodeStr = zipCode.getZipCode();
					final String zipCodePatternStr = pattern.getZipCode();
					if(zipCodePatternStr.length() == zipCodeStr.length()) {
						boolean matches = true;
						for(int charIndex = 0; charIndex < zipCodePatternStr.length(); charIndex++) {
							if(zipCodeStr.charAt(charIndex) != zipCodePatternStr.charAt(charIndex) && zipCodePatternStr.charAt(charIndex) != 'x') {
								matches = false;
								break;
							}
						}
						if(matches) {
							matchingZipCodes.add(zipCode);
							break;
						}
					}
				}
			}
		} 
		return matchingZipCodes;
	}
	
	private List<ZipCode> filterZipCodesByDistance(List<ZipCode> zipCodeList, int minDistance) {
		final List<ZipCode> filteredZipCodeList = new LinkedList<>();
		final int MIN_DISTANCE = 30;
		for(ZipCode zipCode : zipCodeList) {
			boolean isRedundant = false;
			for(ZipCode filteredZipCode : filteredZipCodeList) {
				if(this.zipCodeAccessEJB.getDistance(filteredZipCode.getCountry().getCountryCode(), filteredZipCode.getZipCode(), zipCode.getCountry().getCountryCode(), zipCode.getZipCode()) < MIN_DISTANCE) {
					isRedundant = true;
				}
			}
			if(!isRedundant) {
				filteredZipCodeList.add(zipCode);
			}
		}
		return filteredZipCodeList;
	}
		
	private boolean compareCountries(Country firstCountry, Country secondCountry) {
		if(firstCountry == secondCountry) {
			return true;
		}
		else if(firstCountry != null) {
			return firstCountry.equals(secondCountry);
		}
		else {
			return false;
		}
	}
	
	private IQuery<Broker> buildQuery(IDatasource<Broker> datasource, List<ZipCode> zipCodeList) {
    	IQueryBuilder<Broker> queryBuilder = IQueryBuilder.getInstance(Broker.class);
    	queryBuilder.addDatasource(datasource).startPredicate(LogicalConnective.OR);
    	for(de.kunz.scraping.data.entity.ZipCode zipCode: zipCodeList) {
    		final String zipCodeStr = zipCode.getZipCode();
    		final String zipCodeCountryCodeStr = zipCode.getCountry() != null ? zipCode.getCountry().getCountryCode() : null;
    		if(zipCodeStr != null && (zipCodeCountryCodeStr != null)) {
    			final String constraintStr = zipCodeStr + "@" + zipCodeCountryCodeStr;
    			queryBuilder.addConstraint(new de.kunz.scraping.sourcing.querying.ZipCode(), constraintStr, Relation.EUQAL);
    		}    		
    	}
    	return queryBuilder.closePredicate().getQuery();  
	} 
} 
