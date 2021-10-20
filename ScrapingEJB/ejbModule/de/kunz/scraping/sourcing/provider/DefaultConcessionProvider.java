package de.kunz.scraping.sourcing.provider;

final class DefaultConcessionProvider extends AbstractIteratingProvider implements IConcessionProvider {

	public DefaultConcessionProvider() {
		super();
	}


	@Override
	public String getConcession() {
		return super.getString("concession");
	}

	@Override
	public String getRegisterNo() {
		return super.getString("registerNo");
	}
}
 