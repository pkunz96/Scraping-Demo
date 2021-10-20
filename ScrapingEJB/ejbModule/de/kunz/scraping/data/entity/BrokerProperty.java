package de.kunz.scraping.data.entity;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="BROKER_PROPERTY")
public class BrokerProperty {

	@Id
	@GeneratedValue
	@Column(name="broker_property_id")
	private long brokerPropertyId;
	
	@Column(name="broker_property_description", nullable = false)
	private String brokerPropertyDescription;
	
	@OneToMany(mappedBy="brokerProperty")
	private List<BrokerPropertyValue> brokerPropertyValueList;
	
	@OneToMany(mappedBy="brokerProperty")
	private List<BrokerPropertyValueMapping> brokerPropertyValueMappingList;
	
	public BrokerProperty() {
		super();
		this.brokerPropertyValueList = new LinkedList<>();
		this.brokerPropertyValueMappingList = new LinkedList<>();
	}

	public long getBrokerPropertyId() {
		return brokerPropertyId;
	}

	public void setBrokerPropertyId(long brokerPropertyId) {
		this.brokerPropertyId = brokerPropertyId;
	}

	public String getBrokerPropertyDescription() {
		return brokerPropertyDescription;
	}

	public void setBrokerPropertyDescription(String brokerPropertyDescription) {
		this.brokerPropertyDescription = brokerPropertyDescription;
	}

	public List<BrokerPropertyValue> getBrokerPropertyDefaultValueList() {
		return EntityUtilities.<BrokerPropertyValue>copyList(brokerPropertyValueList);
	}

	public void setBrokerPropertyDefaultValueList(List<BrokerPropertyValue> brokerPropertyValueList) {
		this.brokerPropertyValueList.clear();
		this.brokerPropertyValueList.addAll(brokerPropertyValueList);
	}

	public List<BrokerPropertyValueMapping> getBrokerPropertyValueMappingList() {
		return EntityUtilities.<BrokerPropertyValueMapping>copyList(this.brokerPropertyValueMappingList);
	}

	public void setBrokerPropertyValueMappingList(List<BrokerPropertyValueMapping> brokerPropertyValueMappingList) {
		this.brokerPropertyValueMappingList.clear();
		this.brokerPropertyValueMappingList.addAll(brokerPropertyValueMappingList);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brokerPropertyDescription == null) ? 0 : brokerPropertyDescription.hashCode());
		result = prime * result + (int) (brokerPropertyId ^ (brokerPropertyId >>> 32));
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
		BrokerProperty other = (BrokerProperty) obj;
		if (brokerPropertyDescription == null) {
			if (other.brokerPropertyDescription != null)
				return false;
		} else if (!brokerPropertyDescription.equals(other.brokerPropertyDescription))
			return false;
		if (brokerPropertyId != other.brokerPropertyId)
			return false;
		return true;
	}
	
	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.brokerPropertyId + ", " + this.brokerPropertyDescription + "}";
	}

}
