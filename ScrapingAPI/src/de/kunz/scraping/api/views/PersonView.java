package de.kunz.scraping.api.views;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.exp.ai.scraping.data.entity.*;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class PersonView {

	private Long personID = null;
	
	private String prename = null;
	
	private String lastname = null;
	
	private List<PhoneNumberView> phoneNumbers = new LinkedList<>();
	
	private List<EmailAddressView> emailAddresses = new LinkedList<>();

	private AddressView address = null;
	
	private Date birthdate = null;
	
	private String portraitURL = null;
	
	private Long datasourceID = null;
	
	public PersonView(Person person, IDGeneratator idGenerator) {
		{
			if(idGenerator == null) {
				this.personID = person.getPersonId();
			}
			else {
				this.personID = idGenerator.next();
			}
		}
		{
				this.prename = person.getPrename();
				this.lastname = person.getLastname();
				this.birthdate = person.getBirthdate();
				this.portraitURL = "";
		}
		final Address address = person.getAddress();
		final List<PhoneNumber> phoneNumberList = person.getPhoneNumberList();
		final List<EmailAddress> emailAddressList = person.getEmailAddressList();
		final Datasource datasource = person.getDatasource();
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

	public Long getPersonID() {
		return personID;
	}

	public String getPrename() {
		return prename;
	}

	public String getLastname() {
		return lastname;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public String getPortraitURL() {
		return portraitURL;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}	
	
}
