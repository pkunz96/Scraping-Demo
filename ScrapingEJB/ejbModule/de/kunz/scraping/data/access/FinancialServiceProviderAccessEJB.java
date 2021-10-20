package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.access.CountryAccessEJBLocal.CountryIdentifier;
import de.kunz.scraping.data.entity.*;

/**
 * Session Bean implementation class FinancialServiceProviderAccessEJB
 */
@Stateless
@LocalBean
public class FinancialServiceProviderAccessEJB implements FinancialProductProviderAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityManager;
	
	@EJB
	private DatasourceAccessEJBLocal datasourceAccess;
	
    /**
     * Default constructor. 
     */
    public FinancialServiceProviderAccessEJB() {
    	super();
    }
    
	@Override
	public List<FinancialProductProvider> getFinancialProductProviders()
			throws IOException {
		final List<FinancialProductProvider> financialProductProvidersList = new LinkedList<>();
		final List<FinancialProductProviderIdentifier> financialProductProviderIdentifiersList = FinancialProductProviderIdentifier.getFinancialProductProviderIdentifier();
		for(FinancialProductProviderIdentifier financialProductProviderIdentifier : financialProductProviderIdentifiersList) {
			final FinancialProductProvider financialProductProvider = getFinancialProductProvider(financialProductProviderIdentifier);
			financialProductProvidersList.add(financialProductProvider);
		}
		return financialProductProvidersList;
	}

	@Override
	public FinancialProductProvider getFinancialProductProvider(FinancialProductProviderIdentifier financialProductProviderIdentifier)
			throws IOException {
		FinancialProductProvider productProvider;
		if(financialProductProviderIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			final long financialProductProviderId = financialProductProviderIdentifier.getFinancialServiceProviderId();
			productProvider = entityManager.find(FinancialProductProvider.class, financialProductProviderId);
			if(productProvider == null) {
				final String productProviderName = financialProductProviderIdentifier.getFinancialServiceProviderName();
				final String street = financialProductProviderIdentifier.getStreet();
				final String houseNo = financialProductProviderIdentifier.getHouseNo();
				final String zipCode = financialProductProviderIdentifier.getZipCode();
				final String town = financialProductProviderIdentifier.getTown();
				final Country country = getCountry(financialProductProviderIdentifier.getCountryIdentifier());
				final Address address = createAddress(street, houseNo, zipCode, town, country);
				productProvider = new FinancialProductProvider();
				productProvider.setFinancialProductProviderId(financialProductProviderId);
				productProvider.setFinancialProductName(productProviderName);
				productProvider.setAddress(address);
				entityManager.persist(productProvider);
			}
			else {
				updateFinancialProductProvider(productProvider, financialProductProviderIdentifier);
			}
		}
		return productProvider;
	}
	
	private Country getCountry(CountryIdentifier countryIdentifier) {
		final Country country;
		final long countryId = countryIdentifier.getCountryId();
		country = entityManager.find(Country.class, countryId);
		return country;
	}
	
	private Address createAddress(String street, String houseNo, String zipCode, String town, Country country) 
			throws IOException {
		final Address address = new Address();
		address.setStreet(street);
		address.setHouseNo(houseNo);
		address.setZipCode(zipCode);
		address.setTown(town);
		address.setCountry(country);
		entityManager.persist(address);
		return address;
	}
	
	private void updateFinancialProductProvider(FinancialProductProvider productProvider,
			FinancialProductProviderIdentifier financialProductProviderIdentifier) {
		productProvider.setFinancialProductName(financialProductProviderIdentifier.getFinancialServiceProviderName());
		final Address address = productProvider.getAddress();
		if(address != null) {
			address.setStreet(financialProductProviderIdentifier.getStreet());
			address.setHouseNo(financialProductProviderIdentifier.getHouseNo());
			address.setZipCode(financialProductProviderIdentifier.getZipCode());
			address.setTown(financialProductProviderIdentifier.getTown());
			final Country country = getCountry(financialProductProviderIdentifier.getCountryIdentifier());
			address.setCountry(country);
		}
	}

}
