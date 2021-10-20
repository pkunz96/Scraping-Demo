package de.kunz.scraping.api.views;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class FinancialProductProviderView {

	private Long financialProductProviderId;

	private String financialProductName;

	private AddressView address;
	
	public FinancialProductProviderView(FinancialProductProvider financialProductProvider, IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				financialProductProviderId = financialProductProvider.getFinancialProductProviderId();
			}
			else {
				financialProductProviderId = idGenerator.next();
			}
		}
		{
			this.financialProductName = financialProductProvider.getFinancialProductName();
		}
		final Address address = financialProductProvider.getAddress();
		{
			if(address != null) {
				this.address = new AddressView(address, idGenerator);
			}
		}
	}

	public Long getFinancialProductProviderId() {
		return financialProductProviderId;
	}

	public String getFinancialProductName() {
		return financialProductName; 
	}

	public AddressView getAddress() {
		return address;
	}	
}
