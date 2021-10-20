package de.kunz.scraping.identification;

import javax.ejb.*;

import de.kunz.scraping.conf.IFilterChainConfiguration;
import de.kunz.scraping.conf.IScrapingConfiguration;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.filtering.*;

/**
 * Session Bean implementation class IdentityBean
 */
@Stateless
public class IdentityBean implements IdentityBeanLocal {

	private static IFilterChain IDENTITY_FITLER_CHAIN = null;
	static {
		IFilterChainConfiguration filterChainConf = IScrapingConfiguration.getInstance().getIdentificationConfiguration().getEnabledFilterChainConfiguration();
		if(filterChainConf != null) {
			IDENTITY_FITLER_CHAIN = IFilterChain.getInstance(filterChainConf);
		}
	}
	
    /** 
     * Default constructor. 
     */
    public IdentityBean() {
    	super();
    }

	@Override  
	public boolean isIdentic(Broker firstBroker, Broker secondBroker) {
		final boolean isIdentic;
		try {
			final IdentificationTask identityRequest = new IdentificationTask(firstBroker, secondBroker);			
			IDENTITY_FITLER_CHAIN.filter(identityRequest);
			isIdentic = identityRequest.isIdentical();	
		}catch(FilterException e0) {
			throw new IllegalStateException(e0);
		}
		return isIdentic;
	}
 
}
 