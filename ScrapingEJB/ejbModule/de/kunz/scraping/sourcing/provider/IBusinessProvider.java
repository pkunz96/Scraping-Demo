package de.kunz.scraping.sourcing.provider;

public interface IBusinessProvider {
	
	String getFirm();
	
	Boolean isSmallBusiness();
	
	String getCompanyRegistrationNo();
	
	String getStreet();
	
	String getHouseNo();
	
	String getZipCode();
	
	String getTown();
	
	String getCountryCode();
}
