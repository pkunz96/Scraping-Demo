package de.kunz.scraping.api.views;

import de.exp.ai.scraping.data.entity.*;

public class ZipCodeView {
	
	private String countryCode;
	
	private String zipCodeStr;
	
	private boolean isPattern;
	
	public ZipCodeView(ZipCode zipCode) {
		final Country country;
		if(zipCode == null) {
			throw new NullPointerException();
		}
		else if((country = zipCode.getCountry()) == null){
			throw new IllegalArgumentException();
		}
		else {
			this.countryCode = country.getCountryCode();
			this.zipCodeStr = zipCode.getZipCode();
			this.isPattern = zipCode.isPattern();
		}
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getZipCodeStr() {
		return zipCodeStr;
	}

	public boolean isPattern() {
		return isPattern;
	}	
}
