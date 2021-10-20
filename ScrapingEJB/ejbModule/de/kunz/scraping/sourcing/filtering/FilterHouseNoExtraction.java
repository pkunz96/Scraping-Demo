package de.kunz.scraping.sourcing.filtering;

import java.util.Map;

import de.kunz.scraping.util.filtering.*;

/**
 * Extracts from an instance of {@link StringBuilder} that represents a street and 
 * a house number the street.
 * 
 * @author Philipp Kunz
 */
public class FilterHouseNoExtraction extends AbstractFilter {

	@Override
	protected void filter(Object obj) 
			throws FilterException {
		if(obj == null) {
			throw new NullPointerException();
		}
		else if(!isSupported(obj)) {
			throw new IllegalArgumentException(); 
		}
		else {
			StringBuilder streetHouseNoStr = (StringBuilder) obj;
			final int strLength = streetHouseNoStr.length();
			for(int index = 0; index < strLength; index++) {
				char curChar = streetHouseNoStr.charAt(index);
				if(Character.isDigit(curChar)) {
					streetHouseNoStr.delete(0, index);
					break;
				}
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
