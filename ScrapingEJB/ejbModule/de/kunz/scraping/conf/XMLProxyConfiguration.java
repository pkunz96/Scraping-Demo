package de.kunz.scraping.conf;

import java.beans.*;
import java.net.Proxy.Type;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLProxyConfiguration implements IProxyConfiguration {

	private final PropertyChangeSupport changeSupport;
	
	@XmlAttribute(name="type")
	private Type type;
	
	@XmlAttribute(name="hostname")
	private String hostname;
	
	@XmlAttribute(name="port")
	private int port;
	
	@XmlAttribute(name="isEnabled")
	private boolean isEnabled;
	
	public XMLProxyConfiguration() {
		super();
		this.changeSupport = new PropertyChangeSupport(this);
	}
	
	@Override
	@XmlTransient
	public Type getType() {
		return type;
	}

	@Override
	@XmlTransient 
	public String getHostname() {
		return hostname;
	}
 
	@Override
	@XmlTransient
	public int getPort() {
		return port;
	}
	
	@Override
	@XmlTransient
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public void setType(Type type) {
		final Type oldType = this.type;
		this.type = type;
		this.changeSupport.firePropertyChange("type", oldType, type);
	}

	@Override
	public void setHostname(String hostname) {
		final String oldHostname = this.hostname;
		this.hostname = hostname;
		this.changeSupport.firePropertyChange("hostname", oldHostname, hostname);
	}

	@Override
	public void setPort(int port) {
		final int oldPort = this.port;
		this.port = port;
		this.changeSupport.firePropertyChange("port", oldPort, port);
	}
	


	@Override
	public void enable() {
		final boolean oldIsEnabled = this.isEnabled;
		this.isEnabled = true;
		this.changeSupport.firePropertyChange("isEnabled", oldIsEnabled, true);
	}

	@Override
	public void disable() {
		final boolean oldIsEnabled = this.isEnabled;
		this.isEnabled = false;
		this.changeSupport.firePropertyChange("isEnabled", oldIsEnabled, false);		
	} 
	
	void addPropertyChangeListener(PropertyChangeListener listener) {
		if(listener == null) {
			throw new NullPointerException();
		}
		else {
			this.changeSupport.addPropertyChangeListener(listener);
		}
	}
}
 