package de.kunz.scraping.sourcing.provider;

import de.kunz.scraping.conf.*;

public interface IMasterDataProviderFactory {
	
	public static IMasterDataProviderFactory getInstance() {
		return new MasterDataProviderFactoryImpl();
	}
	
	IMasterDataProvider createMasterDataProvider(IProviderConfiguration providerConf, String src);
	
}
