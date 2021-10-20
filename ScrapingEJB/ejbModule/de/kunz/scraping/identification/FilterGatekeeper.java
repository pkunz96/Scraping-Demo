package de.kunz.scraping.identification;

import java.util.Map;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.filtering.*;


public final class FilterGatekeeper extends AbstractFilter {

	private boolean isInitialized;
	
	@Override
	protected void filter(Object obj) 
			throws FilterException {
		if(!isInitialized) {
			throw new IllegalStateException();
		}
		else if(!isSupported(obj)) {
			throw new IllegalArgumentException();
		}
		else {
			final IdentificationTask identityRequest = (IdentificationTask) obj;
			{
				final Broker firstBroker = identityRequest.getFirstBroker();
				final Broker secondBroker = identityRequest.getSecondBroker();	
				if(firstBroker == null && secondBroker == null) {
					identityRequest.setIdentical(true);
					identityRequest.setCompleted(true);
				}
				else if (firstBroker == null || secondBroker == null) {
					identityRequest.setIdentical(false);
					identityRequest.setCompleted(true);
				}
			}
		}
	}

	@Override
	protected boolean isSupported(Object obj) {
		boolean isSupported = false;
		if(!isInitialized) {
			throw new IllegalStateException();
		}
		if(obj instanceof IdentificationTask) {
			isSupported = true;
		}
		return isSupported;
	}

	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		this.isInitialized = true;
	}
}
