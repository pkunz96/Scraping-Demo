package de.kunz.scraping.sourcing.provider;

import java.util.*;

final class DefaultBusinessProvider extends AbstractNonIteratingProvider implements IBusinessProvider {

	private static final String VALUE_NAME_FIRM = "firm";
	
	private static final String VALUE_NAME_IS_SMALL_BUSINESS = "isSmallBusiness";
	
	private static final String VALUE_NAME_COMPANY_REGISTRATION_NO = "companyRegistrationNo";

	private static final String VALUE_NAME_STREET = "businesStreet";
	
	private static final String VALUE_NAME_HOUSE_NO = "businessHouseNo";
	
	private static final String VALUE_NAME_ZIP_CODE = "businessZipCode";
	
	private static final String VALUE_NAME_TOWN = "businessTown";
	
	private static final String VALUE_NAME_COUNTRY_CODE = "businessCountryCode";
	
	private static final List<String> REQUIRED_VALUE_NAMES = new LinkedList<>();
	
	static {
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_FIRM);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_IS_SMALL_BUSINESS);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_COMPANY_REGISTRATION_NO);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_STREET);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_HOUSE_NO);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_ZIP_CODE);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_TOWN);
		REQUIRED_VALUE_NAMES.add(VALUE_NAME_COUNTRY_CODE);
	}
	
	public DefaultBusinessProvider() {
		super();
	}

	@Override
	public String getFirm() {
		return this.getString(VALUE_NAME_FIRM);
	}

	@Override
	public Boolean isSmallBusiness() {
		return this.getBoolean(VALUE_NAME_IS_SMALL_BUSINESS);
	}

	@Override
	public String getCompanyRegistrationNo() {
		return this.getString(VALUE_NAME_COMPANY_REGISTRATION_NO);
	}

	@Override
	public String getStreet() {
		return this.getString(VALUE_NAME_STREET);
	}

	@Override
	public String getHouseNo() {
		return this.getString(VALUE_NAME_HOUSE_NO);
	}

	@Override
	public String getZipCode() {
		return this.getString(VALUE_NAME_ZIP_CODE);
	}

	@Override
	public String getTown() {
		return this.getString(VALUE_NAME_TOWN);
	}
 
	@Override 
	public String getCountryCode() {
		return this.getString(VALUE_NAME_COUNTRY_CODE);
	}

	@Override
	List<String> getRequiredValueNames() {
		return REQUIRED_VALUE_NAMES;
	}
}
