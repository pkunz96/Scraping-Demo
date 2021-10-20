package de.kunz.scraping.synchronization;

import java.beans.*;
import java.util.concurrent.*;
import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.concurrent.AsyncReport;


@Local
public interface SynchronizerEJBLocal {
	
	Future<AsyncReport<Broker, Broker>> sync(Broker broker, PropertyChangeListener listener, String operationName, long contextId);
}
