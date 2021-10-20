package de.kunz.scraping.util;

public class State {
	
	private final String name;
	private final int stateID;
	
	public State(String name, int stateID) {
		if(name == null) {
			throw new NullPointerException();
		}
		else if(stateID < 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.name = name;
			this.stateID = stateID;
		}
	}

	
	public String getName() {
		return name;
	}

	
	public int getStateID() {
		return stateID;
	}
	
}
