package de.kunz.scraping.conf;

import java.beans.*;
import java.util.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
final class XMLFilterChainConfiguration implements IFilterChainConfiguration, PropertyChangeListener {

	@XmlAttribute(name="filterChainName")
	private String filterChainName;
	
	@XmlAttribute(name="isEnabled")
	private boolean isEnabled;

	@XmlElement(name="FilterConfiguration")
	private final List<XMLFilterConfiguration> filterConfigurationList;
		
	@XmlTransient
	private final PropertyChangeSupport changeSupport;
	
	public XMLFilterChainConfiguration() {
		super();
		this.filterConfigurationList = new LinkedList<>();
		this.changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	@XmlTransient
	public String getFilterChainName() {
		return filterChainName;
	}

	@Override
	public void setFilterChainName(String name) {
		final String oldFilterChainName = this.filterChainName;
		this.filterChainName = name;
		this.changeSupport.firePropertyChange("filterChainName", oldFilterChainName, name);
	}

	@Override
	public List<IFilterConfiguration> getFilterConfigurationsList() {
		return new LinkedList<>(this.filterConfigurationList);
	}

	@Override
	public IFilterConfiguration addFilterConfiguration(String filterName) {
		if(filterName == null) {
			throw new NullPointerException();
		}
		else if(!isFilterNameAvailable(filterName)) {
			throw new IllegalStateException();
		}
		else {
			final XMLFilterConfiguration filterConfiguration = new XMLFilterConfiguration();
			filterConfiguration.setName(filterName);
			filterConfiguration.addPropertyChangeListener(this);
			filterConfiguration.init();
			this.filterConfigurationList.add(filterConfiguration);
			this.changeSupport.firePropertyChange("filterConfigurationList", this.filterConfigurationList, null);
			return filterConfiguration;
		}
	}

	@Override
	public void removeFilter(String filterName) {
		if(filterName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLFilterConfiguration filterConfiguration : this.filterConfigurationList) {
				if(filterName.equals(filterConfiguration.getName())) {
					this.filterConfigurationList.remove(filterConfiguration);
					this.changeSupport.firePropertyChange("filterConfigurationList", this.filterConfigurationList, null);
					break;
				}
			}
		}
	}

	@Override
	public boolean isFilterNameAvailable(String filterName) {
		boolean isFilterNameAvailable = true;
		if(filterName == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLFilterConfiguration filterConfiguration : this.filterConfigurationList) {
				if(filterName.equals(filterConfiguration.getName())) {
					isFilterNameAvailable = false;
					break;
				}
			}
		}
		return isFilterNameAvailable;
	}

	@Override
	@XmlTransient
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void enable() {
		final boolean oldEnabled = this.isEnabled;
		this.isEnabled = true;
		this.changeSupport.firePropertyChange("isEnabled", oldEnabled, true);
	}

	@Override
	public void disable() {
		final boolean oldEnabled = this.isEnabled;
		this.isEnabled = false;
		this.changeSupport.firePropertyChange("isEnabled", oldEnabled, false);
	}
	 
	void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}

	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		changeSupport.firePropertyChange(evt);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filterChainName == null) ? 0 : filterChainName.hashCode());
		result = prime * result + ((filterConfigurationList == null) ? 0 : filterConfigurationList.hashCode());
		result = prime * result + (isEnabled ? 1231 : 1237);
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
		XMLFilterChainConfiguration other = (XMLFilterChainConfiguration) obj;
		if (filterChainName == null) {
			if (other.filterChainName != null)
				return false;
		} else if (!filterChainName.equals(other.filterChainName))
			return false;
		if (filterConfigurationList == null) {
			if (other.filterConfigurationList != null)
				return false;
		} else if (!filterConfigurationList.equals(other.filterConfigurationList))
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		return true;
	}
	
	void init() {
		for(XMLFilterConfiguration filterConfiguration : this.filterConfigurationList) {
			filterConfiguration.addPropertyChangeListener(this);
			filterConfiguration.init();
		}
	}
	
}
