package de.kunz.scraping.conf;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class XMLFilterParameter implements IFilterParameter {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="value")
	private String value;

	@XmlTransient
	private PropertyChangeSupport changeSupport;
	
	public XMLFilterParameter() {
		super();
		this.changeSupport = new PropertyChangeSupport(this);
	}
	
	@Override
	@XmlTransient
	public String getName() {
		return this.name; 
	}

	@Override
	public void setName(String name) {
		final String oldValue = this.name;
		this.name = name;
		this.changeSupport.firePropertyChange("name", oldValue, name);
	}

	@Override
	@XmlTransient
	public String getValue() {
		return this.value;
	}
	
	@Override
	public void setValue(String value) {
		final String oldValue = this.value;
		this.value = value;
		this.changeSupport.firePropertyChange("value", oldValue, value);		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		XMLFilterParameter other = (XMLFilterParameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	void addListener(PropertyChangeListener listener) {		
		this.changeSupport.addPropertyChangeListener(listener);
	}  
	
	void init() {
		
	}
	
}
