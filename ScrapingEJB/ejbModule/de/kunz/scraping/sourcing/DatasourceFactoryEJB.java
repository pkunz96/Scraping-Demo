package de.kunz.scraping.sourcing;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.*;

import javax.ejb.*;

import de.kunz.scraping.data.access.*;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;

@Stateless
@LocalBean
public class DatasourceFactoryEJB implements DatasourceFactoryEJBLocal {

	private static final Logger LOG = Logger.getLogger(DatasourceFactoryEJB.class.getName());
	
	@EJB
	private SalutationAccessEJBLocal salutationAccessEJB;
	
	@EJB
	private ConcessionAccessEJBLocal concessionsAccessEJB;
	
	@EJB
	private LegalStatusAccessEJBLocal legalStatusesAccessEJB;
	
	@EJB
	private FinancialProductProviderAccessEJBLocal financialProductProvierAccessEJB;
	
	@EJB
	private CountryAccessEJBLocal countryAccessEJB;
	
	@EJB
	private DatasourceAccessEJBLocal datasourceAccessEJB;
	
    public DatasourceFactoryEJB() {
    	super();
    }

	@Override
	public IDatasource<Broker> getDatasource(String datasourceName) {
		AbstractDatasource newInstance = null;
		try {
			if(datasourceName == null) {
				throw new NullPointerException();
			}
			else {
				final Datasource datasource = this.datasourceAccessEJB.getDatatource(datasourceName);
				System.out.println("ds: " +datasource); 
				if(datasource != null) {
					final String datasourceClassPath = datasource.getDatasourceClassPath();
					if(datasourceClassPath == null) {
						throw new IllegalArgumentException();
					}
					else {
						final Class<?> datasourceClass = Class.forName(datasourceClassPath);
						Constructor<?> constructor = datasourceClass.getConstructor(new Class[]{});
						newInstance = (AbstractDatasource)  constructor.newInstance();	
						initDatasource(newInstance, datasource);
					}
				}
			}
		}catch(IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e0) {
			LOG.warning(e0.getMessage());
			newInstance = null;
		}
		return newInstance;
	}
	
	private void initDatasource(AbstractDatasource newInstance, Datasource datasource) 
			throws IOException  {
		final List<Salutation> salutationsList = this.salutationAccessEJB.getSalutations();
		final List<Concession> concessionList = this.concessionsAccessEJB.getConcessions();
		final List<LegalStatus> legalStatusesList = this.legalStatusesAccessEJB.getLegalStatuses();
		final List<FinancialProductProvider> productProviderList = this.financialProductProvierAccessEJB.getFinancialProductProviders();
		final List<Country> countryList = this.countryAccessEJB.getCountries();
		newInstance.setSalutations(salutationsList);
		newInstance.setConcessions(concessionList);
		newInstance.setLegalStatuses(legalStatusesList);
		newInstance.setProductProviders(productProviderList);
		newInstance.setCountries(countryList);
		newInstance.setDatasource(datasource);
	}

}
 