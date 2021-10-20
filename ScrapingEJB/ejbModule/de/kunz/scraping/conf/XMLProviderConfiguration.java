package de.kunz.scraping.conf;

import java.beans.*;
import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLProviderConfiguration implements IProviderConfiguration, PropertyChangeListener{

	@XmlAttribute(name="profileName")
	private String profileName;
	
	@XmlAttribute(name="sourceType")
	private SourceType sourceType;
	
	@XmlElement(name="ProxyConfiguration")
	private XMLProxyConfiguration proxyConfiguration;
	
	@XmlAttribute(name="connectionLimit")
	private int connectionLimit;
	
	@XmlAttribute(name="minZipCodeDistance")
	private int minZipCodeDistance;
	
	@XmlAttribute(name="htmlAdapterClassName")
	private String htmlAdapterClassName;
	
	@XmlAttribute(name="jsonAdaptClassName")
	private String jsonAdapterClassName;
	 
	@XmlElementWrapper(name="BaseKeySequence")
	@XmlElement(name="KeyFragment")
	private List<String> baseKeySeq = new LinkedList<>();

	@XmlElementWrapper(name="ValueContextConfigurations")
	@XmlElement(name="JSONValueContextConfiguration")
	private List<XMLValueContextConfiguration> valueContextConfigurations = new LinkedList<>();
	
	@XmlElementWrapper(name="ValueConfigurations")
	@XmlElement(name="ValueConfiguration")
	private List<XMLValueConfiguration> valueConfigurations = new LinkedList<>();
	 
	@XmlTransient 
	private final PropertyChangeSupport changeSupport;
	
	public XMLProviderConfiguration() {
		super();
		this.changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	@XmlTransient
	public String getProfileName() {
		return this.profileName;
	}
	 
	@Override
	public void setProfileName(String profileName) {
		final String oldProfileName = this.profileName;
		this.profileName = profileName;
		this.changeSupport.firePropertyChange("profileName", oldProfileName, profileName);
	}
	
	@Override
	@XmlTransient
	public SourceType getSourceType() {
		return this.sourceType;
	}
	
	@Override
	public void setSourceType(SourceType sourceType) {
		final SourceType oldSourceType = this.sourceType;
		this.sourceType = sourceType;
		this.changeSupport.firePropertyChange("sourceType", oldSourceType, sourceType);
	}
	
	@Override
	public IProxyConfiguration getProxyConfiguration() {
		if(this.proxyConfiguration == null) {
			this.proxyConfiguration = new XMLProxyConfiguration();
			this.proxyConfiguration.addPropertyChangeListener(this);
			this.changeSupport.firePropertyChange("proxyConfiguration", null, this.proxyConfiguration );
		}
		return this.proxyConfiguration;
	}
	
	

	@Override
	@XmlTransient
	public int getConnectionLimit() {
		return this.connectionLimit;
	}
	

	@Override
	public void setConnectionLimit(int connectionLimit) {
		final int oldConnectionLimit = this.connectionLimit;
		this.connectionLimit = connectionLimit;
		this.changeSupport.firePropertyChange("connectionLimit", oldConnectionLimit, connectionLimit);
	}
	
	@Override
	@XmlTransient
	public int getMinZipCodeDistance() {
		return minZipCodeDistance;
	}

	public void setMinZipCodeDistance(int minZipCodeDistance) {
		final int oldMinZipCodeDistance = this.minZipCodeDistance;
		this.minZipCodeDistance = minZipCodeDistance;
		this.changeSupport.firePropertyChange("minZipCodeDistance", oldMinZipCodeDistance, minZipCodeDistance);
	} 

	@Override
	@XmlTransient
	public String getHTMLAdapterClassName() {
		return htmlAdapterClassName;
	}

	@Override
	public void setHTMLAdapterClassName(String className) {
		final String oldHtmlAdapterClassName = this.htmlAdapterClassName;
		this.htmlAdapterClassName = className;
		this.changeSupport.firePropertyChange("htmlAdapterClassName", oldHtmlAdapterClassName, className);
	}

	@Override
	@XmlTransient
	public String getJSONAdapterClassName() {
		return jsonAdapterClassName;
	}

	@Override
	public void setJSONAdapterClassName(String className) {
		final String oldJSONAdapterClassName = this.jsonAdapterClassName;
		this.jsonAdapterClassName = className;
		this.changeSupport.firePropertyChange("jsonAdapterClassName", oldJSONAdapterClassName, className);
	}

	@Override
	@XmlTransient
	public List<String> getBaseKeySequence() {
		return this.baseKeySeq;
	}

	@Override
	public void setBaseKeySequence(List<String> baseKeySequence) {
		this.baseKeySeq.clear();
		this.baseKeySeq.addAll(baseKeySequence);
		this.changeSupport.firePropertyChange("baseKeySeq", this.baseKeySeq, null);
	}

	@Override
	public IValueContextConfiguration getValueContextConfiguration(String valueContextName) {
		XMLValueContextConfiguration jsonValueContextConf = null;
		if(valueContextName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLValueContextConfiguration curValueContextConf : this.valueContextConfigurations) {
				if(valueContextName.equals(curValueContextConf.getValueContextName())) {
					jsonValueContextConf = curValueContextConf;
				}
			}
		}		
		return jsonValueContextConf;
	}

	@Override
	public IValueContextConfiguration addValueContextConfiguration(String valueContextName) {
		if(valueContextName == null) {
			throw new NullPointerException();
		}
		else {
			final XMLValueContextConfiguration jsonValueContextConf = new XMLValueContextConfiguration();
			jsonValueContextConf.setValueContextName(valueContextName);
			jsonValueContextConf.addPropertyChangeListener(this);
			jsonValueContextConf.init();
			this.valueContextConfigurations.add(jsonValueContextConf);
			this.changeSupport.firePropertyChange("jsonValueContextConfigurations", this.valueContextConfigurations, null);
			return jsonValueContextConf;
		}
	}

	@Override
	public List<IValueContextConfiguration> getValueContextConfigurations() {
		List<IValueContextConfiguration> valueContextConfigurations = new LinkedList<>();
		valueContextConfigurations.addAll(valueContextConfigurations);
		return valueContextConfigurations;
	}

	
	@Override
	public XMLValueConfiguration getValueConfiguration(String valueName) {
		XMLValueConfiguration jsonValueConf = null;
		if(valueName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLValueConfiguration curJsonValue : this.valueConfigurations) {
				if(valueName.equals(curJsonValue.getValueName())) {
					jsonValueConf = curJsonValue;
					break;
				}
			}
		}
		return jsonValueConf;
	}

	
	@Override
	public XMLValueConfiguration addValueConfiguration(String valueName) {
		if(valueName == null) {
			throw new NullPointerException();
		}
		else {
			final XMLValueConfiguration jsonValueConf = new XMLValueConfiguration();
			jsonValueConf.setValueName(valueName);
			jsonValueConf.addPropertyChangeListener(this);
			jsonValueConf.init();
			this.valueConfigurations.add(jsonValueConf);
			this.changeSupport.firePropertyChange("jsonValueConfigurations", this.valueConfigurations, null);
			return jsonValueConf;
		}
	}

	@Override
	public List<IValueConfiguration> getValueConfigurations() {
		return new LinkedList<>(this.valueConfigurations);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}
	
	void init() {
		for(XMLValueContextConfiguration curJsonValueContextConf : this.valueContextConfigurations) {
			curJsonValueContextConf.addPropertyChangeListener(this);
			curJsonValueContextConf.init();
		}
		for(XMLValueConfiguration curJsonValueConf : this.valueConfigurations) {
			curJsonValueConf.addPropertyChangeListener(this);
			curJsonValueConf.init();
		}
		if(this.proxyConfiguration != null) {
			this.proxyConfiguration.addPropertyChangeListener(this);
		}
	}
}  
