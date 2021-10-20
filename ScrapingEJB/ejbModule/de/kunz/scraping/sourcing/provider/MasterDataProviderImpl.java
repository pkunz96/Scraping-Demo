package de.kunz.scraping.sourcing.provider;

import java.util.*;
import java.beans.*;

import org.json.simple.*;
import org.jsoup.nodes.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.sourcing.provider.adapter.*;
import de.kunz.scraping.util.*;

final class MasterDataProviderImpl implements IMasterDataProvider {
	
	private static State CONSTRUCTED = new State("CONSTRUCTED", 0);
	
	private static State INITIALIZING = new State("INITIALIZING", 1);
	
	private static State PROVIDER_CONFIGURATION_ADDED = new State("PROVIDER_CONFIGURATION_ADDED", 2);
	
	private static State ADAPTER_ADDED = new State("ADAPTER_ADDED", 3);
	
	private static State DATA_ADDED = new State("DATA_ADDED", 4);
	
	private static State PROVIDER_ADDED = new State("PROVIDER_ADDED", 5);

	private static State INITIALIZED = new State("INITIALIZED", 6);

	private static List<State> STATE_LIST = new LinkedList<>();
	
	static {
		STATE_LIST.add(CONSTRUCTED);
		STATE_LIST.add(INITIALIZING);
		STATE_LIST.add(PROVIDER_CONFIGURATION_ADDED);
		STATE_LIST.add(ADAPTER_ADDED);
		STATE_LIST.add(DATA_ADDED);
		STATE_LIST.add(PROVIDER_ADDED); 
		STATE_LIST.add(INITIALIZED);
	}
	
	private DefaultPersonProvider personProvider;
	
	private DefaultBusinessProvider businessProvider;
	
	private DefaultBusinessRelationProvider businessRelationProvider;
	
	private DefaultPhoneNumberProvider phoneNumberProvider;
	
	private DefaultMobilePhoneNumberProvider mobilePhoneNumberProvider;
	
	private DefaulEmailAddressProvider emailAdressProvider;
	
	private DefaultPropertyProvider properyProvider;
	
	private DefaultConcessionProvider concessionProvider;
	
	private IProviderConfiguration providerConfiguration;
	
	private IJSONAdapter jsonAdapter;
	
	private IHTMLAdapter htmlAdapter;
	
	private List<JSONObject> jsonObjectList;
	
	private List<Element> htmlObjectList;
	
	private int objListIndex;
	
	public final LinearStateMaschine stateMachine;
	
	private final PropertyChangeSupport changeSupport;
	
	public MasterDataProviderImpl() {
		this.stateMachine = new LinearStateMaschine(STATE_LIST, CONSTRUCTED);
		this.changeSupport = new PropertyChangeSupport(this);
		this.objListIndex = -1;
	}

	@Override
	public boolean hasNext() {
		this.stateMachine.assureIsMin(INITIALIZED);
		if(this.jsonObjectList != null) {
			return ((this.objListIndex + 1) < this.jsonObjectList.size());
		}
		else {
			return ((this.objListIndex + 1) < this.htmlObjectList.size());
		}
	}

	@Override
	public Void next() {
		this.stateMachine.assureIsMin(INITIALIZED);
		if(hasNext()) {
			int objListOldIndex = this.objListIndex;
			this.objListIndex++;
			this.changeSupport.firePropertyChange(new PropertyChangeEvent(this, "objListIndex", objListOldIndex, this.objListIndex));
		}
		else {
			throw new NoSuchElementException();
		}
		return null;
	}

