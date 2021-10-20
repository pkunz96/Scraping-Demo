package de.kunz.scraping.util;

import java.util.*;

public class LinearStateMaschine {

	private final List<State> stateList;
	
	private volatile State curState;
	
	public LinearStateMaschine(List<State> stateList, State initialState) {
		if((stateList == null) || (initialState == null)) {
			throw new NullPointerException();
		}
		else if(!stateList.contains(initialState)) {
			throw new IllegalArgumentException();
		}
		this.stateList = stateList;
		this.curState = initialState;
	}
	
	public State getState() {
		return this.curState;
	}
	
	public void setState(State newState) {
		if(newState == null) {
			throw new NullPointerException(); 
		}
		else if(!stateList.contains(newState) 
				|| newState.getStateID() < this.curState.getStateID()) {
			throw new IllegalArgumentException();
		}
		this.curState = newState;
	}
	
	public void assureIsEqual(State state) {
		if(state == null) {
			throw new NullPointerException();
		}
		else if(this.curState.getStateID() != state.getStateID()) {
			throw new IllegalStateException();
		} 
	}

	public void assureIsEqual(List<State> stateList) {
		if(stateList == null) {
			throw new NullPointerException();
		}
		else {
			boolean match = false;
			for(State state: stateList) {
				if(this.curState.equals(state)) {
					match = true;
				}
			}
			if(!match) {
				throw new IllegalStateException();
			}
		}
	}
	
	public void assureIsMin(State state) {
		if(state == null) {
			throw new NullPointerException();
		}
		else if(this.curState.getStateID() < state.getStateID()) { 
			throw new IllegalStateException();
		}
	}
}
