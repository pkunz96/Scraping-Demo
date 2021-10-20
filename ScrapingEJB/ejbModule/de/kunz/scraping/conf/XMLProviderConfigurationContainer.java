package de.kunz.scraping.conf;

import java.util.*;
import java.beans.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLProviderConfigurationContainer implements IProviderConfigurationContainer, PropertyChangeListener  {

	@XmlElement(name="ProviderConfiguration")
	private List<XMLProviderConfiguration> providerConfigurationList = new LinkedList<>();
	
	@XmlTransient 
	private final PropertyChangeSupport changeSupport; 
	
	public XMLProviderConfigurationContainer() {
		super();
		this.changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public XMLProviderConfiguration getProviderConfiguration(String profileName) {
		XMLProviderConfiguration providerConf = null;
		if(profileName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLProviderConfiguration curProviderConf : this.providerConfigurationList) {
				if(profileName.equals(curProviderConf.getProfileName())) {
					providerConf = curProviderConf;
					break;
				}
			}
		}
		return providerConf;
	}

	@Override
	public XMLProviderConfiguration createProviderConfiguration(String profileName) {
		if(profileName == null) {
			throw new NullPointerException();
		}
		else if(!isProviderConfigurationAvailable(profileName)) {
			throw new IllegalArgumentException();
		}
		else {
			final XMLProviderConfiguration jsonProviderConf = new XMLProviderConfiguration();
			jsonProviderConf.setProfileName(profileName);
			jsonProviderConf.addPropertyChangeListener(this);
			jsonProviderConf.init();
			this.providerConfigurationList.add(jsonProviderConf);
			this.changeSupport.firePropertyChange("jsonProviderConfigurationList", this.providerConfigurationList, null);
			return jsonProviderConf;
			
		}
	}

	@Override
	public void removeProviderConfiguration(String profileName) {
		if(profileName == null) {
			throw new NullPointerException(); 
		}
		else {
			for(XMLProviderConfiguration curJsonProviderConf : this.providerConfigurationList) {
				if(profileName.equals(curJsonProviderConf.getProfileName())) {
					this.providerConfigurationList.remove(curJsonProviderConf);
					this.changeSupport.firePropertyChange("jsonProviderConfigurationList", this.providerConfigurationList, null);
					break;
				}
			}
		}
	}

	@Override
	public boolean isProviderConfigurationAvailable(String profileName) {
		if(profileName == null) {
			throw new NullPointerException(); 
		}
		else {
			for(XMLProviderConfiguration curJsonProviderConf : this.providerConfigurationList) {
				if(profileName.equals(curJsonProviderConf.getProfileName())) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}
 
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}
	
	void init() {
		for(XMLProviderConfiguration curJsonProviderConf : this.providerConfigurationList) {
			curJsonProviderConf.addPropertyChangeListener(this);
			curJsonProviderConf.init(); 
		}
	}
}
