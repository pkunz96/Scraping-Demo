package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

import de.kunz.scraping.data.querying.*;

@Entity
@Table(name="BROKER")
public class Broker implements Queryable {

	@Id
	@GeneratedValue
	@Column(name="broker _id")
	private long brokerId;
		
	@OneToOne 
	@JoinColumn(name="fk_person_id", referencedColumnName = "person_id", unique = true)
	private Person person;
	
	@OneToOne 
	@JoinColumn(name="fk_business_id", referencedColumnName = "business_id", unique = true)
	private Business business; 
	 
	@Column(name="user_context_id", unique = true)
	private long userContextId;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@OneToMany(mappedBy="comissioningBroker")
	private List<BusinessRelation> comissionedBusinessRelationsList;
	
	@OneToMany(mappedBy="comissionedBroker") 
	private List<BusinessRelation> comissioningBusinessRelationsList;
	 
	@OneToMany(mappedBy="broker")
	private List<BrokerConcessionMapping> brokerConcessionMappingList;
	
	@OneToMany(mappedBy="broker")
	private List<BrokerClaimValueMapping> brokerClaimValueMappingList; 
	 
	@OneToMany(mappedBy="broker")
	private List<BrokerPropertyValueMapping> brokerPropertyValueMappingList;
	
	@Transient
	private List<Broker> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
		
	public Broker() {
		super();
		this.comissionedBusinessRelationsList = new LinkedList<>();
		this.comissioningBusinessRelationsList = new LinkedList<>();
		this.brokerConcessionMappingList = new LinkedList<>();
		this.brokerClaimValueMappingList = new LinkedList<>();
		this.brokerPropertyValueMappingList = new LinkedList<>();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}

	private Broker(boolean isConflictVersion, long brokerId, Datasource datasource) {
		this();
		this.brokerId = brokerId;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}
	
	public long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(long brokerId) {
		this.brokerId = brokerId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.person = person;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.business = business;
	}

	public long getUserContextId() {
		return userContextId;
	}

	public void setUserContextId(long userContextId) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.userContextId = userContextId;
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

	public List<BusinessRelation> getComissionedBusinessRelationsList() {
		return EntityUtilities.<BusinessRelation>copyList(this.comissionedBusinessRelationsList);
	}

	public void setComissionedBusinessRelationsList(List<BusinessRelation> comissionedBusinessRelationsList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.comissionedBusinessRelationsList.clear();
		this.comissionedBusinessRelationsList.addAll(comissionedBusinessRelationsList);
	}

	public List<BusinessRelation> getComissioningBusinessRelationsList() {
		return EntityUtilities.<BusinessRelation>copyList(this.comissioningBusinessRelationsList);
	}

	public void setComissioningBusinessRelationsList(List<BusinessRelation> comissioningBusinessRelationsList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.comissioningBusinessRelationsList.clear();
		this.comissioningBusinessRelationsList.addAll(comissioningBusinessRelationsList);
	}

	public List<BrokerConcessionMapping> getBrokerConcessionMappingList() {
		return EntityUtilities.<BrokerConcessionMapping>copyList(this.brokerConcessionMappingList);
	}

	public void setBrokerConcessionMappingList(List<BrokerConcessionMapping> brokerConcessionMappingList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.brokerConcessionMappingList.clear();
		this.brokerConcessionMappingList.addAll(brokerConcessionMappingList);
	}

	public List<BrokerClaimValueMapping> getBrokerClaimValueMappingList() {
		return EntityUtilities.<BrokerClaimValueMapping>copyList(this.brokerClaimValueMappingList);
	}

	public void setBrokerClaimValueMappingList(List<BrokerClaimValueMapping> brokerClaimValueMappingList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.brokerClaimValueMappingList.clear();
		this.brokerClaimValueMappingList.addAll(brokerClaimValueMappingList);
	}

	public List<BrokerPropertyValueMapping> getBrokerPropertyValueMappingList() {
		return EntityUtilities.<BrokerPropertyValueMapping>copyList(this.brokerPropertyValueMappingList);
	}
	
	public void setBrokerPropertyValueMappingList(List<BrokerPropertyValueMapping> brokerPropertyValueMappingList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.brokerPropertyValueMappingList.clear();
		this.brokerPropertyValueMappingList.addAll(brokerPropertyValueMappingList);
	}
	
	public boolean isEmployee() {
		return (person != null) && (business == null);
	}
	
	public boolean isSoleTrader() {
		return (person != null) && (business != null);
	}

	public boolean isCorporation() {
		return (person == null) && (business != null);
	}

	public List<Broker> getConflictingVersionsList() {
		return EntityUtilities.<Broker>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(Broker broker) {
		if(broker == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final Broker conflictVersion = new Broker(true, broker.getBrokerId(), broker.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}
	
	public void addConflictingVersions(List<Broker> conflictVersions) {
		for(Broker conflictVersion : conflictVersions) {
			if(!conflictVersion.isConflictVersion) {
				throw new IllegalArgumentException();
			}
			else if(!this.conflictVersionsList.contains(conflictVersion)) {
				this.conflictVersionsList.add(conflictVersion);
			}
		}
	}
	
	public void removeConflictingVersion(Broker broker) {
		if(broker  == null) {
			throw new NullPointerException();
		}
		else if(!broker.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(Broker conflictionVersion : this.conflictVersionsList) {
				if(broker.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(broker);
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (brokerId ^ (brokerId >>> 32));
		result = prime * result + ((business == null) ? 0 : business.hashCode());
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + (int) (userContextId ^ (userContextId >>> 32));
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
		Broker other = (Broker) obj;
		if (brokerId != other.brokerId)
			return false;
		if (business == null) {
			if (other.business != null)
				return false;
		} else if (!business.equals(other.business))
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
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (userContextId != other.userContextId)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.brokerId + ", " + this.person + ", " + this.business + "}";
	}
}
