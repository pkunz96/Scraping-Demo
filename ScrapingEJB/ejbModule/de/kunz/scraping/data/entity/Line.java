package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name="LINE")
public class Line {

	@Id
	@Column(name="line_id")
	private long lineId;
	
	@Column(name="line_description", nullable = false, unique = true)
	private String lineDescription;
	
	@OneToMany(mappedBy="line")
	private List<Revenue> revenueList;
	
	public Line() {
		super();
		this.revenueList = new LinkedList<>();
	}
	
	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public String getLineDescription() {
		return lineDescription;
	}

	public void setLineDescription(String lineDescription) {
		this.lineDescription = lineDescription;
	}

	public List<Revenue> getRevenueList() {
		return EntityUtilities.<Revenue>copyList(revenueList);
	}

	public void setRevenueList(List<Revenue> revenueList) {
		this.revenueList.clear();
		this.revenueList.addAll(revenueList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineDescription == null) ? 0 : lineDescription.hashCode());
		result = prime * result + (int) (lineId ^ (lineId >>> 32));
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
		Line other = (Line) obj;
		if (lineDescription == null) {
			if (other.lineDescription != null)
				return false;
		} else if (!lineDescription.equals(other.lineDescription))
			return false;
		if (lineId != other.lineId)
			return false;
		return true;
	}
	
	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.lineId + ", " + this.lineDescription + "}";
	}

}
