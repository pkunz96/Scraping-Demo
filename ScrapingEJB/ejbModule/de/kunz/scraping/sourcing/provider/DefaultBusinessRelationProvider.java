package de.kunz.scraping.sourcing.provider;

final class DefaultBusinessRelationProvider extends AbstractIteratingProvider implements IBusinessRelationProvider {

	public DefaultBusinessRelationProvider() {
		super();
	}

	@Override
	public String getTitle() {
		return super.getString("businessRelationTitle");
	}

	@Override
	public String getLegalStatus() {
		return super.getString("businessRelationLegalStatus");
	}

	@Override
	public String getCommissioningFinancialProductProviderName() {
		return super.getString("businessRelationCommissioningFinancialProductProviderName");
	}

}
