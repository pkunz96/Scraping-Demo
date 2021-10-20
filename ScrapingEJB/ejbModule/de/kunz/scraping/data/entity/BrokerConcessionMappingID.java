package de.kunz.scraping.data.entity;

import java.io.*;

public class BrokerConcessionMappingID implements Serializable {

	private static final long serialVersionUID = -1627547689624217988L;

	private long broker;
	
	private long concession;
	
	public BrokerConcessionMappingID() {
		super();
	}
	
	public long getBroker() {
		return broker;
	}

	public void setBroker(long broker) {
		this.broker = broker;
	}

	public long getConcession() {
		return concession;
	}

	public void setConcession(long concession) {
		this.concession = concession;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (broker ^ (broker >>> 32));
		result = prime * result + (int) (concession ^ (concession >>> 32));
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
		BrokerConcessionMappingID other = (BrokerConcessionMappingID) obj;
		if (broker != other.broker)
			return false;
		if (concession != other.concession)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "EntityID (" + this.getClass().getName() + "): {" + this.broker + ", " + this.concession  + "}";
	}

}







