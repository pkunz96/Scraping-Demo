package de.kunz.scraping.data.entity;

import java.io.*;

public class BrokerClaimValueMappingID implements Serializable {

	private static final long serialVersionUID = -5410200750107922173L;

	private long broker;
	
	private long brokerClaim;

	private long brokerClaimValue;
	
	public BrokerClaimValueMappingID() {
		super();
	}
	
	public long getBroker() {
		return broker;
	}

	public void setBroker(long broker) {
		this.broker = broker;
	}

	public long getBrokerClaim() {
		return brokerClaim;
	}

	public void setBrokerClaim(long brokerClaim) {
		this.brokerClaim = brokerClaim;
	}
	
	public long getBrokerClaimValue() {
		return brokerClaimValue;
	}
	
	public void setBrokerClaimValue(long brokerClaimValue) {
		this.brokerClaimValue = brokerClaimValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (broker ^ (broker >>> 32));
		result = prime * result + (int) (brokerClaim ^ (brokerClaim >>> 32));
		result = prime * result + (int) (brokerClaimValue ^ (brokerClaimValue >>> 32));
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
		BrokerClaimValueMappingID other = (BrokerClaimValueMappingID) obj;
		if (broker != other.broker)
			return false;
		if (brokerClaim != other.brokerClaim)
			return false;
		if (brokerClaimValue != other.brokerClaimValue)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "EntityID (" + this.getClass().getName() + "): {" + this.broker + ", " + this.brokerClaim + ", " + this.brokerClaimValue + "}";
	}

}
