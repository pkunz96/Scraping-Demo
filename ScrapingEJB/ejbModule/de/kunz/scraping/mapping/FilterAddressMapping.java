package de.kunz.scraping.mapping;

import java.util.*;

import de.kunz.scraping.util.filtering.*;


//TODO Implement a filter to remove invalid or incomplete instances of Address.

public class FilterAddressMapping extends AbstractFilter {

	public FilterAddressMapping() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void filter(Object obj) 
			throws FilterException {
		
	}

	@Override
	protected boolean isSupported(Object obj) {
		return false;
	}

	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		
	}

}
