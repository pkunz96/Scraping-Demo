package de.kunz.scraping.reduction;


import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface MergingEJBLocal {

	public Broker merge(Broker firstBroker, Broker secondBroker);
	
}
   