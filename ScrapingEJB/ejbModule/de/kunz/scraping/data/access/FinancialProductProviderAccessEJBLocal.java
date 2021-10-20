package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.access.CountryAccessEJBLocal.CountryIdentifier;
import de.kunz.scraping.data.entity.FinancialProductProvider;

@Local
public interface FinancialProductProviderAccessEJBLocal {

	public enum FinancialProductProviderIdentifier {
		
		ERGO_GROUP(1, "ERGO Beratung und Vertrieb AG", "ERGO-Platz", "1", "40198", "Düsseldorf", CountryIdentifier.GERMANY),
		BARMENIA(2, "Barmenia Versicherungen", "Barmenia-Allee", "1", "42119", "Wuppertal", CountryIdentifier.GERMANY),
		DEVK(3, "DEVK Deutsche Eisenbahn Versicherung Sach- und HUK-Versicherungsverein a.G.", "Riehler Straße", "190", "50735", "Köln", CountryIdentifier.GERMANY),
		JANITOS(4, "Janitos Versicherung AG", "Im Breitspiel", "2-4", "69126", "Heidelberg", CountryIdentifier.GERMANY);

		public static List<FinancialProductProviderIdentifier> getFinancialProductProviderIdentifier() {
			final List<FinancialProductProviderIdentifier> financialProductProviderIdentifiersList = new  LinkedList<>(); {
				financialProductProviderIdentifiersList.add(ERGO_GROUP);
				financialProductProviderIdentifiersList.add(BARMENIA);
				financialProductProviderIdentifiersList.add(DEVK);
				financialProductProviderIdentifiersList.add(JANITOS);
			}
			return financialProductProviderIdentifiersList;
		}
		
		private final long financialServiceProviderId;
		
		private final String financialServiceProviderName;
		
		private final String street;
		 
		private final String houseNo;
		
		private final String zipCode;
		
		private final String town;
		
		private final CountryIdentifier countryIdentifier;
		
		private FinancialProductProviderIdentifier(long financialServiceProviderId, String financialServiceProviderName, 
				String street, String houseNo, String zipCode, String town, CountryIdentifier countryIdentifier) {
			this.financialServiceProviderId = financialServiceProviderId;
			this.financialServiceProviderName = financialServiceProviderName;
			this.street = street;
			this.houseNo = houseNo;
			this.zipCode = zipCode;
			this.town = town;
			this.countryIdentifier = countryIdentifier;
		}

		public long getFinancialServiceProviderId() {
			return financialServiceProviderId;
		}

		public String getFinancialServiceProviderName() {
			return financialServiceProviderName;
		}

		public String getStreet() {
			return street;
		}

		public String getHouseNo() {
			return houseNo;
		}

		public String getZipCode() {
			return zipCode;
		}

		public String getTown() {
			return town;
		}

		public CountryIdentifier getCountryIdentifier() {
			return countryIdentifier;
		}
	}
	
	public List<FinancialProductProvider> getFinancialProductProviders() 
			throws IOException;
	
	/**
	 * Returns the managed instance of {@link FinancialProductProvider} identified by the passed instance of {@link FinancialProductProviderIdentifier}.
	 * 
	 * @param financialProductProviderIdentifier identifies an instance of {FinancialProductProvider}.
	 * @return an instance of {@link FinancialProductProvider}.
	 * @throws NullPointerException if financialProductProviderIdentifier is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public FinancialProductProvider getFinancialProductProvider(FinancialProductProviderIdentifier financialProductProviderIdentifier)
			throws IOException;
}
