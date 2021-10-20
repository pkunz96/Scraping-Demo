package de.kunz.scraping.util.concurrent;

import java.beans.*;

public final class DefaultAsyncReport<T, V> implements AsyncReport<T, V>{

	private final PropertyChangeSupport changeSupport;
	
	private String operationName;
	
	private long contextId;
	
	private T argument;
	
	private V result;
		
	private AsyncJobState state;
	
	private int progress;
	
	private Exception cause;
	
	public DefaultAsyncReport() {
		super();
		this.operationName = null;
		this.changeSupport = new PropertyChangeSupport(this);
		this.state = AsyncJobState.IS_PENDING;
		this.progress = -1;
		this.cause = null;
	}
 	  
	@Override  
	public String getOperationName() {
		return operationName;
	}

	@Override
	public void setOperationName(String name) {
		final String oldName = this.operationName;
		this.operationName = name; 
		this.changeSupport.firePropertyChange("name", oldName, name);
	}
		
	@Override
	public long getContextId() {
		return contextId;
	}

	@Override
	public void setContextId(long contextId) {
		final long oldContextId = this.contextId;
		this.contextId = contextId;
		this.changeSupport.firePropertyChange("contextId", contextId, oldContextId);
	}
	
	@Override 
	public T getArgument() {
		return this.argument;
	}

	@Override
	public void setArgument(T argument) {
		final T oldArgument = this.argument;
		this.argument = argument;
		this.changeSupport.firePropertyChange("argument", oldArgument, argument);
	}

	@Override
	public V getResult() {
		return result;
	}
	
	@Override
	public void setResult(V result) {
		final V oldResult = this.result;
		this.result = result;
		this.changeSupport.firePropertyChange("result", oldResult, result);
	}
	
	@Override
	public AsyncJobState getState() {
		return state;
	}

	@Override
	public void setState(AsyncJobState state) {
		final AsyncJobState oldState = this.state;
		this.state = state;
		this.changeSupport.firePropertyChange("state", oldState, state);
		
	}

	@Override
	public int getProgress() {
		return progress;
	}

	@Override
	public void setProgress(int progress) {
		if(progress > 100) {
			throw new IllegalArgumentException();
		}
		final int oldProgress = this.progress;
		this.progress = progress;
		this.changeSupport.firePropertyChange("progress", oldProgress, progress);
	}

	@Override
	public Exception getCause() {
		return cause;
	}

	@Override
	public void setChause(Exception exception) {
		final Exception oldCause = this.cause;
		this.cause = exception;
		this.changeSupport.firePropertyChange("cause", oldCause, exception);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.changeSupport.removePropertyChangeListener(listener);
	}


}
