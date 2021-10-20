package de.kunz.scraping.mapping;

import java.util.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.filtering.*;

/**
 * Transforms instances of {@link PhoneNuber} into a standardized format.
 * 
 * @author Philipp Kunz
 */
public final class FilterPhoneNumberMapping extends AbstractFilter {

	/**
	 * Extracts the digits from the passed phone number and returns them in a 
	 * new string. The sequence is preserved.
	 * 	 
	 * @param phoneNumberStr a string representing a phone number. 
	 * @return a string containing the extracted digits.
	 */
	private static String extractDigits(String phoneNumberStr) {
		final StringBuilder phoneNumberBuilder = new StringBuilder();
		char[] phoneNumberCharArr = phoneNumberStr.toCharArray();
		for(char character : phoneNumberCharArr) {
			if(Character.isDigit(character)) {
				phoneNumberBuilder.append(character);
			}
		}
		phoneNumberStr = phoneNumberBuilder.toString();
		return phoneNumberStr;
	}
	
	/**
	 * Returns a list of instances of {@link PhoneNumber} directly or indirectly referenced
	 * by the passed instance of {@link Broker}.
	 * 
	 * @param broker an instance of {@link Broker}
	 * @return a list of phone numbers.
	 */
	private static List<PhoneNumber> getPhoneNumberList(Broker broker) {
		final List<PhoneNumber> phoneNumberList = new LinkedList<>();
		final Person person = broker.getPerson();
		final Business business = broker.getBusiness();
		if(person != null) {
			phoneNumberList.addAll(person.getPhoneNumberList());
		}
		if(business != null) {
			phoneNumberList.addAll(business.getPhoneNumberList());
		}
		return phoneNumberList;
	}
	
	private boolean isInitialized;
		
	public FilterPhoneNumberMapping() {
		super();
		this.isInitialized = false;
	}

	@Override
	public void filter(Object obj) throws FilterException {
 		if(obj == null) {
			throw new NullPointerException();
		}
		else if(!isInitialized) {
			throw new IllegalStateException();
		}
		else if(!isSupported(obj)) {
			throw new IllegalArgumentException();
		}
		else {
			Broker broker = (Broker) obj;
			final List<PhoneNumber> phoneNumberList = getPhoneNumberList(broker);
			for(PhoneNumber phoneNumber : phoneNumberList) {
				String phoneNumberStr = phoneNumber.getPhoneNumber();
				if(phoneNumberStr != null) {
					phoneNumberStr = extractDigits(phoneNumberStr);
					phoneNumber.setPhoneNumber(phoneNumberStr);
				}
			}  
		}	
	}

	@Override
	public boolean isSupported(Object obj) {
		if(!isInitialized) {
			throw new IllegalStateException();
		}
		else if(obj instanceof Broker) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		if(filterConfiguration == null) {
			throw new NullPointerException();
		}
		else {
			this.isInitialized = true;
		}		
	}
}

