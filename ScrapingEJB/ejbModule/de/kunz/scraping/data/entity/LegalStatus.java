package de.kunz.scraping.data.entity;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="LEGAL_STATUS")
public class LegalStatus {

	@Id
	@Column(name="legal_status_id")
	private long legalStatusId;
	
	@Column(name="legal_status_description", nullable = false, unique = true)
	private String legalStatusDescription;
	
	@OneToMany(mappedBy="legalStatus")
	private List<BusinessRelation> businessRelationList;
	
	public LegalStatus() {
		super();
		this.businessRelationList = new LinkedList<>();
	}

	public long getLegalStatusId() {
		return legalStatusId;
	}

	public void setLegalStatusId(long legalStatusId) {
		this.legalStatusId = legalStatusId;
	}

	public String getLegalStatusDescription() {
		return legalStatusDescription;
	}

	public void setLegalStatusDescription(String legalStatusDescription) {
		this.legalStatusDescription = legalStatusDescription;
	}

	public List<BusinessRelation> getBusinessRelationList() {
		return EntityUtilities.<BusinessRelation>copyList(this.businessRelationList);
	}

	public void setBusinessRelationList(List<BusinessRelation> businessRelationList) {
		this.businessRelationList.clear();
		this.businessRelationList.addAll(businessRelationList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legalStatusDescription == null) ? 0 : legalStatusDescription.hashCode());
		result = prime * result + (int) (legalStatusId ^ (legalStatusId >>> 32));
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
		LegalStatus other = (LegalStatus) obj;
		if (legalStatusDescription == null) {
			if (other.legalStatusDescription != null)
				return false;
		} else if (!legalStatusDescription.equals(other.legalStatusDescription))
			return false;
		if (legalStatusId != other.legalStatusId)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.legalStatusId + ", " + this.legalStatusDescription + "}";
	}
}
