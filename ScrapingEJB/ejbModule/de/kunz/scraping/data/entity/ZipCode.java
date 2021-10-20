package de.kunz.scraping.data.entity;

public class ZipCode {
	
	private Country country;
	
	private String zipCode;
	
	private String townName;
	 
	private double longitude;
	
	private double latitude;
	
	private boolean isPattern;
	
	public ZipCode() {
		super();
	} 
		
	public ZipCode(Country country, String zipCode, String townName, double longitude, double latitude, boolean isPattern) {
		super();
		this.country = country;
		this.zipCode = zipCode;
		this.townName = townName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.isPattern = isPattern;
	}

	public ZipCode(ZipCode zipCode) {
		super();
		this.country = zipCode.country;
		this.zipCode = zipCode.zipCode;
		this.townName = zipCode.townName;
		this.longitude = zipCode.longitude;
		this.latitude = zipCode.latitude;
		this.isPattern = zipCode.isPattern;
	}
	
	public Country getCountry() {
		return country;
	}

	public String getZipCode() {
		return zipCode;
	}
 
	public String getTownName() {
		return townName;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public boolean isPattern() {
		return isPattern;
	}

	public void setPattern(boolean isPattern) {
		this.isPattern = isPattern;
	}



	@Override  
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.country + ", " + this.zipCode + ", " + this.isPattern + "}";
	}
	
}
