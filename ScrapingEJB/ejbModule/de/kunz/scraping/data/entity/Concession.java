package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="CONCESSION")
public class Concession {

	@Id
	@Column(name="concession_id")
	private long concessionId;
	
	@Column(name="concession_description", nullable = false, unique = true)
	private String concessionDescription;

	@OneToMany(mappedBy="concession")
	private List<BrokerConcessionMapping> brokerConcessionMappingList;
	
	public Concession() {
		super();
		this.brokerConcessionMappingList = new LinkedList<>();
	}

	public long getConcessionId() {
		return concessionId;
	} 

	public void setConcessionId(long concessionId) {
		this.concessionId = concessionId;
	}

	public String getConcessionDescription() {
		return concessionDescription;
	}

	public void setConcessionDescription(String concessionDescription) {
		this.concessionDescription = concessionDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concessionDescription == null) ? 0 : concessionDescription.hashCode());
		result = prime * result + (int) (concessionId ^ (concessionId >>> 32));
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
		Concession other = (Concession) obj;
		if (concessionDescription == null) {
			if (other.concessionDescription != null)
				return false;
		} else if (!concessionDescription.equals(other.concessionDescription))
			return false;
		if (concessionId != other.concessionId)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.concessionId + ", " + this.concessionDescription + "}";
	}
	
}
