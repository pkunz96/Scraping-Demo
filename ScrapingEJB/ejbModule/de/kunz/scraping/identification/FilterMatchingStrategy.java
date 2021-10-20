package de.kunz.scraping.identification;

import java.util.*;
import java.util.logging.*;

import de.kunz.scraping.util.filtering.*;

import java.lang.reflect.*;

public class FilterMatchingStrategy extends AbstractFilter {
	
	private static final Logger LOG = Logger.getLogger(FilterMatchingStrategy.class.getName());
	
	private static final String MATCHING_STRATEGY_PATTERN = "matching_strategy_[0-9]+";
	
	private boolean isInitialized;
	
	private boolean isTrueFinal;
	
	private boolean isFalseFinal;
	
	private List<MatchingStrategy> matchingStrategyList;
	
	public FilterMatchingStrategy() {
		super();
		this.isInitialized = false;
		this.isTrueFinal = true;
		this.isFalseFinal = true;
		this.matchingStrategyList = new LinkedList<>(); 
	} 
	
	@Override
	protected void filter(Object obj) 
			throws FilterException {
		if(obj == null) {
			throw new NullPointerException();
		}
		else if(!isSupported(obj)) {
			throw new IllegalArgumentException();
		}
		else {
			final IdentificationTask identificationTask = (IdentificationTask) obj;
			if(!identificationTask.isCompleted()) {
				boolean matches = true;
				for(MatchingStrategy matchingStrategy : this.matchingStrategyList) {
					if(!matchingStrategy.match(identificationTask.getFirstBroker(), identificationTask.getSecondBroker())) {
						matches = false;
						break;
					}
				}
				identificationTask.setIdentical(matches);
				if((matches && this.isTrueFinal) 
						|| (!matches && this.isFalseFinal)) {
					identificationTask.setCompleted(true);
				}
			}
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
		if(filterConfiguration == null) {
			throw new NullPointerException();
		}
		else {
			for(Map.Entry<String, String> entry : filterConfiguration.entrySet()) {
				if(entry.getKey().matches(MATCHING_STRATEGY_PATTERN)) {
					final String classname = entry.getValue();
					try {
						final Class<?> filterClass = Class.forName(classname);
						final Constructor<?> constructor = filterClass.getConstructor(new Class[]{});
						final MatchingStrategy matching = (MatchingStrategy) constructor.newInstance();
						matching.init(filterConfiguration);
						this.matchingStrategyList.add(matching);
					} catch(Exception  e0) {
						LOG.log(Level.WARNING, e0.toString());
					}
				}
			}
			for(MatchingStrategy matchingStrategy : this.matchingStrategyList) {
				if(!matchingStrategy.isTrueFinal()) {
					this.isTrueFinal = false;
				}
			}
			for(MatchingStrategy matchingStrategy : this.matchingStrategyList) {
				if(!matchingStrategy.isFalseFinal()) {
					this.isFalseFinal = false;
				}
			}
			this.isInitialized = true; 
		}
	}
 
}
