package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface CountryAccessEJBLocal {

	public enum CountryIdentifier {
		
		GERMANY(0, "Deutschland", "DE"),
		
		AUSTRIA(1, "Österreich", "AT"),
		
		SWITZERLAND(2, "Schweiz", "CH");
		
		public static List<CountryIdentifier> getCountryIdentifiers() {
			final List<CountryIdentifier> countryIdenfierList = new LinkedList<>();
			{
				countryIdenfierList.add(GERMANY);
				countryIdenfierList.add(AUSTRIA);
				countryIdenfierList.add(SWITZERLAND);
			}
			return countryIdenfierList;
		}
		
		private final long countryId;
		 
		private final String countryName;
		
		private final String countryCode;
		
		private CountryIdentifier(long countryId, String countryName, String countryCode) {
			this.countryId = countryId;
			this.countryName = countryName;
			this.countryCode = countryCode;
		}

		public long getCountryId() {
			return countryId;
		}

		public String getCountryName() {
			return countryName;
		}

		public String getCountryCode() {
			return countryCode;
		}
	}
	
	public List<Country> getCountries() 
			throws IOException; 
	
	/**
	 * Returns the managed instance of {@link Country} identified by the passed instance of {@link CountryIdentifier}.
	 * 
	 * @param countryIdentified identifies an instance of {Country}.
	 * @return an instance of {@link Country}.
	 * @throws NullPointerException if countryIdentified is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public Country getCountry(CountryIdentifier countryIdentified)
			throws IOException;

}
