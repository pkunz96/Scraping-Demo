package de.kunz.scraping.data.entity;

import java.io.*;

public class RevenueID implements Serializable {

	private static final long serialVersionUID = -1627547689624217988L;

	private long businessRelation;

	private int businessYear;
	
	private long line;
	
	public RevenueID() {
		super();
	}
	
	public long getBusinessRelation() {
		return businessRelation;
	}

	public void setBusinessRelation(long businessRelation) {
		this.businessRelation = businessRelation;
	}

	public int getBusinessYear() {
		return businessYear;
	}

	public void setBusinessYear(int businessYear) {
		this.businessYear = businessYear;
	}

	public long getLine() {
		return line;
	}

	public void setLine(long line) {
		this.line = line;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (businessRelation ^ (businessRelation >>> 32));
		result = prime * result + businessYear;
		result = prime * result + (int) (line ^ (line >>> 32));
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
		RevenueID other = (RevenueID) obj;
		if (businessRelation != other.businessRelation)
			return false;
		if (businessYear != other.businessYear)
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "EntityID (" + this.getClass().getName() + "): {" + this.businessRelation + ", " + this.businessYear + ", "  + this.line + "}";
	}

}







