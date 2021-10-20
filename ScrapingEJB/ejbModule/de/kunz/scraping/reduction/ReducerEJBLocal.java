package de.kunz.scraping.reduction;

import java.beans.*;
import java.util.*;
import java.util.concurrent.*;
import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.concurrent.AsyncReport;

@Local
public interface ReducerEJBLocal {
	
	boolean add(Broker broker, long contextID);
	
	Future<AsyncReport<Void, Iterator<Broker>>> reduce(PropertyChangeListener listener, String operationName, long contextID);
}
