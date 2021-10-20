package de.kunz.scraping.api.views;

import de.kunz.scraping.util.*;
import de.kunz.scraping.manager.*;

public class ContextView {
	
	private long queryID;
	 
	private String displayName;
	
	private int state;
	
	public ContextView(IContext context) {
		this.queryID = context.getContextId();
		this.displayName = context.getDisplayName();
		final State state = context.getState();
		if(state != null) {
			this.state = state.getStateID();
		} 
		else {
			this.state = -1;
		}
	}

	
	public long getQueryID() {
		return queryID;
	}

	
	public String getDisplayName() {
		return displayName;
	}

	
	public int getState() {
		return state;
	}

	
	public void setQueryID(long queryID) {
		this.queryID = queryID;
	}

	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	public void setState(int state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + (int) (queryID ^ (queryID >>> 32));
		result = prime * result + state;
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
		ContextView other = (ContextView) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (queryID != other.queryID)
			return false;
		if (state != other.state)
			return false;
		return true;
	}
	
}
 