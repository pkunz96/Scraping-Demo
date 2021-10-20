package de.kunz.scraping.sourcing.provider;

final class DefaultMobilePhoneNumberProvider extends AbstractIteratingProvider implements IMobilePhoneNumberProvider {

	public DefaultMobilePhoneNumberProvider() {
		super();
	}

	@Override
	public String getPhoneNumber() {
		return super.getString("mobilePhoneNumber");
	}

	@Override
	public Boolean isPrivatePhoneNumber() {
		return super.getBoolean("isPrivateMobilePhoneNumber");
	}	
}
  