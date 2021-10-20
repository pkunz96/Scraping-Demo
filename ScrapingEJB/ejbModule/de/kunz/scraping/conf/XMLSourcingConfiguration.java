package de.kunz.scraping.conf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlAccessorType(XmlAccessType.PROPERTY)
final class XMLSourcingConfiguration implements ISourcingConfiguration, PropertyChangeListener {
	
	@XmlElement(name="ProviderConfigurationContainer")
	private final XMLProviderConfigurationContainer providerConfigurationContainer;
	
	
	@XmlElement(name="FilterChainConfigurationContainer")
	private final XMLFilterChainConfigurationContainer filterChainConfContainer;
	
	@XmlTransient
	private final PropertyChangeSupport changeSupport; 
	
	public XMLSourcingConfiguration() {
		this.changeSupport = new PropertyChangeSupport(this);
		this.providerConfigurationContainer = new XMLProviderConfigurationContainer();
		this.filterChainConfContainer = new XMLFilterChainConfigurationContainer();
	}

	@Override
	public XMLProviderConfiguration getProviderConfiguration(String profileName) {
		return this.providerConfigurationContainer.getProviderConfiguration(profileName);
	}

	@Override
	public XMLProviderConfiguration createProviderConfiguration(String profileName) {
		return this.providerConfigurationContainer.createProviderConfiguration(profileName);
	}

	@Override
	public void removeProviderConfiguration(String profileName) {
		this.providerConfigurationContainer.removeProviderConfiguration(profileName);
	}

	@Override
	public boolean isProviderConfigurationAvailable(String profileName) {
		return this.isProviderConfigurationAvailable(profileName);
	}

	@Override
	public IFilterChainConfiguration getEnabledFilterChainConfiguration() {
		return this.filterChainConfContainer.getEnabledFilterChainConfiguration();
	}

	@Override
	public List<IFilterChainConfiguration> getFilterChainConfigurationsList() {
		return this.filterChainConfContainer.getFilterChainConfigurationsList();
	}

	@Override
	public IFilterChainConfiguration createFilterChain(String filterChainName) {
		return this.filterChainConfContainer.createFilterChain(filterChainName);
	}

	@Override
	public void removeFilterChain(String filterChainName) {
		this.filterChainConfContainer.removeFilterChain(filterChainName);
	}

	@Override
	public boolean isFilterChainNameAvailable(String filterChainName) {
		return this.filterChainConfContainer.isFilterChainNameAvailable(filterChainName);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}

	void addListener(PropertyChangeListener listener) {		
		this.changeSupport.addPropertyChangeListener(listener);
	}  
	
	void init() {
		this.providerConfigurationContainer.addPropertyChangeListener(this);
		this.filterChainConfContainer.addPropertyChangeListener(this);
		this.filterChainConfContainer.init();
		this.providerConfigurationContainer.init();
	}
}
