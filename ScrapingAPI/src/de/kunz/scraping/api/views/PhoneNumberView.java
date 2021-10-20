package de.kunz.scraping.api.views;

import de.exp.ai.scraping.data.entity.Datasource;
import de.exp.ai.scraping.data.entity.PhoneNumber;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class PhoneNumberView {

	private Long phoneNumberID = null;
	
	private Boolean isMobilePhoneNumber = null;
	
	private String phoneNumber = null;

	private Long datasourceID = null;
	
	public PhoneNumberView(PhoneNumber phoneNumber, IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				this.phoneNumberID = phoneNumber.getPhoneNumberId();
			}
			else {
				this.phoneNumberID = idGenerator.next();
			}
		}
		{
			this.isMobilePhoneNumber = phoneNumber.isMobilePhoneNumber();
			this.phoneNumber = phoneNumber.getPhoneNumber();
		}
		final Datasource datasource = phoneNumber.getDatasource();
		{
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
		}
	}

	public Long getPhoneNumberID() {
		return phoneNumberID;
	}

	public Boolean getIsMobilePhoneNumber() {
		return isMobilePhoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}
	
}
