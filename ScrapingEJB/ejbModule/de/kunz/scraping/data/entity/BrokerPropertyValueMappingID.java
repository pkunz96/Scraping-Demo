package de.kunz.scraping.data.entity;

import java.io.*;

public class BrokerPropertyValueMappingID implements Serializable {

	private static final long serialVersionUID = -5410200750107922173L;

	private long broker;
	
	private long brokerProperty;

	private long brokerPropertyValue;
	
	public BrokerPropertyValueMappingID() {
		super();
	}

	public long getBroker() {
		return broker;
	}

	public void setBroker(long broker) {
		this.broker = broker;
	}

	public long getBrokerProperty() {
		return brokerProperty;
	}

	public void setBrokerProperty(long brokerProperty) {
		this.brokerProperty = brokerProperty;
	}

	public long getBrokerPropertyValue() {
		return brokerPropertyValue;
	}

	public void setBrokerPropertyValue(long brokerPropertyValue) {
		this.brokerPropertyValue = brokerPropertyValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (broker ^ (broker >>> 32));
		result = prime * result + (int) (brokerProperty ^ (brokerProperty >>> 32));
		result = prime * result + (int) (brokerPropertyValue ^ (brokerPropertyValue >>> 32));
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
		BrokerPropertyValueMappingID other = (BrokerPropertyValueMappingID) obj;
		if (broker != other.broker)
			return false;
		if (brokerProperty != other.brokerProperty)
			return false;
		if (brokerPropertyValue != other.brokerPropertyValue)
			return false;
		return true;
	}
	
	@Override 
	public String toString() {
		return "EntityID (" + this.getClass().getName() + "): {" + this.broker + ", " + this.brokerProperty + ", " + this.brokerPropertyValue + "}";
	}

}
