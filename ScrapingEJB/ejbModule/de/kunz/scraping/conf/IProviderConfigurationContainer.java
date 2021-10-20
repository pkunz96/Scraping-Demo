package de.kunz.scraping.conf;

interface IProviderConfigurationContainer {
	
	IProviderConfiguration getProviderConfiguration(String profileName);
	
	IProviderConfiguration createProviderConfiguration(String profileName);
	
	void removeProviderConfiguration(String profileName);
	
	boolean isProviderConfigurationAvailable(String profileName);
}
