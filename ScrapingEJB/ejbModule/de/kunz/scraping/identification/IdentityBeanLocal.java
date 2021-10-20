package de.kunz.scraping.identification;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface IdentityBeanLocal {
	boolean isIdentic(Broker firstBroker, Broker secondBroker);
}
