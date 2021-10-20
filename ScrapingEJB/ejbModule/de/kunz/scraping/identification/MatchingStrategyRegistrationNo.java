package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.data.entity.*;

public final class MatchingStrategyRegistrationNo extends AbstractMatchingStrategy {
	
	public MatchingStrategyRegistrationNo() {
		super();
	}
	
	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		
	}

	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		boolean containsMatch = false;
		final List<BrokerConcessionMapping> firstBrokerConcessionMapping = firstBroker.getBrokerConcessionMappingList();
		final List<BrokerConcessionMapping> secondBrokerConcessionMapping = secondBroker.getBrokerConcessionMappingList();
		int index = 0;
		while(!containsMatch
				&& index < firstBrokerConcessionMapping.size()) {
			BrokerConcessionMapping firstBrokerMapping = firstBrokerConcessionMapping.get(index);
			String firstBrokerRegistrationNo = firstBrokerMapping.getRegisterNo();
			firstBrokerRegistrationNo = firstBrokerRegistrationNo.toLowerCase();
			if(firstBrokerRegistrationNo != null) {
				for(BrokerConcessionMapping secondBrokerMapping : secondBrokerConcessionMapping) {
					String secondRegistrationNo = secondBrokerMapping.getRegisterNo();
					if(secondRegistrationNo != null) {
						secondRegistrationNo = secondRegistrationNo.toLowerCase();
						if(firstBrokerRegistrationNo.matches(secondRegistrationNo)) {
							containsMatch = true;
							break;
						}
					}
				}
			}
			index++;
		}
		return containsMatch;
	}

}
