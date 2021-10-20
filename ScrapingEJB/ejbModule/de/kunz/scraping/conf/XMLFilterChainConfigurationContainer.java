package de.kunz.scraping.conf;

import java.beans.*;
import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.PROPERTY)
final class XMLFilterChainConfigurationContainer implements IFilterChainConfigurationContainer, PropertyChangeListener {

	@XmlElement(name="FilterChainConfiguration")
	private final List<XMLFilterChainConfiguration> filterChainConfigurationList = new LinkedList<>();
	
	@XmlTransient
	private final PropertyChangeSupport changeSupport;
	
	public XMLFilterChainConfigurationContainer() {
		super();
		changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public IFilterChainConfiguration getEnabledFilterChainConfiguration() {
		IFilterChainConfiguration filterChainConfiguration = null;
		for(IFilterChainConfiguration curFilterChainConfiguration : this.filterChainConfigurationList) {
			if(curFilterChainConfiguration.isEnabled()) {
				filterChainConfiguration = curFilterChainConfiguration;
				break;
			}
		}
		return filterChainConfiguration;
	}

	@Override
	public List<IFilterChainConfiguration> getFilterChainConfigurationsList() {
		return new LinkedList<>(this.filterChainConfigurationList);
	}
	
	@Override
	public IFilterChainConfiguration createFilterChain(String filterChainName) {
		if(filterChainName == null) {
			throw new NullPointerException();
		}
		else if(!isFilterChainNameAvailable(filterChainName)) {
			throw new IllegalArgumentException();
		}
		else {
			final XMLFilterChainConfiguration filterChainConfiguration = new XMLFilterChainConfiguration();
			filterChainConfiguration.setFilterChainName(filterChainName);
			filterChainConfiguration.addPropertyChangeListener(this);
			filterChainConfiguration.init();
			this.filterChainConfigurationList.add(filterChainConfiguration);
			this.changeSupport.firePropertyChange("filterChainConfigurationList", this.filterChainConfigurationList, null);
			return filterChainConfiguration;
		}
	}
 
	@Override
	public void removeFilterChain(String filterChainName) {
		if(filterChainName == null) {
			throw new NullPointerException();
		}
		else {
			for(IFilterChainConfiguration curFilterChainConfiguration : this.filterChainConfigurationList) {
				if(filterChainName.equals(curFilterChainConfiguration.getFilterChainName())) {
					this.filterChainConfigurationList.remove(curFilterChainConfiguration);
					this.changeSupport.firePropertyChange("filterChainConfigurationList", this.filterChainConfigurationList, null);
					break;
				}
			}
		}
	}

	@Override
	public boolean isFilterChainNameAvailable(String filterChainName) {
		boolean isFilterChainNameAvailable = true;
		if(filterChainName == null) {
			isFilterChainNameAvailable = false;
		}
		else {
			for(IFilterChainConfiguration curFilterChainConfiguration : this.filterChainConfigurationList) {
				if(filterChainName.equals(curFilterChainConfiguration.getFilterChainName())) {
					isFilterChainNameAvailable = false;
				}
			}
		}
		return isFilterChainNameAvailable;
	}

	void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
		if(src instanceof XMLFilterChainConfiguration) {
			final String PROPERTY_NAME_ENABLED = "isEnabled";
			if(PROPERTY_NAME_ENABLED.equals(evt.getPropertyName()) 
					&& (boolean)evt.getNewValue()) {
				XMLFilterChainConfiguration modifiedFilterChainConf = (XMLFilterChainConfiguration) src;
				onFilterChainEnabled(modifiedFilterChainConf.getFilterChainName());
			}			
		}
		this.changeSupport.firePropertyChange(evt);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filterChainConfigurationList == null) ? 0 : filterChainConfigurationList.hashCode());
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
		XMLFilterChainConfigurationContainer other = (XMLFilterChainConfigurationContainer) obj;
		if (filterChainConfigurationList == null) {
			if (other.filterChainConfigurationList != null)
				return false;
		} else if (!filterChainConfigurationList.equals(other.filterChainConfigurationList))
			return false;
		return true;
	}
	
	void init() {
		for(XMLFilterChainConfiguration filterChainConfiguration : this.filterChainConfigurationList) {
			filterChainConfiguration.addPropertyChangeListener(this);
			filterChainConfiguration.init(); 
		}
	}
	
	private void onFilterChainEnabled(String filterChainName) {
		if(filterChainName != null) {
			for(XMLFilterChainConfiguration filterChainConf : this.filterChainConfigurationList) {
				if(!filterChainName.equals(filterChainConf.getFilterChainName())) {
					filterChainConf.disable();    
				}
			}
		}
	}
} 
