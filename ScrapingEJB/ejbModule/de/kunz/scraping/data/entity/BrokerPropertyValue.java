package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="BROKER_PROPERTY_VALUE")
public class BrokerPropertyValue {

	@Id
	@GeneratedValue
	@Column(name="broker_property_value_id")
	private long brokerPropertyValueId;
	
	@Column(name="string_value")
	private String stringValue;
	 
	@Column(name="integer_value")
	private long integerValue;
	
	@Column(name="float_value")
	private double floatValue;
	
	@Column(name="boolean_value")
	private boolean doubleValue;
	
	@JoinColumn(name="fk_broker_property_id", referencedColumnName = "broker_property_id")
	private BrokerProperty brokerProperty;
	
	@OneToMany(mappedBy="brokerPropertyValue")
	private List<BrokerPropertyValueMapping> brokerPropertyValueMappingList;
	
	public BrokerPropertyValue() {
		super();
		this.brokerPropertyValueMappingList = new LinkedList<>();
	}

	public long getBrokerPropertyValueId() {
		return brokerPropertyValueId;
	}

	public void setBrokerPropertyValueId(long brokerPropertyValueId) {
		this.brokerPropertyValueId = brokerPropertyValueId;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public long getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(long integerValue) {
		this.integerValue = integerValue;
	}

	public double getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(double floatValue) {
		this.floatValue = floatValue;
	}

	public boolean isDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(boolean doubleValue) {
		this.doubleValue = doubleValue;
	}

	public BrokerProperty getBrokerProperty() {
		return brokerProperty;
	}

	public void setBrokerProperty(BrokerProperty brokerProperty) {
		this.brokerProperty = brokerProperty;
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
		result = prime * result + ((brokerProperty == null) ? 0 : brokerProperty.hashCode());
		result = prime * result + (int) (brokerPropertyValueId ^ (brokerPropertyValueId >>> 32));
		result = prime * result + (doubleValue ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(floatValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (integerValue ^ (integerValue >>> 32));
		result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
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
		BrokerPropertyValue other = (BrokerPropertyValue) obj;
		if (brokerProperty == null) {
			if (other.brokerProperty != null)
				return false;
		} else if (!brokerProperty.equals(other.brokerProperty))
			return false;
		if (brokerPropertyValueId != other.brokerPropertyValueId)
			return false;
		if (doubleValue != other.doubleValue)
			return false;
		if (Double.doubleToLongBits(floatValue) != Double.doubleToLongBits(other.floatValue))
			return false;
		if (integerValue != other.integerValue)
			return false;
		if (stringValue == null) {
			if (other.stringValue != null)
				return false;
		} else if (!stringValue.equals(other.stringValue))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.brokerPropertyValueId + "}";
	}

}
