package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@IdClass(BrokerPropertyValueMappingID.class)
@Table(name="broker_property_value_mapping")
public class BrokerPropertyValueMapping {
	
	@Id
	@JoinColumn(name="fk_broker_id", referencedColumnName = "broker_id")
	private Broker broker;
	
	@Id
	@JoinColumn(name="fk_broker_property_id", referencedColumnName = "broker_property_id")
	private BrokerProperty brokerProperty;
	
	@Id
	@JoinColumn(name="fk_broker_property_value_id", referencedColumnName = "broker_property_value_id")
	private BrokerPropertyValue brokerPropertyValue;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@Transient
	private List<BrokerPropertyValueMapping> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	
	public BrokerPropertyValueMapping() {
		super();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
	
	
	private BrokerPropertyValueMapping(boolean isConflictVersion, BrokerProperty property, BrokerPropertyValue propertyValue, Datasource datasource) {
		this();
		this.brokerProperty = property;
		this.brokerPropertyValue = propertyValue;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}
	
	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public BrokerProperty getBrokerProperty() {
		return brokerProperty;
	}

	public void setBrokerProperty(BrokerProperty brokerProperty) {
		if(this.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		this.brokerProperty = brokerProperty;
	}

	public BrokerPropertyValue getBrokerPropertyValue() {
		return brokerPropertyValue;
	}

	public void setBrokerPropertyValue(BrokerPropertyValue brokerPropertyValue) {
		if(this.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		this.brokerPropertyValue = brokerPropertyValue;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		if(this.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		this.datasource = datasource;
	}

	public List<BrokerPropertyValueMapping> getConflictingVersionsList() {
		return EntityUtilities.<BrokerPropertyValueMapping>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(BrokerPropertyValueMapping valueMapping) {
		if(valueMapping == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final BrokerPropertyValueMapping conflictVersion = new BrokerPropertyValueMapping(true, valueMapping.getBrokerProperty(), valueMapping.getBrokerPropertyValue(), valueMapping.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}
	
	public void removeConflictingVersion(BrokerPropertyValueMapping valueMapping) {
		if(valueMapping  == null) {
			throw new NullPointerException();
		}
		else if(!valueMapping.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(BrokerPropertyValueMapping conflictionVersion : this.conflictVersionsList) {
				if(valueMapping.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(valueMapping);
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((broker == null) ? 0 : broker.hashCode());
		result = prime * result + ((brokerProperty == null) ? 0 : brokerProperty.hashCode());
		result = prime * result + ((brokerPropertyValue == null) ? 0 : brokerPropertyValue.hashCode());
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
		BrokerPropertyValueMapping other = (BrokerPropertyValueMapping) obj;
		if (broker == null) {
			if (other.broker != null)
				return false;
		} else if (!broker.equals(other.broker))
			return false;
		if (brokerProperty == null) {
			if (other.brokerProperty != null)
				return false;
		} else if (!brokerProperty.equals(other.brokerProperty))
			return false;
		if (brokerPropertyValue == null) {
			if (other.brokerPropertyValue != null)
				return false;
		} else if (!brokerPropertyValue.equals(other.brokerPropertyValue))
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
		return "Entity (" + this.getClass().getName() + "): {" + this.broker + ", " + this.brokerProperty + ", " + this.brokerPropertyValue + "}";
	}
}
