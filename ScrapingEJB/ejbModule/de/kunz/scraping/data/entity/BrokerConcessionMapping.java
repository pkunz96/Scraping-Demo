package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@IdClass(BrokerConcessionMappingID.class)
@Table(name="broker_concession_mapping")
public class BrokerConcessionMapping {

	@Id
	@JoinColumn(name="fk_broker_id", referencedColumnName = "broker_id")
	private Broker broker;
	
	@Id
	@JoinColumn(name="fk_concession_id", referencedColumnName = "concession_id")
	private Concession concession;
	
	@Column(name="register_no", unique = true)
	private String registerNo;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;

	@Transient
	private final List<BrokerConcessionMapping> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public BrokerConcessionMapping() {
		super();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
	
	private BrokerConcessionMapping(boolean isConflictVersion, String registrationNo,  Concession concession, Datasource datasource) {
		this();
		this.registerNo = registrationNo;
		this.concession = concession;
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

	public Concession getConcession() {
		return concession;
	}

	public void setConcession(Concession concession) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.concession = concession;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.registerNo = registerNo;
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
	
	public List<BrokerConcessionMapping> getConflictingVersionsList() {
		return EntityUtilities.<BrokerConcessionMapping>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(BrokerConcessionMapping brokerConcessionMapping) {
		if(brokerConcessionMapping == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final BrokerConcessionMapping conflictVersion = new BrokerConcessionMapping(true, brokerConcessionMapping.getRegisterNo(), brokerConcessionMapping.getConcession(), brokerConcessionMapping.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}	
	
	public void removeConflictingVersion(BrokerConcessionMapping brokerConcessionMapping) {
		if(brokerConcessionMapping  == null) {
			throw new NullPointerException();
		}
		else if(!brokerConcessionMapping.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(BrokerConcessionMapping conflictionVersion : this.conflictVersionsList) {
				if(brokerConcessionMapping.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(brokerConcessionMapping);
				}
			} 
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((broker == null) ? 0 : broker.hashCode());
		result = prime * result + ((concession == null) ? 0 : concession.hashCode());
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((registerNo == null) ? 0 : registerNo.hashCode());
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
		BrokerConcessionMapping other = (BrokerConcessionMapping) obj;
		if (broker == null) {
			if (other.broker != null)
				return false;
		} else if (!broker.equals(other.broker))
			return false;
		if (concession == null) {
			if (other.concession != null)
				return false;
		} else if (!concession.equals(other.concession))
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
		if (registerNo == null) {
			if (other.registerNo != null)
				return false;
		} else if (!registerNo.equals(other.registerNo))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.broker + ", " + this.concession + "}";
	}
}
