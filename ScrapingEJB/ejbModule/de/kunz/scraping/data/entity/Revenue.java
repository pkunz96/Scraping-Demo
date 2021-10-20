package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;


@Entity
@IdClass(RevenueID.class)
@Table(name="REVENUE")
public class Revenue {

	@Id
	@JoinColumn(name="fk_business_relation_id", referencedColumnName = "business_relation_id")
	private BusinessRelation businessRelation;
	
	@Id
	@GeneratedValue
	@Column(name="business_year")
	private int businessYear;
	
	@Id
	@JoinColumn(name="fk_line_id", referencedColumnName = "line_id")
	private Line line;
	
	@Column(name="revenue", nullable = false)
	private long revenue;
	
	@Transient
	private List<Revenue> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public Revenue() {
		super();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
		
	private Revenue(boolean isConflictVersion, int businessYear, long revenue, Line line) { 
		this();
		this.businessYear = businessYear;
		this.revenue = revenue;
		this.line = line;
		this.isConflictVersion = isConflictVersion;
	}

	public BusinessRelation getBusinessRelation() { 
		return businessRelation;
	}

	public void setBusinessRelation(BusinessRelation businessRelation) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.businessRelation = businessRelation;
	}

	public int getBusinessYear() {
		return businessYear;
	}

	public void setBusinessYear(int businessYear) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.businessYear = businessYear;
	}

	public long getRevenue() {
		return revenue;
	}

	public void setRevenue(long revenue) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.revenue = revenue;
	}

	public Line getLine() { 
		return line;
	}

	public void setLine(Line line) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.line = line;
	}
	
	public List<Revenue> getConflictingVersionsList() {
		return EntityUtilities.<Revenue>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(Revenue revenue) {
		if(revenue == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final Revenue conflictVersion = new Revenue(true, revenue.getBusinessYear(), revenue.getRevenue(), revenue.getLine());
		this.conflictVersionsList.add(conflictVersion);
	}	
	
	public void removeConflictingVersion(Revenue revenue) {
		if(revenue  == null) {
			throw new NullPointerException();
		}
		else if(!revenue.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(Revenue conflictionVersion : this.conflictVersionsList) {
				if(revenue.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(revenue);
				}
			} 
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessRelation == null) ? 0 : businessRelation.hashCode());
		result = prime * result + businessYear;
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + (int) (revenue ^ (revenue >>> 32));
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
		Revenue other = (Revenue) obj;
		if (businessRelation == null) {
			if (other.businessRelation != null)
				return false;
		} else if (!businessRelation.equals(other.businessRelation))
			return false;
		if (businessYear != other.businessYear)
			return false;
		if (conflictVersionsList == null) {
			if (other.conflictVersionsList != null)
				return false;
		} else if (!conflictVersionsList.equals(other.conflictVersionsList))
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		if (revenue != other.revenue)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.businessRelation + ", " + this.businessYear + ", " + this.line + "}";
	}
}
