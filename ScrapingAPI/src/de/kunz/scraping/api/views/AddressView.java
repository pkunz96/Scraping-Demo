package de.kunz.scraping.api.views;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class AddressView {
	
	private Long addressID = null;
	
	private String street = null;
	
	private String houseNo = null;
	
	private String zipCode = null;
	
	private String town = null;
	
	private Long countryID = null;
	
	private Long datasourceID = null;
	
	public AddressView(Address address, IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				this.addressID = address.getAddressId();
			}
			else {
				this.addressID = idGenerator.next();
			}
		}
		final Country country = address.getCountry();
		final Datasource datasource = address.getDatasource();
		{
			if(country != null) {
				this.countryID = country.getCountryId();
			}
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
		}
		{
			this.street = address.getStreet();
			this.houseNo = address.getHouseNo();
			this.zipCode = address.getZipCode();
			this.town = address.getTown();
		}
	}

	public Long getAddressID() {
		return addressID;
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

	public Long getCountryID() {
		return countryID;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}
}
