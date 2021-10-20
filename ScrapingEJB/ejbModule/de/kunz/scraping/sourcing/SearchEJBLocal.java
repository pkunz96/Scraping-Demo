package de.kunz.scraping.sourcing;

import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.*;
import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.util.concurrent.AsyncReport;

@Local
public interface SearchEJBLocal {
		
	Future<AsyncReport<IQuery<Broker>, List<Broker>>> find(IQuery<Broker> query, PropertyChangeListener changeListener, String operationName, long contextID);

	boolean supports(IQuery<Broker> query);
	
}
