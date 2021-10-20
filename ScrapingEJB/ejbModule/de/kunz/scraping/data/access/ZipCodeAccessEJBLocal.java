package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.access.CountryAccessEJBLocal.CountryIdentifier;
import de.kunz.scraping.data.entity.*;

@Local
public interface ZipCodeAccessEJBLocal {

	public List<ZipCode> getZipCodes() 
			throws IOException;
	
	public List<ZipCode> getZipCodes(CountryIdentifier countryID) 
		throws IOException;
	
	public double getDistance(String firstCountryCode, String firstZipCode, String secondCountryCode, String secondZipCode);	

	public List<ZipCode> getZipCodePatterns() 
		throws IOException;
	
	public List<ZipCode> getZipCodePatterns(CountryIdentifier countryID) 
			throws IOException;
} 