	@Override
	public IPersonProvider getPersonProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.personProvider;
	}

	@Override
	public IBusinessProvider getBusinessProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.businessProvider;
	}

	@Override
	public IBusinessRelationProvider getBusinessRelationProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.businessRelationProvider;
	}

	@Override
	public IPhoneNumberProvider getPhoneNumberProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.phoneNumberProvider;
	}

	@Override
	public IMobilePhoneNumberProvider getMobilePhoneNumberProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.mobilePhoneNumberProvider;
	}

	@Override
	public IEmailAdressProvider getEmailAddressProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.emailAdressProvider;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.properyProvider;
	}

	@Override
	public IConcessionProvider getConcessionProvider() {
		this.stateMachine.assureIsMin(INITIALIZED);
		return this.concessionProvider;
	}

	IProviderConfiguration getProviderConfiguration() {
		this.stateMachine.assureIsMin(PROVIDER_CONFIGURATION_ADDED);
		return this.providerConfiguration;
	}
	
	JSONObject getCurrentJSONObject() {
		JSONObject curJSONObj = null;
		this.stateMachine.assureIsEqual(INITIALIZED);
		final int MIN_INDEX = 0;
		if(this.jsonObjectList != null) {
			final int MAX_INDEX = this.jsonObjectList.size() - 1;
			if((this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX)) {
				curJSONObj = this.jsonObjectList.get(this.objListIndex);
			}
		}
		else if(this.jsonAdapter != null && this.htmlObjectList != null) {
			final int MAX_INDEX = this.htmlObjectList.size() - 1;
			if((this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX)) {
				curJSONObj = this.jsonAdapter.convert(this.htmlObjectList.get(this.objListIndex));
			}
		}
		return curJSONObj;
	}
	
	Element getCurrentElement() {
		Element curElement = null;
		this.stateMachine.assureIsEqual(INITIALIZED);
		final int MIN_INDEX = 0;
		if(this.htmlObjectList != null) {
			final int MAX_INDEX = this.htmlObjectList.size() - 1;
			if((this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX)) {
				curElement = this.htmlObjectList.get(this.objListIndex);
			}
		}
		else if(this.htmlAdapter != null && this.jsonObjectList != null) {
			final int MAX_INDEX = this.jsonObjectList.size() - 1;
			if((this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX)) {
				curElement = this.htmlAdapter.convert(this.jsonObjectList.get(this.objListIndex));
			} 
		}
		return curElement;
	}
	
	//TODO
	
	
	boolean isCurrentObjectAvailable() {
		
		boolean isAvailable = false;
		this.stateMachine.assureIsEqual(INITIALIZED);
		final int MIN_INDEX = 0;
		if(this.jsonObjectList != null) {
			final int MAX_INDEX = this.jsonObjectList.size() - 1;
			isAvailable = (this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX);
		}	
		else if(this.htmlObjectList != null) {
			final int MAX_INDEX = this.htmlObjectList.size() - 1;
			isAvailable = (this.objListIndex >= MIN_INDEX) && (this.objListIndex <= MAX_INDEX);
		}
		return isAvailable;
	}
	 
	
	
	
	
	void startInit() {
		this.stateMachine.assureIsEqual(CONSTRUCTED);
		this.stateMachine.setState(INITIALIZING);
	}
	
	void setProviderConfiguration(IProviderConfiguration providerConfiguration) {
		if(providerConfiguration == null) {
			throw new NullPointerException();
		}
		this.stateMachine.assureIsEqual(INITIALIZING);
		this.providerConfiguration = providerConfiguration;
		this.stateMachine.setState(PROVIDER_CONFIGURATION_ADDED);
	}
	
	void setAdapter(IJSONAdapter jsonAdapter) {
		this.stateMachine.assureIsMin(PROVIDER_CONFIGURATION_ADDED);
		this.jsonAdapter = jsonAdapter;
		this.stateMachine.setState(ADAPTER_ADDED);
	}
	
	void setAdapter(IHTMLAdapter htmlAdapter) {
		this.stateMachine.assureIsEqual(PROVIDER_CONFIGURATION_ADDED);
		this.htmlAdapter = htmlAdapter;
		this.stateMachine.setState(ADAPTER_ADDED);
	}
	
	void setJSONData(List<JSONObject> jsonObjectList) {
		if(jsonObjectList == null) {
			throw new NullPointerException();
		}
		this.stateMachine.assureIsMin(PROVIDER_CONFIGURATION_ADDED);
		final SourceType sourceType = this.providerConfiguration.getSourceType();
		if(SourceType.JSON.equals(sourceType)) {
			this.jsonObjectList = jsonObjectList;
			this.htmlObjectList = null;
		}
		this.stateMachine.setState(DATA_ADDED);
	}
	
	void setHTMLData(List<Element> htmlObjectList) {
		if(htmlObjectList == null) {
			throw new NullPointerException();
		}
		this.stateMachine.assureIsMin(PROVIDER_CONFIGURATION_ADDED);
		final SourceType sourceType = this.providerConfiguration.getSourceType();
		if(SourceType.HTML.equals(sourceType)) {
			this.jsonObjectList = null;
			this.htmlObjectList = htmlObjectList;
		}
		this.stateMachine.setState(DATA_ADDED);
	}
	
	void setPersonProvider(DefaultPersonProvider personProvider) {
		if(!isSupported(businessRelationProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.personProvider = personProvider;
		this.changeSupport.addPropertyChangeListener(personProvider);
	}
	
	void setBusinessProvider(DefaultBusinessProvider businessProvider) {
		if(!isSupported(businessRelationProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.businessProvider = businessProvider;
		this.changeSupport.addPropertyChangeListener(businessProvider);
	}
	
	void setBusinessRelationProvider(DefaultBusinessRelationProvider businessRelationProvider) {
		if(!isSupported(businessRelationProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.businessRelationProvider = businessRelationProvider;
		this.changeSupport.addPropertyChangeListener(businessRelationProvider);
	}
	
	void setPhoneNumberProvider(DefaultPhoneNumberProvider phoneNumberProvider) {
		if(!isSupported(phoneNumberProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.phoneNumberProvider = phoneNumberProvider;
		this.changeSupport.addPropertyChangeListener(phoneNumberProvider);
	}
	
	void setMobilePhoneNumberProvider(DefaultMobilePhoneNumberProvider mobilePhoneNumberProvider) {
		if(!isSupported(mobilePhoneNumberProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.mobilePhoneNumberProvider = mobilePhoneNumberProvider;
		this.changeSupport.addPropertyChangeListener(mobilePhoneNumberProvider);
	}
	 
	void setEmailAddressProvider(DefaulEmailAddressProvider emailAddressProvider) {
		if(!isSupported(emailAddressProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.emailAdressProvider = emailAddressProvider;
		this.changeSupport.addPropertyChangeListener(emailAddressProvider);
	}
	
	void setPropertyProvider(DefaultPropertyProvider propertyProvider) {
		if(!isSupported(propertyProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.properyProvider = propertyProvider;
		this.changeSupport.addPropertyChangeListener(propertyProvider);
	}
	
	void setConcessionProvider(DefaultConcessionProvider concessionProvider) {
		if(!isSupported(concessionProvider)) {
			throw new IllegalArgumentException();
		}
		this.stateMachine.assureIsMin(DATA_ADDED);
		this.stateMachine.setState(PROVIDER_ADDED);
		this.concessionProvider = concessionProvider;
		this.changeSupport.addPropertyChangeListener(concessionProvider);
	}
	
	void finalizeInit() {
		this.stateMachine.assureIsEqual(PROVIDER_ADDED);
		this.stateMachine.setState(INITIALIZED);
	}
	
	private boolean isSupported(AbstractProvider provider) {
		boolean supported = true;
		if(provider != null) {
			if(provider.isJSONSupportRequired() && (this.jsonObjectList == null && this.jsonAdapter == null)) {
				supported = false;
			}
			else if(provider.isHTMLSupportRequired() && (this.htmlObjectList == null && this.htmlAdapter == null)) {
				supported = false;
			}
		} 
		return supported;
	}
	
}
