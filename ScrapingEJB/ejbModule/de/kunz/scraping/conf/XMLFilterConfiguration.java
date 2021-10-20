package de.kunz.scraping.conf;

import java.beans.*;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
final class XMLFilterConfiguration implements IFilterConfiguration, PropertyChangeListener {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="classname")
	private String classname;
	
	@XmlAttribute(name="priority")
	private int priorty;
	
	@XmlAttribute(name="isEnabled")
	private boolean isEnabled;
	
	@XmlElementWrapper(name="FilterParamters")
	@XmlElement(name="FilterParamter")
	private List<XMLFilterParameter> filterParamterList;
	
	@XmlTransient
	private PropertyChangeSupport changeSupport; 
	
	public XMLFilterConfiguration() {
		super();
		this.filterParamterList = new LinkedList<>();
		this.changeSupport = new PropertyChangeSupport(this);
	}
	
	@Override
	@XmlTransient
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		final String oldName = this.name;
		this.name = name;
		this.changeSupport.firePropertyChange("name", oldName, name);
	}

	@Override
	@XmlTransient
	public String getClassname() {
		return classname;
	}

	@Override
	public void setClassname(String classname) {
		final String oldClassname = this.classname;
		this.classname = classname;
		this.changeSupport.firePropertyChange("classname", oldClassname, classname);
	}

	
	@Override
	@XmlTransient
	public int getPriority() {
		return priorty;
	}

	@Override
	public void setPrioty(int priority) {
		final int oldPriority = this.priorty;
		this.priorty = priority;
		this.changeSupport.firePropertyChange("priorty", oldPriority, priority);
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
		this.changeSupport.firePropertyChange("isEnabled", oldEnabled, this.isEnabled);
	}

	@Override
	public void disbale() {
		final boolean oldEnabled = this.isEnabled;
		this.isEnabled = false;
		this.changeSupport.firePropertyChange("isEnabled", oldEnabled, this.isEnabled);
	}
	
	void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classname == null) ? 0 : classname.hashCode());
		result = prime * result + (isEnabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + priorty;
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
		XMLFilterConfiguration other = (XMLFilterConfiguration) obj;
		if (classname == null) {
			if (other.classname != null)
				return false;
		} else if (!classname.equals(other.classname))
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priorty != other.priorty)
			return false;
		return true;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.changeSupport.firePropertyChange(evt);
	}
	
	void init() {
		for(XMLFilterParameter filterParam : this.filterParamterList) {
			filterParam.init();
			filterParam.addListener(this);
		}
	}

	@Override
	public List<IFilterParameter> getParamterList() {
		return new LinkedList<>(this.filterParamterList);
	}

	@Override
	public IFilterParameter addParameter(String name, String value) {
		if((name == null) || (value == null)) {
			throw new NullPointerException();
		}
		else {
			final XMLFilterParameter filterParameter = new XMLFilterParameter();
			filterParameter.setName(name);
			filterParameter.setValue(value);
			filterParameter.init();
			filterParameter.addListener(this);
			this.filterParamterList.add(filterParameter);
			this.changeSupport.firePropertyChange("filterParamterList", this.filterParamterList, null);
			return filterParameter;
		}
	} 

	@Override
	public boolean removeParamter(String name) {
		boolean removed = false;
		if(name == null) {
			throw new NullPointerException();
		}
		else {
			for(XMLFilterParameter filterParamter : this.filterParamterList) {
				if(name.equals(filterParamter.getName())) {
					this.filterParamterList.remove(filterParamter);
					this.changeSupport.firePropertyChange("filterParamterList", this.filterParamterList, null);
					removed = true;
					break;
				}
			}
		}
		return removed;
	}
}
 