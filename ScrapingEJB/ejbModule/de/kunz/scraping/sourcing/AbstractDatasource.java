package de.kunz.scraping.sourcing;

import java.io.*;
import java.util.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.sourcing.provider.*;
import de.kunz.scraping.util.net.*;

abstract class AbstractDatasource implements IDatasource<Broker> {

	private static class IConstraintIterator implements Iterator<IConstraint<Broker>> {

		private static void extractConstraints(IPredicate<Broker> predicate, Queue<IConstraint<Broker>> constraintQueue) {
			if((predicate == null) || (constraintQueue == null)) {
				throw new NullPointerException();
			}
			else {
				constraintQueue.addAll(predicate.getConstraints());
				for(IPredicate<Broker> subPredicate : predicate.getSubPredicates()) {
					extractConstraints(subPredicate, constraintQueue);
				}
			}
		}
		
		private final Queue<IConstraint<Broker>> constraintQueue = new LinkedList<>();
		
		public IConstraintIterator(IQuery<Broker> query) {
			if(query == null) {
				throw new NullPointerException();
			}
			else {
				final IPredicate<Broker> predicate = query.getPredicate();
				if(predicate != null) {
					extractConstraints(predicate, constraintQueue);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return !this.constraintQueue.isEmpty();
		}

		@Override
		public IConstraint<Broker> next() {
			if(this.constraintQueue.isEmpty()) {
				throw new NoSuchElementException();
			}
			else {
				return this.constraintQueue.poll();
			}
		}
		
	}
			
	public static String read(URLConnection connection)
			throws IOException {
		StringBuilder response = new StringBuilder("");
		if(connection == null) {
			throw new NullPointerException();
		}
		else {
			final InputStream connectionInputStream = connection.getInputStream();
			try(connection; connectionInputStream) {
				final BufferedReader bufReader = new BufferedReader(new InputStreamReader(connectionInputStream));
				String tmp;
	        	while((tmp = bufReader.readLine()) != null) {
	        		response.append(tmp);
	        	} 
			} catch(Exception e0) { 
				e0.printStackTrace();  
				throw new IOException(e0); 
			}	
		}
        return response.toString();
	} 
	
	private List<Salutation> salutations;
	 
	private List<Country> countries;
	
	private List<Concession> concessions;
	
	private List<FinancialProductProvider> productProviders;
	
	private List<LegalStatus> legalStatuses;
	
	private Datasource datasource;
	
	private final IURLConnectionFactory connectionFactory;
	
	public AbstractDatasource() {
		this.connectionFactory = IURLConnectionFactory.getInstance();
	}
	
	@Override
	public List<Broker> executeQuery(IQuery<Broker> query) 
			throws QueryingException {
		final List<Broker> resultList = new LinkedList<>();
		if(query == null) {
			throw new NullPointerException();
		}
		else if(!supports(query)) {
			throw new IllegalArgumentException();
		}
		else {
			try {
				final String providerProfileName = getProviderProfileName();
				final IProviderConfiguration providerConf =  IScrapingConfiguration.getInstance().getSourcingConfiguration().getProviderConfiguration(providerProfileName);
				final IMasterDataProviderFactory masterDataProviderFactory = IMasterDataProviderFactory.getInstance();
				final IBrokerBuilder brokerBuilder = new BrokerBuilderImpl();
				{
					brokerBuilder.setSaluations(salutations);
					brokerBuilder.setCountries(countries);
					brokerBuilder.setConcessions(concessions);
					brokerBuilder.setFinancialProductProviders(productProviders);
					brokerBuilder.setLegalStatuses(legalStatuses);
					brokerBuilder.setDatasource(datasource);
				}
				final IConstraintIterator constraintIterator = new IConstraintIterator(query);
				while(constraintIterator.hasNext()) {
					final IConstraint<Broker> constraint = constraintIterator.next();
					final String queryUrl = getQueryUrl(constraint);
					if(queryUrl != null) {
						final URLConnection connection = connectionFactory.getURLConnection(queryUrl, providerProfileName);
						initConnection(constraint, connection);
						final String queryResult = read(connection);			
						IMasterDataProvider masterDataProvider = masterDataProviderFactory.createMasterDataProvider(providerConf, queryResult);
						brokerBuilder.setMasterDataProvider(masterDataProvider);
					} 
					resultList.addAll(brokerBuilder.build());	
				}					
				query.getFilter().filter(resultList); 
			} catch(IOException | InterruptedException e0) {
				e0.printStackTrace();
				throw new QueryingException(e0); 
			}
		} 
		return resultList; 
	}
	
	@Override
	public boolean supports(IQuery<Broker> query) {
		if(query == null) {
			throw new NullPointerException();
		}
		else {
			final IConstraintIterator constraintIterator = new IConstraintIterator(query);
			while(constraintIterator.hasNext()) {
				final IConstraint<Broker> constraint = constraintIterator.next();
				if(!supports(constraint)) {
					return false;
				}
			}
		}
		return true;
	}

	final void setSalutations(List<Salutation> salutations) {
		this.salutations = salutations;
	}
	
	final void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	final void setConcessions(List<Concession> concessions) {
		this.concessions = concessions;
	}

	final void setProductProviders(List<FinancialProductProvider> productProviders) {
		this.productProviders = productProviders;
	}

	final void setLegalStatuses(List<LegalStatus> legalStatuses) {
		this.legalStatuses = legalStatuses;
	}	
	
	final void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	abstract String getQueryUrl(IConstraint<Broker> constraint);	
	
	abstract void initConnection(IConstraint<Broker> constraint, URLConnection connection);
	
	abstract boolean supports(IConstraint<Broker> constraint);
	
	abstract String getProviderProfileName(); 
}  
