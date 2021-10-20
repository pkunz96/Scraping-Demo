package de.kunz.scraping.conf;

import java.util.*;

public interface IValueConfiguration {
	
	String getValueName();
	
	void setValueName(String valueName);

	SourceType getSourceType();
	 
	void setSourceType(SourceType sourceType);
	
	List<String> getRelativeKeySequence();
	
	void setRelativeKeySequence(List<String> relativeKeySeq);
	
	String getFilterChainName();
	
	void setFilterChainName(String filterChainName);
	
	String getReductionChainName();
	
	void setReductionChainName(String reductionChainName);
	
	Map<String, String> getParameterMap();
		
	void setParameterMap(Map<String, String> paramMap);
	
}
  