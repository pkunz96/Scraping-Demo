package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.data.entity.*; 

public final class MatchingStrategyAddress extends AbstractMatchingStrategy {

	
	private static final String PARAM_NAME_TYPE = "type";
	
	private static final String PARAM_NAME_MAX_EDIT_DISTANCE = "maxAddressEditDistance";

	
	private static final int PARAM_DEFAULT_VALUE_MAX_EDIT_DISTANCE = 4;
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_PERSON = "person";
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_BUSINESS = "business";
	
	
	private static final String PARAM_REGEX_MAX_EDIT_DISTANCE = "[0-9]+";
	
	
    private static String normalizeAddress(Address address) {
    	final StringBuilder addressBuilder = new StringBuilder("");
    	if(address == null) {
    		return null;
    	}
    	else {
    		final String streetStr = toLowerCase(address.getStreet());
    		final String houseNoStr = toLowerCase(address.getHouseNo());
    		final String zipCodeStr = toLowerCase(address.getZipCode());
    		final String townStr = toLowerCase(address.getTown());
    		final String countryStr;
    		{
    			final Country country = address.getCountry();
    			if(country != null) {
    				countryStr = country.getCountryName();
    			}
    			else {
    				countryStr = null;
    			}
    		}
    		addressBuilder.append(streetStr);
    		addressBuilder.append(houseNoStr);
    		addressBuilder.append(zipCodeStr);
			addressBuilder.append(townStr);
			addressBuilder.append(countryStr);
    	}
    	return addressBuilder.toString();
    }
    
    private static String toLowerCase(String str) {
    	if(str == null) {
    		return null;
    	}
    	else {
    		return str.toLowerCase();
    	}
    }
    
	
	private String type;

	private int maxAddressEditDistance;
	
	
	public MatchingStrategyAddress() {
		super();
		this.type = PARAM_DEFAULT_VALUE_TYPE_PERSON;
		this.maxAddressEditDistance = PARAM_DEFAULT_VALUE_MAX_EDIT_DISTANCE;
	}

	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		final String maxAdreessEditDistanceStr = extractParameter(paramterMap, PARAM_NAME_MAX_EDIT_DISTANCE);
		final String typeStr = extractParameter(paramterMap, PARAM_NAME_TYPE);
		if((maxAdreessEditDistanceStr != null) && maxAdreessEditDistanceStr.matches(PARAM_REGEX_MAX_EDIT_DISTANCE)) {
			this.maxAddressEditDistance = Integer.parseInt(maxAdreessEditDistanceStr);
		}
		if((typeStr != null) && (PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(typeStr) || PARAM_DEFAULT_VALUE_TYPE_BUSINESS.equals(typeStr))) {
			this.type = typeStr;
		}
	}

	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		boolean matches = false;
		final String firstBrokerAddressStr = normalizeAddress(fetchAddress(firstBroker));
		final String seconderBrokerAdressStr = normalizeAddress(fetchAddress(secondBroker));
		if((firstBrokerAddressStr != null) && (seconderBrokerAdressStr != null)) {
			final int editDistance = EditDistance.calculateEditDistance(firstBrokerAddressStr, seconderBrokerAdressStr);
			matches = (editDistance <= this.maxAddressEditDistance);
		}
		return matches;
	}
	
	private Address fetchAddress(Broker broker) {
		Address address = null;
		if(PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(this.type)) {
			final Person person = broker.getPerson();
			if(person != null) {
				address = person.getAddress();
			}
		}
		else {
			final Business business = broker.getBusiness();
			if(business != null) {
				address = business.getAddress();
			}
		}
		return address;
	}
}
