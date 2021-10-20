package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IBusinessRelationProvider extends Iterator<Void> {

	String getTitle();

	String getLegalStatus();
		
	String getCommissioningFinancialProductProviderName();
	
}