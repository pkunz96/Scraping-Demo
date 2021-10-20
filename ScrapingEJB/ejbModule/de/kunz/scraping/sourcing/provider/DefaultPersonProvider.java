package de.kunz.scraping.sourcing.provider;

import java.util.*;

final class DefaultPersonProvider extends AbstractNonIteratingProvider implements IPersonProvider {

	private static final String VALUE_NAME_SALUTATION = "saluation";
	
	private static final String VALUE_NAME_PRENAME = "prename";
	
	private static final String VALUE_NAME_LASTNAME = "lastname";
	
	private static final String VALUE_NAME_PORTRAIT_URL = "portraitUrl";
	
	private static final String VALUE_NAME_STREET = "personStreet";
	
	private static final String VALUE_NAME_HOUSE_NO = "personHouseNo";
	
	private static final String VALUE_NAME_ZIP_CODE = "personZipCode";
	
	private static final String VALUE_NAME_TOWN = "personTown";
	
	private static final String VALUE_NAME_COUNTRY_CODE = "personCountryCode";
	
	private static final List<String> REQUIRED_VALUE_NAMES = new LinkedList<>();
	
	static {
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_SALUTATION);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_PRENAME);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_LASTNAME);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_PORTRAIT_URL);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_STREET);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_HOUSE_NO);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_ZIP_CODE);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_TOWN);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_COUNTRY_CODE);
	}
	
	public DefaultPersonProvider() {
		super();
	}

	@Override
	public String getSalutation() {
		return super.getString(VALUE_NAME_SALUTATION);
	}

	@Override
	public String getPrename() {
		return super.getString(VALUE_NAME_PRENAME);

	}

	@Override
	public String getLastname() {
		return super.getString(VALUE_NAME_LASTNAME);
	}

	@Override
	public Date getBirthDate() {
		return null;
	}

	@Override
	public String getPortraitUrl() {
		return super.getString(VALUE_NAME_PORTRAIT_URL);
	}

	@Override
	public String getStreet() {
		return super.getString(VALUE_NAME_STREET);
	}

	@Override
	public String getHouseNo() {
		return super.getString(VALUE_NAME_HOUSE_NO);
	}

	@Override
	public String getZipCode() {
		return super.getString(VALUE_NAME_ZIP_CODE);
	}

	@Override
	public String getTown() {
		return super.getString(VALUE_NAME_TOWN);
	}

	@Override
	public String getCountryCode() {
		return super.getString(VALUE_NAME_COUNTRY_CODE);
	}

	@Override
	List<String> getRequiredValueNames() {
		return REQUIRED_VALUE_NAMES;
	}

}
