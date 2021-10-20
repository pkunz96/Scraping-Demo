package de.kunz.scraping.conf;

import java.util.List;

public interface IValueContextConfiguration {
	
	String getValueContextName();
	
	void setValueContextName(String valueContextName);
	
	SourceType getSourceType();
	
	void setSourceType(SourceType sourceType);
	
	List<String> getValueContextKeySequence(); 
	
	void setValueContextKeySequence(List<String> valueContextKeySeq);
	
	IValueConfiguration getValueConfiguration(String valueName);
	
	IValueConfiguration addValueConfiguration(String valueName); 

	List<IValueConfiguration> getValueConfigurations();
}
 