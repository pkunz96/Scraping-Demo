package de.kunz.scraping.mapping;

import java.util.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.filtering.*;


/**
 * Transforms instances of {@link EmailAddress} into standardized format. 
 *  
 * @author Philipp Kunz
 */
public final class FilterEmailAddressMapping extends AbstractFilter {

	/**
	 * Returns a list of instances of {@link EmailAddress} directly or indirectly 
	 * referenced by the passed instance of {@link Broker}.
	 */
	private static List<EmailAddress> getEmailAddressList(Broker broker) {
		final List<EmailAddress> emailAddressList = new LinkedList<>();
		final Person person = broker.getPerson();
		final Business business = broker.getBusiness();
		if(person != null) {
			emailAddressList.addAll(person.getEmailAddressList());
		}
		if(business != null) {
			emailAddressList.addAll(business.getEmailAddressList());
		}
		return emailAddressList;
	}
	
	private boolean isInitialized;
	
	public FilterEmailAddressMapping() {
		super();
		this.isInitialized = false;
	}
	
	@Override
	public void filter(Object obj) 
			throws FilterException { 
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
			final Broker broker = (Broker) obj;
			final List<EmailAddress> emailAddressList = getEmailAddressList(broker);
			for(EmailAddress emailAddress : emailAddressList) {
				String emailAddressStr = emailAddress.getEmailAddress();
				if(emailAddressStr != null) {
					emailAddressStr = emailAddressStr.trim().toLowerCase();
					emailAddress.setEmailAddress(emailAddressStr);
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
