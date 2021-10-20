package de.kunz.scraping.sourcing.provider;

final class DefaulEmailAddressProvider extends AbstractIteratingProvider implements IEmailAdressProvider {

	public DefaulEmailAddressProvider() {
		super();
	}

	@Override
	public String getEmailAddress() {
		return this.getString("emailAddress");
	}

	@Override
	public Boolean isPrivateEmailAddress() {
		return this.getBoolean("isPrivateEmailAddress");
	}
}
 