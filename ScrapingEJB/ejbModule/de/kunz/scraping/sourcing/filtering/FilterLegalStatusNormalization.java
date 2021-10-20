package de.kunz.scraping.sourcing.filtering;

import java.util.*;

import de.kunz.scraping.util.filtering.*;

public class FilterLegalStatusNormalization extends AbstractFilter {

	private Map<String, String> filterConfiguration;
	
	@Override
	protected void filter(Object obj) 
			throws FilterException {		
		final StringBuilder legalStatusStr = (StringBuilder) obj;
		if(legalStatusStr != null) {
			final String replacement = this.filterConfiguration.get(legalStatusStr.toString());
			if(replacement != null) {
				final int beginIndex = 0;
				final int endIndex = legalStatusStr.length();
				legalStatusStr.delete(beginIndex, endIndex);
				legalStatusStr.insert(beginIndex, replacement);
			}
		}
	}

	@Override
	protected boolean isSupported(Object obj) {
		return (obj instanceof StringBuilder);
	}

	//TODO Loading replacement from external configuration file.
	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		this.filterConfiguration = new HashMap<String, String>();
		this.filterConfiguration.put("Gebundener Versicherungsvermittler nach § 34d Abs. 7 Nr. 1 GewO", "Gebundener Versicherungsvertreter nach § 34d Abs. 7 GewO");		
	}
}
