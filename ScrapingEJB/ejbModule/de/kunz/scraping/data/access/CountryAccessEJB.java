package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * Session Bean implementation class CountryAccessEJB
 */
@Stateless
@LocalBean
public class CountryAccessEJB implements CountryAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityManager;
	
    /**
     * Constructs an instance of CountryAccessEJB.
     */
    public CountryAccessEJB() {
    	super();
    }

	@Override
	public List<Country> getCountries()
			throws IOException {
		final List<Country> countryList = new LinkedList<>();
		final List<CountryIdentifier> countryIdentifierList = CountryIdentifier.getCountryIdentifiers();
		for(CountryIdentifier countryIdentifier : countryIdentifierList) {
			final Country country = getCountry(countryIdentifier);
			countryList.add(country);
		}
		return countryList;
	}
    
	@Override
	public Country getCountry(CountryIdentifier countryIdentified) 
			throws IOException {
		Country country;
		if(countryIdentified == null) {
			throw new NullPointerException();
		}
		else {
			try {
				final long countryId  = countryIdentified.getCountryId();
				country = entityManager.find(Country.class, countryId);
				if(country == null) {
					final String countryName = countryIdentified.getCountryName();
					final String countryCode = countryIdentified.getCountryCode();
					country = new Country();
					country.setCountryId(countryId);
					country.setCountryName(countryName);
					country.setCountryCode(countryCode);
					entityManager.persist(country);
				}
			}catch(PersistenceException e0) {
				throw new IOException(e0);
			}
		} 
		return country;
	}


}
