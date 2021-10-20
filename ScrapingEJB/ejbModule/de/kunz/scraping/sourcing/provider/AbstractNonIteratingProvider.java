package de.kunz.scraping.sourcing.provider;

import java.util.*;

import org.json.simple.*;
import org.jsoup.nodes.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.util.*;

abstract class AbstractNonIteratingProvider extends AbstractProvider {

	private static State CONSTRUCTED = new State("CONSTRUCTED", 0);
		
	private static State INITIALIZING = new State("INITIALIZING", 1);
	
	private static State INITIALIZED = new State("INITIALIZED", 2);
	
	private static List<State> STATE_LIST = new LinkedList<>();

	static {
		STATE_LIST.add(CONSTRUCTED);
		STATE_LIST.add(INITIALIZING);
		STATE_LIST.add(INITIALIZED);
	}
	
	private MasterDataProviderImpl masterDataProvider;
	
	private IProviderConfiguration providerConf;
	
	public AbstractNonIteratingProvider() {
		super();
		this.stateMachine = new LinearStateMaschine(STATE_LIST, CONSTRUCTED);
	}
	
	private final LinearStateMaschine stateMachine;
	 
	@Override
	final String getString(String valueName) {
		final String strValue;
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(this.masterDataProvider.isCurrentObjectAvailable()) {
			if(valueName == null) {
				strValue = null;
			}
			else {
				final IValueConfiguration valueConf = providerConf.getValueConfiguration(valueName);
				if(valueConf == null) {
					strValue = null;
				}
				else {
					final SourceType srcType = valueConf.getSourceType();
					if(SourceType.JSON.equals(srcType)) {
						final JSONObject curJSONObject = this.masterDataProvider.getCurrentJSONObject();
						strValue = super.getString(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(srcType)) {
						final Element curElement = this.masterDataProvider.getCurrentElement();
						strValue = super.getString(valueConf, curElement);
					}
					else {
						throw new IllegalArgumentException();
					}
				}
			}
		}
		else {
			throw new IllegalStateException();
		}
		return strValue;
	}

	@Override
	final Boolean getBoolean(String valueName) {
		final Boolean booleanValue;
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(this.masterDataProvider.isCurrentObjectAvailable()) {
			if(valueName == null) {
				booleanValue = null;
			}
			else {
				final IValueConfiguration valueConf = providerConf.getValueConfiguration(valueName);
				if(valueConf == null) {
					booleanValue = null;
				}
				else {
					final SourceType srcType = valueConf.getSourceType();
					if(SourceType.JSON.equals(srcType)) {
						final JSONObject curJSONObject = this.masterDataProvider.getCurrentJSONObject();
						booleanValue = super.getBoolean(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(srcType)) {
						final Element curElement = this.masterDataProvider.getCurrentElement();
						booleanValue = super.getBoolean(valueConf, curElement);
					}
					else {
						throw new IllegalArgumentException();
					}
				}
			}
		}
		else {
			throw new IllegalStateException();
		}
		return booleanValue;
	}

	final List<String> getStringCollection(String valueName) {
		final List<String> strCollection;
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(this.masterDataProvider.isCurrentObjectAvailable()) {
			if(valueName == null) {
				strCollection = null;
			}
			else {
				final IValueConfiguration valueConf = providerConf.getValueConfiguration(valueName);
				if(valueConf == null) {
					strCollection = null;
				}
				else {
					final SourceType srcType = valueConf.getSourceType();
					if(SourceType.JSON.equals(srcType)) {
						final JSONObject curJSONObject = this.masterDataProvider.getCurrentJSONObject();
						strCollection = super.getStringCollection(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(srcType)) {
						final Element curElement = this.masterDataProvider.getCurrentElement();
						strCollection = super.getStringCollection(valueConf, curElement);
					}
					else {
						throw new IllegalArgumentException();
					}
				}
			}
		}
		else {
			throw new IllegalStateException();
		}
		return strCollection;
	}
	
	@Override
	final boolean isJSONSupportRequired() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		return isSupportForSourceTypeRequired(SourceType.JSON);
	}

	@Override
	final boolean isHTMLSupportRequired() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		return isSupportForSourceTypeRequired(SourceType.HTML);
	}
	
	final void startInit() {
		this.stateMachine.assureIsEqual(CONSTRUCTED);
		this.stateMachine.setState(INITIALIZING);
	}
	
	final void setMasterDataProvider(MasterDataProviderImpl masterDataProvider) {
		if(masterDataProvider == null) {
			throw new NullPointerException();
		}
		else if(masterDataProvider.getProviderConfiguration() == null) {
			throw new IllegalArgumentException();
		}
		else {
			this.stateMachine.assureIsEqual(INITIALIZING);
			if(masterDataProvider.getProviderConfiguration() != null) {
				this.masterDataProvider = masterDataProvider;
				this.providerConf = masterDataProvider.getProviderConfiguration();
			}
			else {
				throw new IllegalArgumentException();
			}
		}
	} 

	final void finalizeInit() {
		this.stateMachine.assureIsEqual(INITIALIZING);
		this.stateMachine.setState(INITIALIZED);		
	}
	
	private boolean isSupportForSourceTypeRequired(SourceType sourceType) {
		final List<String> valueNameList = getRequiredValueNames();
		final IProviderConfiguration providerConfiguration = this.masterDataProvider.getProviderConfiguration();
		for(String valueName : valueNameList) {
			IValueConfiguration valueConfiguration = providerConfiguration.getValueConfiguration(valueName);
			if(sourceType.equals(valueConfiguration.getSourceType())) {
				return true;
			}
		}
		return false;
	}
		
	@Override
	final void onRootObjectChanged() {
		//Ignore.
	}

	abstract List<String> getRequiredValueNames();
}
