package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IPersonProvider {
	
	String getSalutation();
	
	String getPrename();
	
	String getLastname();
	
	Date getBirthDate();
	
	String getPortraitUrl();
	
	String getStreet();
	
	String getHouseNo();
	
	String getZipCode();
	
	String getTown();
	
	String getCountryCode();	
}
