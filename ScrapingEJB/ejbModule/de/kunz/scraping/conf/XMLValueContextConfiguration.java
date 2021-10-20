package de.kunz.scraping.conf;

import java.beans.*;
import java.util.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLValueContextConfiguration implements IValueContextConfiguration, PropertyChangeListener {

	@XmlAttribute(name="valueContextName")
	private String valueContextName;
	
	@XmlAttribute(name="sourceType")
	private SourceType sourceType;
	
	@XmlElementWrapper(name="ValueContextKeySequence") 
	@XmlElement(name="KeyFragment")
	private final List<String> valueContexKeySequence;
	
	@XmlElementWrapper(name="ValueConfigurations")
	@XmlElement(name="ValueConfiguration")
	private final List<XMLValueConfiguration> jsonValueConfigurations;
	
	@XmlTransient
	private final PropertyChangeSupport changeSupport;
	
	public XMLValueContextConfiguration() {
		this.jsonValueConfigurations = new LinkedList<>();
		this.valueContexKeySequence = new LinkedList<>();
		this.changeSupport = new PropertyChangeSupport(this);
	}
	
	@Override
	@XmlTransient
	public String getValueContextName() {
		return this.valueContextName;
	}

	@Override
	public void setValueContextName(String valueContextName) {
		final String oldValueContextName = this.valueContextName;
		this.valueContextName = valueContextName;
		this.changeSupport.firePropertyChange("valueContextName", oldValueContextName, valueContextName);
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
	@XmlTransient
	public List<String> getValueContextKeySequence() {
		return new LinkedList<>(this.valueContexKeySequence);
	}

	@Override
	public void setValueContextKeySequence(List<String> valueContextKeySeq) {
		this.valueContexKeySequence.clear();
		this.valueContexKeySequence.addAll(valueContextKeySeq);
		this.changeSupport.firePropertyChange("valueContexKeySequence", this.valueContexKeySequence, null);
	}
	
	@Override
	public XMLValueConfiguration getValueConfiguration(String valueName) {
		XMLValueConfiguration jsonValueConf = null;
		if(valueName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLValueConfiguration curJsonValue : this.jsonValueConfigurations) {
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
			this.jsonValueConfigurations.add(jsonValueConf);
			this.changeSupport.firePropertyChange("jsonValueConfigurations", this.jsonValueConfigurations, null);
			return jsonValueConf;
		}
	} 

	@Override
	public List<IValueConfiguration> getValueConfigurations() {
		return new LinkedList<>(this.jsonValueConfigurations);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}

	void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}
	
	void init() {
		for(XMLValueConfiguration jsonValueConf : this.jsonValueConfigurations) {
			jsonValueConf.addPropertyChangeListener(this);
			jsonValueConf.init();
		}
	}
} 
