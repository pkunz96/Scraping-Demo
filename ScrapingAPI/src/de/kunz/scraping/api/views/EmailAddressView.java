package de.kunz.scraping.api.views;

import de.kunz.scraping.data.entity.Datasource;
import de.kunz.scraping.data.entity.EmailAddress;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class EmailAddressView {


	private Long emailAddressID = null;
	
	private String emailAddress = null;

	private Long datasourceID = null;
	
	public EmailAddressView(EmailAddress emailAddress, IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				this.emailAddressID = emailAddress.getEmailAddressId();
			}
			else {
				this.emailAddressID = idGenerator.next();
			}
		}
		{
			this.emailAddress = emailAddress.getEmailAddress();
		}
		final Datasource datasource = emailAddress.getDatasource();
		{
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
		}
	}

	public Long getEmailAddressID() {
		return emailAddressID;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}
	
}
