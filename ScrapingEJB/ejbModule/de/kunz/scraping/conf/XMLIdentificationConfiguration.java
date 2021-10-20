package de.kunz.scraping.conf;

import java.beans.*;
import java.util.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLIdentificationConfiguration implements IIdentificationConfiguration, PropertyChangeListener {
	
	@XmlElement(name="FilterChainConfigurationContainer")
	private final XMLFilterChainConfigurationContainer filterChainConfContainer;
	
	@XmlTransient
	private final PropertyChangeSupport changeSupport; 
	
	public XMLIdentificationConfiguration() {
		this.filterChainConfContainer = new XMLFilterChainConfigurationContainer();
		this.changeSupport = new PropertyChangeSupport(this);
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
		return this.isFilterChainNameAvailable(filterChainName);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}

	void addListener(PropertyChangeListener listener) {		
		this.changeSupport.addPropertyChangeListener(listener);
	}  

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1; 
		result = prime * result + ((filterChainConfContainer == null) ? 0 : filterChainConfContainer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XMLIdentificationConfiguration other = (XMLIdentificationConfiguration) obj;
		if (filterChainConfContainer == null) {
			if (other.filterChainConfContainer != null)
				return false;
		} else if (!filterChainConfContainer.equals(other.filterChainConfContainer))
			return false;
		return true;
	}
	
	void init() {  
		this.filterChainConfContainer.addPropertyChangeListener(this);
		this.filterChainConfContainer.init();
	}
}
