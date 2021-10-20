package de.kunz.scraping.sourcing.filtering;

import java.util.Map;

import de.kunz.scraping.util.filtering.*;


public class FilterRegisterNoExtraction extends AbstractFilter {

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
			final StringBuilder registerNoStrBuilder = (StringBuilder) obj;
			{
				int prefixBeginIndex = 0;
				int prefixEndIndex = registerNoStrBuilder.indexOf("D-");
				if(prefixEndIndex  != -1 && prefixEndIndex >= prefixBeginIndex) {
					registerNoStrBuilder.delete(prefixBeginIndex, prefixEndIndex);
				}
			}
			{
				int suffixBeginIndex = registerNoStrBuilder.lastIndexOf("-") + 3;
				int suffixEndIndex = registerNoStrBuilder.length();
				if(suffixBeginIndex != -1 && suffixEndIndex >= suffixBeginIndex) {
					registerNoStrBuilder.delete(suffixBeginIndex, suffixEndIndex);
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
