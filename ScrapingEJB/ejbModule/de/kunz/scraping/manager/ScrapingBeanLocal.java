package de.kunz.scraping.manager;

import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface ScrapingBeanLocal {
		
	public IContext getRunningUpdate();
	
	public IContext startUpdate();
		
	public Set<IContext> getRunningQueries();
	
	public IContext executeQuery(List<ZipCode> zipCodePatterns, List<String> datasources);
} 
  