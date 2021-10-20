package de.kunz.scraping.sourcing;

import javax.ejb.Local;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;

@Local
public interface DatasourceFactoryEJBLocal {
	
	IDatasource<Broker> getDatasource(String datasourceName);
}
