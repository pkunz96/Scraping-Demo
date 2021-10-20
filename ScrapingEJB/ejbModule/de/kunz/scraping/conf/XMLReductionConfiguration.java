package de.kunz.scraping.conf;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

final class XMLReductionConfiguration implements IReductionConfiguration {

	private final PropertyChangeSupport changeSupport;
	
	public XMLReductionConfiguration() {
		this.changeSupport = new PropertyChangeSupport(this);
	}

	public void addListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}
	
	void init() {
		
	}
}
