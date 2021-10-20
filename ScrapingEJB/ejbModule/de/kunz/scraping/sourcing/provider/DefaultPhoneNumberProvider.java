package de.kunz.scraping.sourcing.provider;

final class DefaultPhoneNumberProvider extends AbstractIteratingProvider implements IPhoneNumberProvider {

	
	public DefaultPhoneNumberProvider() {
		super();
	}

	@Override
	public String getPhoneNumber() {		
		return super.getString("phoneNumber");
	}
 
	@Override 
	public Boolean isPrivatePhoneNumber() {
		return super.getBoolean("isPrivatePhoneNumber");
	}
}
