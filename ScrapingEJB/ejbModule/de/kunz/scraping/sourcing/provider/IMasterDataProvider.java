package de.kunz.scraping.sourcing.provider;

import java.util.*;

/**
 * The interface represents a collection of information about instances of 
 * {@link Broker}. A Client can use an implementation of this interface
 * to iterate over the underlying instances of {@link Broker}.
 * 
 * @author Philipp Kunz
 */
public interface IMasterDataProvider extends Iterator<Void> {
	
	/**
	 * Returns an instance of {@link IPersonProvider}.
	 * 
	 * @return - an instance of {@link IPersonProvider}.
	 */
	IPersonProvider getPersonProvider();
	
	/**
	 * Returns an instance of {@link IBusinessProvider}.
	 * 
	 * @return - an instance of {@link IBusinessProvider}.
	 */
	IBusinessProvider getBusinessProvider();
	
	/**
	 * Returns an instance of {@link IBusinessRelationProvider}.
	 * 
	 * @return - an instance of {@link IBusinessRelationProvider}.
	 */
	IBusinessRelationProvider getBusinessRelationProvider();
	
	/**
	 * Returns an instance of {@link IPhoneNumberProvider}.
	 * 
	 * @return - an instance of {@link IPhoneNumberProvider}.
	 */
	IPhoneNumberProvider getPhoneNumberProvider();

	/**
	 * Returns an instance of {@link IMobilePhoneNumberProvider}.
	 * 
	 * @return - an instance of {@link IMobilePhoneNumberProvider}.
	 */
	IMobilePhoneNumberProvider getMobilePhoneNumberProvider();
	
	/**
	 * Returns an instance of {@link IEmailAdressProvider}.
	 * 
	 * @return - an instance of {@link IEmailAdressProvider}.
	 */
	IEmailAdressProvider getEmailAddressProvider();
	
	/**
	 * Returns an instance of {@link IPropertyProvider}.
	 * 
	 * @return - an instance of {@link IPropertyProvider}.
	 */
	IPropertyProvider getPropertyProvider();
	
	/**
	 * Returns an instance of {@link IConcessionProvider}.
	 * 
	 * @return - an instance of {@link IConcessionProvider}.
	 */
	IConcessionProvider getConcessionProvider();
}
