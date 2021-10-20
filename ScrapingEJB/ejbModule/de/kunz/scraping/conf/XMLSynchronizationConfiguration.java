package de.kunz.scraping.conf;

import java.beans.*;

final class XMLSynchronizationConfiguration implements ISynchronizationConfiguration  {

	private final PropertyChangeSupport changeSupport;
	
	public XMLSynchronizationConfiguration() {
		this.changeSupport = new PropertyChangeSupport(this);
	}

	public void addListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}
	
	void init() {
		
	}
}
