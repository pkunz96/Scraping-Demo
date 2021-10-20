package de.kunz.scraping.identification;

import de.kunz.scraping.data.entity.*;

final class IdentificationTask {

	private final Broker firstBroker;
	
	private final Broker secondBrokder;
	
	private boolean isIdentical;
	
	private boolean isCompleted;
	
	public IdentificationTask(Broker broker, Broker secondBroker) {
		this.firstBroker = broker; 
		this.secondBrokder = secondBroker;
		this.isIdentical = false;
		this.isCompleted = false;
	}

	public Broker getFirstBroker() {
		return firstBroker;
	}

	public Broker getSecondBroker() {
		return secondBrokder;
	}

	public boolean isIdentical() {
		return isIdentical;
	}

	public void setIdentical(boolean isIdentic) {
		this.isIdentical = isIdentic;
	}

	
	public boolean isCompleted() {
		return isCompleted;
	}
	
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
  