package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.data.entity.*;

public final class MatchingStrategyPhoneNumber extends AbstractMatchingStrategy {


	private static final String PARAM_NAME_TYPE = "type";

	
	private static final String PARAM_DEFAULT_VALUE_TYPE_PERSON = "person";
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_BUSINESS = "business";
	
	
    private static String normalizePhoneNumber(String phoneNumber) {
    	final StringBuilder phoneNumberBuilder;
    	if(phoneNumber == null) {
    		return null;
    	}
    	else {
    		phoneNumberBuilder = new StringBuilder("");
    		for(int index = 0; index < phoneNumber.length(); index++) {
    			char charactedAtIndex = phoneNumber.charAt(index);
    			if(Character.isDigit(charactedAtIndex)) {
    				phoneNumberBuilder.append(charactedAtIndex);
    			}
    		}
    	}
    	return phoneNumberBuilder.toString();
    }
    
	
	private String type;
	
	
	public MatchingStrategyPhoneNumber() {
		this.type = PARAM_NAME_TYPE; 
	} 
	
	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		final String typeStr = this.extractParameter(paramterMap, PARAM_NAME_TYPE);
		if(typeStr != null || (PARAM_DEFAULT_VALUE_TYPE_PERSON.matches(typeStr) || PARAM_DEFAULT_VALUE_TYPE_BUSINESS.matches(typeStr))) {
			this.type = typeStr;
		}
	} 
 
	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		boolean matches = false;
		final List<PhoneNumber> firstBrokerPhoneNumbers = fetchPhoneNumbers(firstBroker);
		final List<PhoneNumber> secondBrokerPhoneNumbers = fetchPhoneNumbers(secondBroker);
		if((firstBrokerPhoneNumbers != null)
				&& (secondBrokerPhoneNumbers != null)) {
			for(PhoneNumber firstBrokerPhoneNumber : firstBrokerPhoneNumbers) {
				for(PhoneNumber secondBrokerPhoneNumber : secondBrokerPhoneNumbers) {
					String firstBrokerPhoneNumberStr = normalizePhoneNumber(firstBrokerPhoneNumber.getPhoneNumber());
					String secondBrokerPhoneNumberStr = normalizePhoneNumber(secondBrokerPhoneNumber.getPhoneNumber());
					if(firstBrokerPhoneNumberStr != null 
							&& firstBrokerPhoneNumberStr.equals(secondBrokerPhoneNumberStr)) {
						matches = true;
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

	private List<PhoneNumber> fetchPhoneNumbers(Broker broker) {
		List<PhoneNumber> phoneNumberList = null;
		if(PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(this.type)) {
			final Person person = broker.getPerson();
			if(person != null) {
				phoneNumberList = person.getPhoneNumberList();
			}
		}
		else {
			final Business business = broker.getBusiness();
			if(business != null) {
				phoneNumberList = business.getPhoneNumberList();
			}
		}
		return phoneNumberList;
	} 
}
