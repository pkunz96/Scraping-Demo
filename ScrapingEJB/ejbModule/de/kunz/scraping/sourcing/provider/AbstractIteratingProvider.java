package de.kunz.scraping.sourcing.provider;

import java.util.*;

import org.jsoup.nodes.*;
import org.json.simple.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.util.*;

abstract class AbstractIteratingProvider extends AbstractProvider implements Iterator<Void>{

	private static State CONSTRUCTED = new State("CONSTRUCTED", 0);
	
	private static State INITIALIZING = new State("INITIALIZING", 1);
	
	private static State MASTER_DATA_PROVIDER_INITIALIZED = new State("MASTER_DATA_PROVIDER_INITIALIZED", 2);
	
	private static State VALUE_CONTEXT_CONF_INITIALIZED = new State("VALUE_CONTEXT_CONF_INITIALIZED", 3);
	
	private static State INITIALIZED = new State("INITIALIZED", 4);

	private static final List<State> STATE_LIST = new LinkedList<>();
	
	static {
		STATE_LIST.add(CONSTRUCTED);
		STATE_LIST.add(INITIALIZING);
		STATE_LIST.add(MASTER_DATA_PROVIDER_INITIALIZED);
		STATE_LIST.add(VALUE_CONTEXT_CONF_INITIALIZED);
		STATE_LIST.add(INITIALIZED);
	}
	
	private final LinearStateMaschine stateMachine;
	
	private MasterDataProviderImpl masterDataProvider;
		
	private IValueContextConfiguration valueContextConfiguration;
	
	private SourceType sourceType;
	
	private int objListIndex = -1;
		
	private List<JSONObject> jsonObjectList =  null;
	
	private List<Element> elementList = null;
	
	public AbstractIteratingProvider() {
		super();
		this.jsonObjectList = new LinkedList<>();
		this.elementList = new LinkedList<>();
		this.objListIndex = -1;
		this.stateMachine = new LinearStateMaschine(STATE_LIST, CONSTRUCTED);
	}

	@Override
	final public boolean hasNext() {
		boolean hasNext = false;
		this.stateMachine.assureIsEqual(INITIALIZED);
		final int nextObjListIndex = this.objListIndex + 1;
		if(SourceType.JSON.equals(sourceType)) {
			hasNext = (this.jsonObjectList != null) && (nextObjListIndex < this.jsonObjectList.size()); 
		}
		else if(SourceType.HTML.equals(sourceType)) {
			hasNext = (this.elementList != null) && (nextObjListIndex < this.elementList.size()); 
		} 
		else {
			throw new IllegalStateException();
		}
		return hasNext;
	}

