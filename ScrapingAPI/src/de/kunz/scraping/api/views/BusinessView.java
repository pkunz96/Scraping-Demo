package de.kunz.scraping.api.views;

import java.util.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.api.views.BrokerView.*;

public class BusinessView {

	private Long businessID = null;
	
	private String firm = null;
	
	private Boolean isSmallBusiness = null;
	
	private String companyRegistrationNo = null;
	
	private List<PhoneNumberView> phoneNumbers = new LinkedList<>();
	
	private List<EmailAddressView> emailAddresses = new LinkedList<>();

	private AddressView address = null;  
	
	private Long datasourceID = null; 
	
	public BusinessView(Business business , IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				this.businessID = business.getBusinessId();
			}
			else {
				this.businessID = idGenerator.next();
			}
		}
		{
			this.firm = business.getFirm();
			this.isSmallBusiness = business.isSmallBusiness();
			this.companyRegistrationNo = business.getCompanyRegistrationNo();
		}
		final Address address = business.getAddress();
		final List<PhoneNumber> phoneNumberList = business.getPhoneNumberList();
		final List<EmailAddress> emailAddressList = business.getEmailAddressList();
		final Datasource datasource = business.getDatasource();
		{
			if(address != null) {
				this.address = new AddressView(address, idGenerator);
			}
			if(phoneNumberList != null) {
				for(PhoneNumber phoneNumber: phoneNumberList) {
					this.phoneNumbers.add(new PhoneNumberView(phoneNumber, idGenerator));
				}
			}
			if(emailAddressList != null) {
				for(EmailAddress emailAddress: emailAddressList) {
					this.emailAddresses.add(new EmailAddressView(emailAddress, idGenerator));
				}
			}
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}

		}
	}

	public Long getBusinessID() {
		return businessID;
	}

	public String getFirm() {
		return firm;
	}

	public Boolean getIsSmallBusiness() {
		return isSmallBusiness;
	}

	public String getCompanyRegistrationNo() {
		return companyRegistrationNo;
	}

	public List<PhoneNumberView> getPhoneNumbers() {
		return phoneNumbers;
	}

	public List<EmailAddressView> getEmailAddresses() {
		return emailAddresses;
	}
  
	public AddressView getAddress() {
		return address;
	} 

	public Long getDatasourceID() {
		return datasourceID;
	}
}
