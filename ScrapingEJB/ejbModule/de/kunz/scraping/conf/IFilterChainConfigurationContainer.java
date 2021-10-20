package de.kunz.scraping.conf;

import java.util.*;


interface IFilterChainConfigurationContainer {
	
	IFilterChainConfiguration getEnabledFilterChainConfiguration();
	
	List<IFilterChainConfiguration> getFilterChainConfigurationsList();
	
	IFilterChainConfiguration createFilterChain(String filterChainName);
	
	void removeFilterChain(String filterChainName);
	
	boolean isFilterChainNameAvailable(String filterChainName);	 
}
