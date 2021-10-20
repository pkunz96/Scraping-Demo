package de.kunz.scraping.sourcing;

import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.util.concurrent.*;
import de.kunz.scraping.util.concurrent.AsyncReport.AsyncJobState;


/**
 * Session Bean implementation class SearchEJB
 */
@Stateless
@LocalBean
public class SearchEJB implements SearchEJBLocal {

	@Override
	@Asynchronous
	@Lock(LockType.READ)
	public Future<AsyncReport<IQuery<Broker>, List<Broker>>> find(IQuery<Broker> query, PropertyChangeListener changeListener, String operationName, long contextID) {
		AsyncReport<IQuery<Broker>, List<Broker>> asyncReport = new DefaultAsyncReport<>();
		asyncReport.setOperationName(operationName);
		asyncReport.addPropertyChangeListener(changeListener);
		asyncReport.setContextId(contextID);
		if(query == null) {
			throw new NullPointerException();
		} 
		else if(!supports(query)) {
			throw new IllegalArgumentException();
		}
		else {
			try {
				final List<Broker> resultList = query.execute();
				asyncReport.setResult(resultList);
				asyncReport.setState(AsyncJobState.DONE);
			} catch(Exception e0) {
				e0.printStackTrace();
				asyncReport.setState(AsyncJobState.IS_FAILED);
				asyncReport.setChause(e0);
			}
			
		}
		return new AsyncResult<>(asyncReport); 
	}

	@Override
	public boolean supports(IQuery<Broker> query) {
		if(query == null) {
			return false;
		}
		else {
			final int ONE_ELEMENT = 1;
			final int FIRST_ELEMENT = 0;
			final List<IDatasource<Broker>> datasourcesList = query.getDatasources();
			return (datasourcesList.size() == ONE_ELEMENT)
					&& ((datasourcesList.get(FIRST_ELEMENT) instanceof DatasourceErgo) || (datasourcesList.get(FIRST_ELEMENT) instanceof DatasourceDEVK)) ; 
		}
	}

	
}
 