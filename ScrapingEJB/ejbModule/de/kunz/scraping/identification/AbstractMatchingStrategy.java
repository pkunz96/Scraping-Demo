package de.kunz.scraping.identification;

import java.util.Map;

import de.kunz.scraping.data.entity.Broker;

abstract class AbstractMatchingStrategy implements MatchingStrategy {

	private static final String PARAM_NAME_IS_TRUE_FINAL = "isTrueFinal";
	
	private static final String PARAM_NAME_IS_FALSE_FINAL = "isFalseFinal";
	
	
	private boolean isInitialized;
	
	private boolean isTrueFinal;
	
	private boolean isFalseFinal;
	
	
	public AbstractMatchingStrategy() {
		this.isInitialized = false;
		this.isFalseFinal = true;
		this.isTrueFinal = true;
	}
	
	 
	@Override
	public final void init(Map<String, String> paramterMap) {
		if(paramterMap == null) {
			throw new NullPointerException();
		}
		else {
			final String isTrueFinalStr = extractParameter(paramterMap, PARAM_NAME_IS_TRUE_FINAL);
			final String isFalseFinalStr = extractParameter(paramterMap, PARAM_NAME_IS_FALSE_FINAL);
			if((isTrueFinalStr != null) && ("true".equals(isTrueFinalStr)|| "false".matches(isTrueFinalStr))) {
				this.isTrueFinal = Boolean.parseBoolean(isTrueFinalStr);
			}
			if((isFalseFinalStr != null) && ("true".equals(isFalseFinalStr)|| "false".matches(isFalseFinalStr))) {
				this.isFalseFinal = Boolean.parseBoolean(isFalseFinalStr);
			}
		}
		this.onInit(paramterMap);
		this.isInitialized = true;
	}
 
	@Override
	public final boolean isTrueFinal() {
		return this.isTrueFinal;
	}
	
	@Override
	public final boolean isFalseFinal() {
		return this.isFalseFinal;
	}

	@Override
	public final boolean match(Broker firstBroker, Broker secondBroker) {
		if((firstBroker == null) || (secondBroker == null)) {
			throw new NullPointerException();
		}
		else if(!this.isInitialized) {
			throw new IllegalStateException();
		}
		else {
			return this.onMatch(firstBroker, secondBroker);
		}
	}

	protected String extractParameter(Map<String, String> paramterMap, String paramterName) {
		if((paramterMap == null) || (paramterName == null)) {
			throw new NullPointerException();
		}
		else {
			return paramterMap.get(this.getClass().getName() + "." + paramterName);
		}
	}
	
	protected abstract void onInit(Map<String, String> paramterMap);

	protected abstract boolean onMatch(Broker firstBroker, Broker secondBroker);
}


 