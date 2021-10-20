package de.kunz.scraping.synchronization;

import java.beans.*;
import java.util.concurrent.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.concurrent.AsyncReport;

@Stateless
public class SynchronizerEJB implements SynchronizerEJBLocal {

    /**
     * Constructs an instance of SynchronizerEJB.
     */
    public SynchronizerEJB() {
    	super();
    }

	@Override
	@Asynchronous
	public Future<AsyncReport<Broker, Broker>> sync(Broker broker, PropertyChangeListener listener, String operationName, long contextID) {
		return null;
	}

}