	@Override
	final public Void next() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(hasNext()) {
			this.objListIndex++;
		}
		else {
			throw new NoSuchElementException();
		}
		return null;
	}

	@Override
	final void onRootObjectChanged() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(this.masterDataProvider.isCurrentObjectAvailable()) {
			if(SourceType.JSON.equals(sourceType)) {		
				this.jsonObjectList = new LinkedList<>();
				this.objListIndex = -1; 
				final JSONObject curJSONBaseObject = this.masterDataProvider.getCurrentJSONObject();
				this.jsonObjectList.addAll(ExtractionUtility.extractCollection(JSONObject.class, curJSONBaseObject, this.valueContextConfiguration.getValueContextKeySequence()));	
			}
			else if(SourceType.HTML.equals(sourceType)) {
				this.elementList = new LinkedList<>();
				this.objListIndex = -1;
				final Element curBaseElement = this.masterDataProvider.getCurrentElement();
				this.elementList.addAll(ExtractionUtility.extractCollection(Element.class, curBaseElement, this.valueContextConfiguration.getValueContextKeySequence()));
			}
			else {
				throw new IllegalStateException();
			}
		} 
		else {
			this.elementList = null;
			this.jsonObjectList = null;
			this.objListIndex = -1;
		}
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
		this.stateMachine.assureIsEqual(INITIALIZING);
		if(masterDataProvider.getProviderConfiguration() != null) {
			this.masterDataProvider = masterDataProvider;
		}
		else {
			throw new IllegalStateException();
		} 
		this.stateMachine.setState(MASTER_DATA_PROVIDER_INITIALIZED);
	}
	
	final void setValueContextName(String valueContextName) {
		if(valueContextName == null) {
			throw new NullPointerException();
		}
		else {
			this.stateMachine.assureIsMin(MASTER_DATA_PROVIDER_INITIALIZED);
			final IProviderConfiguration providerConfiguration = this.masterDataProvider.getProviderConfiguration();
			final IValueContextConfiguration valueContextConfiguration = providerConfiguration.getValueContextConfiguration(valueContextName);
			if(valueContextConfiguration == null) {
				throw new IllegalArgumentException();
			}
			else if(valueContextConfiguration.getSourceType() != null){
				this.valueContextConfiguration = valueContextConfiguration;
				this.sourceType = valueContextConfiguration.getSourceType();
				this.stateMachine.setState(VALUE_CONTEXT_CONF_INITIALIZED);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
	}
	
	final void finalizeInit() {
		this.stateMachine.assureIsEqual(VALUE_CONTEXT_CONF_INITIALIZED);
		this.stateMachine.setState(INITIALIZED);
	}
	
	@Override
	final boolean isJSONSupportRequired() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		return SourceType.JSON.equals(this.valueContextConfiguration.getSourceType());
	}

	@Override
	final boolean isHTMLSupportRequired() {
		this.stateMachine.assureIsEqual(INITIALIZED);
		return SourceType.HTML.equals(this.valueContextConfiguration.getSourceType());
	}

	
	@Override
	final String getString(String valueName) {
		final String strValue;
		this.stateMachine.assureIsEqual(INITIALIZED);
		if(this.isCurrentObjectAvailable()) {
			if(valueName == null) {
				strValue = null;
			}
			else {
				final IValueConfiguration valueConf = valueContextConfiguration.getValueConfiguration(valueName);
				if(valueConf == null) {
					strValue = null;
				}
				else { 
					if(SourceType.JSON.equals(this.sourceType)) {
						final JSONObject curJSONObject = this.getCurrentJSONObject();
						strValue = super.getString(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(this.sourceType)) {
						final Element curElement = this.getCurrentElement();						
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
		if(this.isCurrentObjectAvailable()) {
			if(valueName == null) {
				booleanValue = null;
			}
			else {
				final IValueConfiguration valueConf = valueContextConfiguration.getValueConfiguration(valueName);
				if(valueConf == null) {
					booleanValue = null;
				}
				else {
					if(SourceType.JSON.equals(this.sourceType)) {
						final JSONObject curJSONObject = this.getCurrentJSONObject();
						booleanValue = super.getBoolean(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(this.sourceType)) {
						final Element curElement = this.getCurrentElement();
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
		if(this.isCurrentObjectAvailable()) {
			if(valueName == null) {
				strCollection = null;
			}
			else {
				final IValueConfiguration valueConf = valueContextConfiguration.getValueConfiguration(valueName);
				if(valueConf == null) {
					strCollection = null;
				}
				else {
					if(SourceType.JSON.equals(this.sourceType)) {
						final JSONObject curJSONObject = this.getCurrentJSONObject();
						strCollection = super.getStringCollection(valueConf, curJSONObject);
					}
					else if(SourceType.HTML.equals(this.sourceType)) {
						final Element curElement = this.getCurrentElement();
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
	
	private boolean isCurrentObjectAvailable() {
		final int MIN_INDEX = 0;
		if(SourceType.JSON.equals(this.sourceType)) {
			return (this.jsonObjectList != null && this.objListIndex >= MIN_INDEX && this.objListIndex < this.jsonObjectList.size());
		}
		else if(SourceType.HTML.equals(this.sourceType)) {
			return (this.elementList != null && this.objListIndex >= MIN_INDEX && this.objListIndex < this.elementList.size());
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	private JSONObject getCurrentJSONObject() {
		if(SourceType.JSON.equals(this.sourceType) && isCurrentObjectAvailable()) {
			return this.jsonObjectList.get(this.objListIndex);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	private Element getCurrentElement() {
		if(SourceType.HTML.equals(this.sourceType) && isCurrentObjectAvailable()) {
			return this.elementList.get(this.objListIndex);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
}
