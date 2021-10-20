package de.kunz.scraping.conf;

import java.util.*;
import java.beans.*;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.PROPERTY)
class XMLValueConfiguration implements IValueConfiguration {

	@XmlAttribute(name="valueName")
	private String valueName;
	
	@XmlAttribute(name="sourceType")
	private SourceType sourceType;
	
	@XmlAttribute(name="filterChainName")
	private String filterChainName;

	@XmlAttribute(name="reductionChainName")
	private String reductionChainName;

	@XmlElementWrapper(name="RelativeKeySequence")
	@XmlElement(name="KeyFragment")
	private final List<String> relativeKeySequence = new LinkedList<>();
	
	@XmlElementWrapper(name="Parameters")
	@XmlElement(name="Paramter")
	private final Map<String, String> paramMap = new HashMap<>();
	
	@XmlTransient
	private final PropertyChangeSupport changeSupport;
	
	public XMLValueConfiguration() {
		super(); 
		this.changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	@XmlTransient
	public String getValueName() {
		return this.valueName;
	}

	@Override
	public void setValueName(String valueName) {
		final String oldValueName = this.valueName;
		this.valueName = valueName;
		this.changeSupport.firePropertyChange("valueName", oldValueName, valueName);		
	}
	
	@Override
	@XmlTransient
	public SourceType getSourceType() {
		return this.sourceType;
	}
	
	@Override
	public void setSourceType(SourceType sourceType) {
		final SourceType oldSourceType = this.sourceType;
		this.sourceType = sourceType;
		this.changeSupport.firePropertyChange("sourceType", oldSourceType, sourceType);
	}
	
	@Override
	@XmlTransient
	public List<String> getRelativeKeySequence() {
		return new LinkedList<>(this.relativeKeySequence);
	}

	@Override
	public void setRelativeKeySequence(List<String> relativeKeySeq) {
		this.relativeKeySequence.clear();
		this.relativeKeySequence.addAll(relativeKeySeq);
		this.changeSupport.firePropertyChange("relativeKeySequence", this.relativeKeySequence, null);
	}

	@Override
	@XmlTransient
	public String getFilterChainName() {
		return this.filterChainName;
	}

	@Override
	public void setFilterChainName(String filterChainName) {
		final String oldValue = this.filterChainName;
		this.filterChainName = filterChainName;
		this.changeSupport.firePropertyChange("filterChainName", filterChainName, oldValue);	
	}
	
	

	@Override
	@XmlTransient
	public String getReductionChainName() {
		return this.reductionChainName;
	}

	@Override
	public void setReductionChainName(String reductionChainName) {
		final String oldValue = this.reductionChainName;
		this.reductionChainName = reductionChainName;
		this.changeSupport.firePropertyChange("reductionChainName", this.reductionChainName, oldValue);	
	}
 
	@Override
	@XmlTransient
	public Map<String, String> getParameterMap() {
		return this.paramMap;
	}

	@Override
	public void setParameterMap(Map<String, String> paramMap) {
		this.paramMap.clear();
		for(String key : paramMap.keySet()) {
			this.paramMap.put(key, paramMap.get(key));
		}
		this.changeSupport.firePropertyChange("paramMap", this.paramMap, null);	
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listner) {
		this.changeSupport.addPropertyChangeListener(listner);
	}
	
	void init() {
		
	} 

}
