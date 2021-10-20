package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;


@Entity
@IdClass(BrokerClaimValueMappingID.class)
@Table(name="broker_claim_value_mapping")
public class BrokerClaimValueMapping {

	@Id
	@JoinColumn(name="fk_broker_id", referencedColumnName = "broker_id")
	private Broker broker;
	
	@Id
	@JoinColumn(name="fk_broker_claim_id", referencedColumnName = "broker_claim_id")
	private BrokerClaim brokerClaim;
	
	@Id
	@JoinColumn(name="fk_broker_claim_value_id", referencedColumnName = "broker_claim_value_id")
	private BrokerClaimValue brokerClaimValue;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@Transient
	private List<BrokerClaimValueMapping> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public BrokerClaimValueMapping() {
		super();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
		
	private BrokerClaimValueMapping(boolean isConflictVersion, BrokerClaim claim,  BrokerClaimValue claimValue, Datasource datasource) {
		this();
		this.brokerClaim = claim;
		this.brokerClaimValue = claimValue;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.broker = broker;
	}

	public BrokerClaim getBrokerClaim() {
		return brokerClaim;
	}

	public void setBrokerClaim(BrokerClaim brokerClaim) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.brokerClaim = brokerClaim;
	}

	public BrokerClaimValue getBrokerClaimValue() {
		return brokerClaimValue;
	}

	public void setBrokerClaimValue(BrokerClaimValue brokerClaimValue) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.brokerClaimValue = brokerClaimValue;
	}
	
	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.datasource = datasource;
	}
	
	public List<BrokerClaimValueMapping> getConflictingVersionsList() {
		return EntityUtilities.<BrokerClaimValueMapping>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(BrokerClaimValueMapping claimMapping) {
		if(claimMapping == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final BrokerClaimValueMapping conflictVersion = new BrokerClaimValueMapping(true, claimMapping.getBrokerClaim(), claimMapping.getBrokerClaimValue(), claimMapping.getDatasource());	
		this.conflictVersionsList.add(conflictVersion);
	}
	
	public void removeConflictingVersion(BrokerClaimValueMapping claimMapping) {
		if(claimMapping  == null) {
			throw new NullPointerException();
		}
		else if(!claimMapping.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(BrokerClaimValueMapping conflictionVersion : this.conflictVersionsList) {
				if(claimMapping.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(claimMapping);
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((broker == null) ? 0 : broker.hashCode());
		result = prime * result + ((brokerClaim == null) ? 0 : brokerClaim.hashCode());
		result = prime * result + ((brokerClaimValue == null) ? 0 : brokerClaimValue.hashCode());
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
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
		BrokerClaimValueMapping other = (BrokerClaimValueMapping) obj;
		if (broker == null) {
			if (other.broker != null)
				return false;
		} else if (!broker.equals(other.broker))
			return false;
		if (brokerClaim == null) {
			if (other.brokerClaim != null)
				return false;
		} else if (!brokerClaim.equals(other.brokerClaim))
			return false;
		if (brokerClaimValue == null) {
			if (other.brokerClaimValue != null)
				return false;
		} else if (!brokerClaimValue.equals(other.brokerClaimValue))
			return false;
		if (conflictVersionsList == null) {
			if (other.conflictVersionsList != null)
				return false;
		} else if (!conflictVersionsList.equals(other.conflictVersionsList))
			return false;
		if (datasource == null) {
			if (other.datasource != null)
				return false;
		} else if (!datasource.equals(other.datasource))
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.broker + ", " + this.brokerClaim + ", " + this.brokerClaimValue + "}";
	}
}
