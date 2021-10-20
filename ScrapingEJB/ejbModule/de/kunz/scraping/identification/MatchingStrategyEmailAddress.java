package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.data.entity.*;


public final class MatchingStrategyEmailAddress extends AbstractMatchingStrategy {

	private static final String PARAM_NAME_TYPE = "type";

	
	private static final String PARAM_DEFAULT_VALUE_TYPE_PERSON = "person";
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_BUSINESS = "business";
	
	
    private static String normalizeEmailAddress(String emailAddress) {
    	if(emailAddress == null) {
    		return null;
    	} 
    	else {
    		return emailAddress.toLowerCase();
    	}
    }
	
    
	private String type;

	
	public MatchingStrategyEmailAddress() {
		super();
		this.type = PARAM_DEFAULT_VALUE_TYPE_PERSON;
	}
	
	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		final String typeStr = this.extractParameter(paramterMap, PARAM_NAME_TYPE);
		if(typeStr != null && (PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(typeStr) && PARAM_DEFAULT_VALUE_TYPE_BUSINESS.equals(typeStr))) {
			this.type = typeStr;
		}
	}

	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		boolean matches = false;
		final List<EmailAddress> firstBrokerEmailAddresses = fetchEmailAddresses(firstBroker);
		final List<EmailAddress> secondBrokerEmailAddresses = fetchEmailAddresses(secondBroker);
		if((firstBrokerEmailAddresses != null) && (secondBrokerEmailAddresses != null)) {
			for(EmailAddress firstBrokerEmailAddress : firstBrokerEmailAddresses) {
				for(EmailAddress secondBrokerEmailAddress : secondBrokerEmailAddresses) {
					String firstBrokerEmailAddressStr = normalizeEmailAddress(firstBrokerEmailAddress.getEmailAddress());
					String secondBrokerEmailAddressStr = normalizeEmailAddress(secondBrokerEmailAddress.getEmailAddress());
					matches = (firstBrokerEmailAddressStr != null) && firstBrokerEmailAddressStr.equals(secondBrokerEmailAddressStr);
					if(matches) {
						break; 
					} 
				}
				if(matches) {
					break; 
				} 
			}
		} 
		return matches;
	}
	
	private List<EmailAddress> fetchEmailAddresses(Broker broker) {
		List<EmailAddress> emailAddressList = null;
		if(PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(this.type)) {
			final Person person = broker.getPerson();
			if(person != null) {
				emailAddressList = person.getEmailAddressList();
			}
		}
		else {
			final Business business = broker.getBusiness();
			if(business != null) {
				emailAddressList = business.getEmailAddressList();
			}
		}
		return emailAddressList; 
	}
}
 