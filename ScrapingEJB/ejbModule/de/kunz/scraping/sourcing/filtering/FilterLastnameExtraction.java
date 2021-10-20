package de.kunz.scraping.sourcing.filtering;

import java.util.*;

import de.kunz.scraping.util.filtering.*;

public class FilterLastnameExtraction extends AbstractFilter {

	public FilterLastnameExtraction() {
		super();
	}

	@Override
	protected void filter(Object obj)
			throws FilterException {
		StringBuilder prenameLastnameStr = (StringBuilder) obj;
		final int strLength = prenameLastnameStr.length();
		for(int index = (strLength - 1); index >= 0; index--) {
			char curChar = prenameLastnameStr.charAt(index);
			if(curChar == ' ') {
				prenameLastnameStr.delete(0, index);
				break;
			}
		}
	}

	@Override
	protected boolean isSupported(Object obj) {
		return (obj instanceof StringBuilder);
	}

	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		
	}

}
