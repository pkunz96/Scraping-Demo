package de.kunz.scraping.conf;

import java.util.*;

public interface IProviderConfiguration {
	
	String getProfileName();
	
	void setProfileName(String profileName);
	
	SourceType getSourceType();
	
	void setSourceType(SourceType sourceType);
	
	IProxyConfiguration getProxyConfiguration();
	
	int getConnectionLimit();
	
	void setConnectionLimit(int connectionLimit);
	
	int getMinZipCodeDistance();
	
	void setMinZipCodeDistance(int minDistance);
	
	String getHTMLAdapterClassName(); 
	
	void setHTMLAdapterClassName(String className);
	
	String getJSONAdapterClassName();
	 
	void setJSONAdapterClassName(String className);
	
	List<String> getBaseKeySequence();

	void setBaseKeySequence(List<String> baseKeySequence);

	IValueContextConfiguration getValueContextConfiguration(String valueContextName);
	
	IValueContextConfiguration addValueContextConfiguration(String valueContextName);

	List<IValueContextConfiguration> getValueContextConfigurations();
	
	IValueConfiguration getValueConfiguration(String valueName);
	
	IValueConfiguration addValueConfiguration(String valueName);

	List<IValueConfiguration> getValueConfigurations();
	
}
 