package de.kunz.scraping.mapping;

import java.beans.PropertyChangeListener;
import java.util.concurrent.*;

import javax.ejb.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.concurrent.AsyncReport;
import de.kunz.scraping.util.concurrent.DefaultAsyncReport;
import de.kunz.scraping.util.concurrent.AsyncReport.AsyncJobState;
import de.kunz.scraping.util.filtering.*;


/**
 * An implementation of MappingEJBLocal.
 */
@Stateless
@LocalBean
public class MappingEJB implements MappingEJBLocal {

	private static IFilterChain MAPPING_FILTER_CHAIN = null;
	static {
		IFilterChainConfiguration filterChainConf = IScrapingConfiguration.getInstance().getMappingConfiguration().getEnabledFilterChainConfiguration();
		if(filterChainConf != null) {
	 		MAPPING_FILTER_CHAIN = IFilterChain.getInstance(filterChainConf);
		}
	}
	
    /**
     * Constructs an instance of MappingEJB. 
     */
    public MappingEJB() {
    	super();
    }

	@Override
	@Asynchronous
	public Future<AsyncReport<Broker, Broker>> map(Broker broker, PropertyChangeListener changeListener, String operationName, long contextID) {
		final AsyncReport<Broker, Broker> asyncReport = new DefaultAsyncReport<>();
		asyncReport.setOperationName(operationName);
		asyncReport.addPropertyChangeListener(changeListener);
		asyncReport.setContextId(contextID);
		if(broker == null) {
			throw new NullPointerException(); 
		}
		else if(MAPPING_FILTER_CHAIN != null){
			asyncReport.setArgument(broker);
			try {
				if(MAPPING_FILTER_CHAIN.isSupported(broker)) {
					MAPPING_FILTER_CHAIN.filter(broker);
					asyncReport.setResult(broker);
					asyncReport.setState(AsyncJobState.DONE);
				}
				else {
					asyncReport.setState(AsyncJobState.INGNORED);
				}
			} catch(Exception e0) {
				e0.printStackTrace();
				asyncReport.setState(AsyncJobState.IS_FAILED);
				asyncReport.setChause(e0);
			}
		}
		else {
			asyncReport.setState(AsyncJobState.INGNORED);
		}
		return new AsyncResult<>(asyncReport);
	}
}


 