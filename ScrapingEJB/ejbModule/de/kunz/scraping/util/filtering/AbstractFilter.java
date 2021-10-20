package de.kunz.scraping.util.filtering;

import java.util.*;

public abstract class AbstractFilter {

	private int priority;
	
	protected AbstractFilter() {
		this.priority = -1;
	}
		
	protected abstract void filter(Object obj)	
		throws FilterException;

	protected abstract boolean isSupported(Object obj);

	protected abstract void loadConfiguration(Map<String, String> filterConfiguration);

	final void initFilter(Map<String, String> filterConfiguration) {
		final String PRIORITY_KEY = "priority";
		int priority;
		final String priorityStr = filterConfiguration.get(PRIORITY_KEY);
		if(priorityStr == null) {
			throw new IllegalArgumentException();
		}
		else {
			try {
				priority = Integer.parseInt(priorityStr);
			} catch(NumberFormatException e0) {
				throw new IllegalArgumentException(e0);
			}
		}		
		this.priority = priority;
		this.loadConfiguration(filterConfiguration);
	}

	final int getPriorty() {
		return priority;
	}	
}
