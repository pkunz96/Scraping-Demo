package de.kunz.scraping.util.concurrent;

import java.beans.*;

/**
 * Represents an ongoing asynchronious computation. A client may use this 
 * interface to fetch the result of an asynchronious computation.
 * 
 * @param <T> - the type of argument passed to asynchronious computation. 
 * @author Philipp Kunz
 */
public interface AsyncReport<T, V> {
	
	public enum AsyncJobState {
		IS_PENDING, IN_PROGRESS, DONE, INGNORED, IS_FAILED;
	}
	
	void setOperationName(String name);
	
	String getOperationName();
	
	void setContextId(long contextId);
	
	long getContextId();
	
	T getArgument();
		
	void setArgument(T argument);
		
	V getResult();
		
	void setResult(V result);
		
	AsyncJobState getState();
	
	void setState(AsyncJobState state);
	
	int getProgress();

	void setProgress(int progress);
	
	Exception getCause();

	void setChause(Exception exception);
	
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	void removePropertyChangeListener(PropertyChangeListener listener);
}
