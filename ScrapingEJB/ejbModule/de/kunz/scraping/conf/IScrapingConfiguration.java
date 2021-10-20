package de.kunz.scraping.conf;

public interface IScrapingConfiguration {

	public static IScrapingConfiguration getInstance() {
		return XMLScrapingConfiguration.getInstance();
	}
	
	IProxyConfiguration getProxyConfiguration();
	
	ISourcingConfiguration getSourcingConfiguration();

	IMappingConfiguration getMappingConfiguration();
	
	IIdentificationConfiguration getIdentificationConfiguration();
	
	IReductionConfiguration getReductionConfiguration();
	
	ISynchronizationConfiguration getSynchronizationConfiguration();
}
