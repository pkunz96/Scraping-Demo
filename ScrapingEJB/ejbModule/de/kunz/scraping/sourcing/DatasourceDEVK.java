package de.kunz.scraping.sourcing;

import java.net.*;
import java.nio.charset.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.util.net.URLConnection;


final class DatasourceDEVK extends AbstractDatasource {

	private static final String PROVIDER_CONFIGURATION_PROFILE_NAME = "devk";
	
    private final static String DEVK_SALES_PARTNER_SEARCH_URL = "https://beratersuche.devk.de/beratersuche.html?q=";
	
    public DatasourceDEVK() {
    	super();
    }
    
	@Override
	String getQueryUrl(IConstraint<Broker> constraint) {
		String queryUrl;
		if(constraint == null) {
			throw new NullPointerException(); 
		}
		else if(!supports(constraint)){
			throw new IllegalArgumentException();
		}
		else {
			final String zipCode = constraint.getValue();
			if(zipCode.endsWith("@DE")) {
				queryUrl = URLEncoder.encode(DEVK_SALES_PARTNER_SEARCH_URL + zipCode, StandardCharsets.UTF_8);
			}
			else {
				queryUrl = null;
			}
		}
		return queryUrl;
	} 
 
	@Override
	void initConnection(IConstraint<Broker> constraint, URLConnection connection) {
		if((constraint == null) || (connection == null)) {
			throw new NullPointerException();
		}
		connection.setRequestProperty("Accept", "application/json"); 
	}

	@Override
	boolean supports(IConstraint<Broker> constraint) {
		if(constraint == null) {
			throw new NullPointerException();
		}
		return(constraint.getId() instanceof ZipCode) && constraint.getValue() != null;
	}  
 
	@Override
	String getProviderProfileName() {
		return PROVIDER_CONFIGURATION_PROFILE_NAME; 
	}
}
  