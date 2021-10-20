package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="BROKER_CLAIM")
public class BrokerClaim {

	@Id
	@GeneratedValue
	@Column(name="broker_claim_id", nullable = false)
	private long brokerClaimId;
	
	@Column(name="broker_claim_description", nullable = false)
	private String brokerClaimDescription;
	
	@OneToMany(mappedBy="brokerClaim")
	private List<BrokerClaimValue> claimDefaultValueList;
	
	@OneToMany(mappedBy="brokerClaim")
	private List<BrokerClaimValueMapping> brokerClaimValueMappingList;
	
	public BrokerClaim() {
		super(); 
		this.claimDefaultValueList = new LinkedList<>();
		this.brokerClaimValueMappingList = new LinkedList<>();
	}

	public long getBrokerClaimId() {
		return brokerClaimId;
	}

	public void setBrokerClaimId(long brokerClaimId) {
		this.brokerClaimId = brokerClaimId;
	}

	public String getBrokerClaimDescription() {
		return brokerClaimDescription;
	}

	public void setBrokerClaimDescription(String brokerClaimDescription) {
		this.brokerClaimDescription = brokerClaimDescription;
	}
	
	public List<BrokerClaimValue> getClaimDefaultValueList() {
		return EntityUtilities.<BrokerClaimValue>copyList(claimDefaultValueList);
	}

	public void setClaimDefaultValueList(List<BrokerClaimValue> claimDefaultValueList) {
		this.claimDefaultValueList.clear();
		this.claimDefaultValueList.addAll(claimDefaultValueList);
	}
	
	public List<BrokerClaimValueMapping> getBrokerClaimValueMappingList() {
		return EntityUtilities.<BrokerClaimValueMapping>copyList(brokerClaimValueMappingList);
	}

	public void setBrokerClaimValueMappingList(List<BrokerClaimValueMapping> brokerClaimValueMappingList) {
		this.brokerClaimValueMappingList.clear();
		this.brokerClaimValueMappingList.addAll(brokerClaimValueMappingList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brokerClaimDescription == null) ? 0 : brokerClaimDescription.hashCode());
		result = prime * result + (int) (brokerClaimId ^ (brokerClaimId >>> 32));
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
		BrokerClaim other = (BrokerClaim) obj;
		if (brokerClaimDescription == null) {
			if (other.brokerClaimDescription != null)
				return false;
		} else if (!brokerClaimDescription.equals(other.brokerClaimDescription))
			return false;
		if (brokerClaimId != other.brokerClaimId)
			return false;
		return true;
	}
	
	
	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.brokerClaimId + ", " + this.brokerClaimDescription + "}";
	}
}
