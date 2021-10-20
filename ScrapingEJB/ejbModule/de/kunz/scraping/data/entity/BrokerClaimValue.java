package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="BROKER_CLAIM_VALUE")
public class BrokerClaimValue {

	@Id
	@GeneratedValue
	@Column(name="broker_claim_value_id")
	private long brokerClaumValueId;
	
	@Column(name="string_value")
	private String stringValue;
	
	@Column(name="integer_value")
	private long integerValue;
	
	@Column(name="float_value")
	private double floatValue;
	
	@Column(name="boolean_value")
	private boolean booleanValue;
	
	@ManyToOne
	@JoinColumn(name="fk_broker_claim_id", referencedColumnName = "broker_claim_id")
	private BrokerClaim brokerClaim;
	
	@OneToMany(mappedBy="brokerClaimValue")
	private List<BrokerClaimValueMapping> brokerClaimValueMappingList;
		
	public BrokerClaimValue() {
		super();
		this.brokerClaimValueMappingList = new LinkedList<>();
	}

	public long getBrokerClaumValueId() {
		return brokerClaumValueId;
	}

	public void setBrokerClaumValueId(long brokerClaumValueId) {
		this.brokerClaumValueId = brokerClaumValueId;
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

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	
	public List<BrokerClaimValueMapping> getBrokerClaimValueMappingList() {
		return EntityUtilities.<BrokerClaimValueMapping>copyList(brokerClaimValueMappingList);
	}

	public void setBrokerClaimValueMappingList(List<BrokerClaimValueMapping> brokerClaimValueMappingList) {
		this.brokerClaimValueMappingList.clear();
		this.brokerClaimValueMappingList.addAll(brokerClaimValueMappingList);
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.brokerClaumValueId + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (booleanValue ? 1231 : 1237);
		result = prime * result + (int) (brokerClaumValueId ^ (brokerClaumValueId >>> 32));
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
		BrokerClaimValue other = (BrokerClaimValue) obj;
		if (booleanValue != other.booleanValue)
			return false;
		if (brokerClaumValueId != other.brokerClaumValueId)
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
}
