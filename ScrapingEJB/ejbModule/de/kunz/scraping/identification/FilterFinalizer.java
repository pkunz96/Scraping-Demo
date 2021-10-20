package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.util.filtering.*;

public final class FilterFinalizer extends AbstractFilter {

	private boolean isInitialized;
	
	public FilterFinalizer() {
		super();  
		this.isInitialized = false;
	}
	
	@Override 
	protected void filter(Object obj) 
			throws FilterException {
		if(!this.isInitialized) {
			throw new IllegalStateException();
		}
		else if(!isSupported(obj)) {
			throw new IllegalArgumentException();
		}
		else {
			final IdentificationTask identificationTask = (IdentificationTask) obj;
			identificationTask.setCompleted(true);
		}
	}

	@Override
	protected boolean isSupported(Object obj) {
		if(!this.isInitialized) {
			throw new IllegalStateException();
		}
		else {
			return (obj instanceof IdentificationTask);
		}
	}

	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		this.isInitialized = true;
	}

}
 