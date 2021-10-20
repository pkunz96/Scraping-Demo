package de.kunz.scraping.manager;

import java.util.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.util.*;

public interface IContext {

	public final State INITIAL = new State("INITIAL", 1);
	
	public final State UPDATE_RUNNING = new State("UPDATE_RUNNING", 2);
	
	public final State UPDATE_DONE = new State("UPDATE_DONE", 3);
	
	public final State UPDATE_FAILED = new State("UPDATE_FAILED", 4);
	
	public final State UPDATE_ABORTED = new State("UPDATE_ABORTED", 5);
	
	public final State QUERY_RUNNING = new State("QUERYING", 6);
	
	public final State QUERY_DONE = new State("QUERY_DONE", 7);
	
	public final State QUERY_FAILED = new State("QUERY_FAILED", 8);
	
	public final State QUERY_ABORTED = new State("QUERY_ABORTED", 9); 

	long getContextId();

	State getState(); 
  
	String getDisplayName();

	void setDisplayName(String displayName);
	 
	long getCompletionTime();

	List<Broker> getQueryResultList();

	void abort();

}  