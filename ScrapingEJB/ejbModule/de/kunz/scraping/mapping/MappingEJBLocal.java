package de.kunz.scraping.mapping;

import java.beans.*;
import java.util.concurrent.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.concurrent.AsyncReport;


@Local
public interface MappingEJBLocal {

	Future<AsyncReport<Broker, Broker>> map(Broker broker, PropertyChangeListener changeListener, String operationName, long contextID);
	
}
