package de.kunz.scraping.identification;

import java.util.Map;

import de.kunz.scraping.data.entity.Broker;

final class MatchingStrategyStatus extends AbstractMatchingStrategy {

	
	public MatchingStrategyStatus() {
		super();
	}
	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		
	}

	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		return !((firstBroker.getPerson() != null && secondBroker.getPerson() == null) || (firstBroker.getPerson() == null && secondBroker.getPerson() != null));
	}
}
