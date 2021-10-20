package de.kunz.scraping.sourcing;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.sourcing.querying.ZipCode;
import de.kunz.scraping.util.net.URLConnection;

final class DatasourceErgo extends AbstractDatasource {

	private static final String PROVIDER_CONFIGURATION_PROFILE_NAME = "ergo";
	
    private final static String ERGO_SALES_PARTNER_SEARCH_URL = "https://www.ergo.de/ergode/handlers/agentsearchhandler.ashx?page=1&pageSize=30&functions=AO1AGT;AO1WAGT;AO1AD;AO155P;AO2AGT;AO2WAGT;AO2AD;AO1DKVPLUS;AO1WDKV;ERGOPROAGT;ERGOPROSTR&input=";

	public DatasourceErgo() {
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
				queryUrl = ERGO_SALES_PARTNER_SEARCH_URL + zipCode;
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
