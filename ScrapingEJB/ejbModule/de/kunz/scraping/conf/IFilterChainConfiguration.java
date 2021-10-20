package de.kunz.scraping.conf;

import java.util.*;

public interface IFilterChainConfiguration {
	
	public String getFilterChainName();
	
	public void setFilterChainName(String name);
	
	List<IFilterConfiguration> getFilterConfigurationsList();
	
	IFilterConfiguration addFilterConfiguration(String filterName);
	
	void removeFilter(String filterName);
	
	boolean isFilterNameAvailable(String filterName);

	boolean isEnabled();
	
	void enable();
	
	void disable();
}

