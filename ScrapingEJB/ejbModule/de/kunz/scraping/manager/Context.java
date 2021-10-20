package de.kunz.scraping.manager;

import java.beans.*;
import java.util.*;
import java.util.concurrent.*;

import org.jboss.resteasy.logging.Logger;

import com.fasterxml.jackson.annotation.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.data.querying.*;
import de.kunz.scraping.mapping.*;
import de.kunz.scraping.reduction.*;
import de.kunz.scraping.sourcing.*;
import de.kunz.scraping.synchronization.*;
import de.kunz.scraping.util.*;
import de.kunz.scraping.util.concurrent.AsyncReport;
import de.kunz.scraping.util.concurrent.AsyncReport.AsyncJobState;


class Context implements PropertyChangeListener, IContext {
	
	private static final class ScrapingEvent {

		public enum Type {
			DONE, FAILED, IGNORED;
		}
		
		private Type type;
		
		private String operationName;
		
		private IQuery<Broker> query;
		
		private Broker broker;
		
		private List<Broker> result;
		
		private Exception cause;
		
		private Context context;
		
		public static boolean isScrapingEvent(PropertyChangeEvent changeEvent) {
			@SuppressWarnings("rawtypes")
			final AsyncReport asyncReport = (AsyncReport) changeEvent.getSource();
			if(asyncReport == null) {
				return false;
			}
			else {
				final Object changedObject = changeEvent.getNewValue();
				if(!(changedObject instanceof AsyncJobState)) {
					return false;
				}
				else {
					final AsyncJobState newState = (AsyncJobState) changedObject;
					if((AsyncJobState.IN_PROGRESS.equals(newState)) 
							|| AsyncJobState.IS_PENDING.equals(newState)) {
						return false;
					}
					else {
						return true;
					}
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public ScrapingEvent(PropertyChangeEvent changeEvent, Context context) {
			if(!isScrapingEvent(changeEvent)) {
				throw new IllegalArgumentException();
			}
			else {
				this.context = context;
				@SuppressWarnings("rawtypes")
				final AsyncReport asyncReport = (AsyncReport) changeEvent.getSource();
				this.operationName = asyncReport.getOperationName();
				final AsyncJobState asyncJobState = (AsyncJobState)changeEvent.getNewValue();
				switch(this.operationName) {
					case OPERATION_NAME_FIND: {
						this.query = (IQuery<Broker>) asyncReport.getArgument();
						switch(asyncJobState) {
							case DONE: {
								this.type = Type.DONE;
								this.result = (List<Broker>) asyncReport.getResult();
								break;
							}
							case IS_FAILED: {
								this.type = Type.FAILED;
								this.cause = asyncReport.getCause();
								break;
							}
							case INGNORED: {
								this.type = Type.IGNORED;
								break;	
							}
							default: {
								throw new IllegalStateException();
							}
						}
						break;
					}
					case OERATION_NAME_MAP: {
						this.broker = (Broker) asyncReport.getArgument();
						switch(asyncJobState) {
							case DONE: {
								this.type = Type.DONE;
								this.result = new LinkedList<>();
								this.result.add((Broker) asyncReport.getResult());
								break;
							}
							case IS_FAILED: {
								this.type = Type.FAILED;
								this.cause = asyncReport.getCause();
								break;
							}
							case INGNORED: {
								this.type = Type.IGNORED;
								break;
							}
							default: {
								throw new IllegalStateException();
							}
						}
						break;
					}
					case OPERATION_NAME_REDUCE: {
						switch(asyncJobState) {
							case DONE: {
								this.type = Type.DONE;
								this.result = new LinkedList<>();
								final Iterator<Broker> brokerIterator = (Iterator<Broker>) asyncReport.getResult();
								while(brokerIterator.hasNext()) {
									this.result.add(brokerIterator.next());
								}
								break;
							}
							case IS_FAILED: {
								this.type = Type.FAILED;
								this.cause = asyncReport.getCause();
								break;
							}
							case INGNORED: {
								this.type = Type.IGNORED;
								break;
							}
							default: {
								throw new IllegalStateException();
							}
						}
						break;
					}
					case OPERATION_NAME_SYNC: {
						switch(asyncJobState) {
							case DONE: {
								this.type = Type.DONE;
								this.result = new LinkedList<>();
								this.result.add((Broker) asyncReport.getResult());
								break;
							}
							case IS_FAILED: {
								this.type = Type.FAILED;
								this.cause = asyncReport.getCause();
								break;
							}
							case INGNORED: {
								this.type = Type.IGNORED;
								break;
							}
							default: {
								throw new IllegalStateException();
							}
						}
						break;
					}
					default: {
						throw new IllegalStateException();
					}
				}
			}
		}
		
		public Type getType() {
			return this.type;
		}

		public String getOperationName() {
			return this.operationName;
		}

		public IQuery<Broker> getQuery() {
			return this.query;
		}

		public Broker getBroker() {
			return this.broker;
		}

		public List<Broker> getResult() {
			return this.result;
		}

		public Exception getCause() {
			return this.cause;
		}

		public Context getContext() {
			return this.context;
		}
		
	}
	
	
	private abstract static class AbstractScrapingEventHandler {

		public static AbstractScrapingEventHandler getInstance() {
			return new FindOperationEventHandler(new MapOperationEventHandler(new ReduceOperationEventHandler(new SyncOperationEventHandler(null))));
		}
			
		private AbstractScrapingEventHandler successor;
		
		protected AbstractScrapingEventHandler(AbstractScrapingEventHandler successor) {
			this.successor = successor;
		}
		 
		public final void handle(ScrapingEvent event) {
			handle(event, true);
		}
		
		private void handle(ScrapingEvent event, boolean lock) {
			if(event == null) {
				throw new NullPointerException();
			}
			else {
				final Context context = event.getContext();
				if(lock && (context != null)) {
					while(!context.lock());
				}
				if(this.supports(event.getType()) 
						&& this.supports(event.getOperationName())) {
					this.onScrapingEvent(event);
				}
				else if(successor != null){
					this.successor.handle(event, false);
				}
				if(lock && (context != null)) {
					context.unlock();
				}
			}
		}
		
		protected abstract boolean supports(ScrapingEvent.Type type);
		
		protected abstract boolean supports(String operation);
		
		protected abstract void onScrapingEvent(ScrapingEvent event);
		
	}
	
	private static class FindOperationEventHandler extends AbstractScrapingEventHandler {

		public FindOperationEventHandler(AbstractScrapingEventHandler successor) {
			super(successor);
		}

		@Override
		protected void onScrapingEvent(ScrapingEvent event) {
			final ScrapingEvent.Type type = event.getType();
			final Context context = event.getContext();
			switch(type) {
				case DONE: {
					for(Broker broker : event.getResult()) {
						context.increaseMapTaskCount();
						context.map(broker);
					}
					context.decreaseFindTaskCount();
					break;
				}
				case FAILED: {
					Context.logFailedOperation(context.getContextId(), event.getOperationName(), event.getQuery(), event.getCause());
					if(context.getFindTaskCount() == 1 && context.getMapTaskCount() == 0) {
						context.increaseReduceTakCount();
						context.reduce();
					}
					context.decreaseFindTaskCount();
					break;
				}
				case IGNORED: {
					Context.logIngoredArgument(context.getContextId(), event.getOperationName(), event.getQuery());
					if(context.getFindTaskCount() == 1 && context.getMapTaskCount() == 0) {
						context.increaseReduceTakCount();
						context.reduce();
					}
					context.decreaseFindTaskCount();
					break;
				} 
				default: {
					throw new IllegalStateException();
				}
			}
		}

		@Override
		public boolean supports(ScrapingEvent.Type type) {
			return true;
		}

		@Override
		public boolean supports(String operation) {
			return OPERATION_NAME_FIND.equals(operation);
		}
		
	}

	private static class MapOperationEventHandler extends AbstractScrapingEventHandler {

		public MapOperationEventHandler(AbstractScrapingEventHandler successor) {
			super(successor);
		}

		@Override
		protected boolean supports(ScrapingEvent.Type type) {
			return true;
		}

		@Override
		protected boolean supports(String operation) {
			return OERATION_NAME_MAP.equals(operation);
		}

		@Override
		protected void onScrapingEvent(ScrapingEvent event) {
			final ScrapingEvent.Type type = event.getType();
			final Context context = event.getContext();
			switch(type) {
				case DONE: {
					final Broker broker = event.getBroker();
					if(broker != null) {
						context.add(broker);
					}
					if((context.getFindTaskCount() == 0) && (context.getMapTaskCount() == 1)) {
						context.increaseReduceTakCount();
						context.reduce();
					} 
					context.decreaseMapTaskCount();
					break;
				}
				case FAILED: {
					Context.logFailedOperation(context.getContextId(), event.getOperationName(), event.getBroker(), event.getCause());
					if((context.getFindTaskCount() == 0) && (context.getMapTaskCount() == 1)) {
						context.increaseReduceTakCount();
						context.reduce();
					} 
					context.decreaseMapTaskCount();
					break;
				}
				case IGNORED: {
					Context.logIngoredArgument(context.getContextId(), event.getOperationName(), event.getBroker());
					if((context.getFindTaskCount() == 0) && (context.getMapTaskCount() == 1)) {
						context.increaseReduceTakCount();
						context.reduce();
					} 
					context.decreaseMapTaskCount();
					break; 
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	}

	private static class ReduceOperationEventHandler extends AbstractScrapingEventHandler {

		public ReduceOperationEventHandler(AbstractScrapingEventHandler successor) {
			super(successor);
		}

		@Override
		protected boolean supports(ScrapingEvent.Type type) {
			return true;
		}

		@Override
		protected boolean supports(String operation) {
			return OPERATION_NAME_REDUCE.equals(operation);
		}

		@Override
		protected void onScrapingEvent(ScrapingEvent event) {
			final ScrapingEvent.Type type = event.getType();
			final Context context = event.getContext();
			switch(type) {
				case DONE: {					
					final List<Broker> brokerList = event.getResult();					
					if(IContext.QUERY_RUNNING.equals(context.getState()) && context.getReduceTaskCount() == 1) {
						for(Broker broker : brokerList) {
							context.addBroker(broker);
						}
						context.decreaseReduceTaskCount();
						context.done();
					}
					else if(IContext.UPDATE_RUNNING.equals(context.getState()) && context.getReduceTaskCount() == 1) {
						for(Broker broker : brokerList) {
							context.increaseSyncTaksCount();
							context.sync(broker);
						}
						context.decreaseReduceTaskCount();
					}
					break;
				}
				case FAILED: {
					Context.logFailedOperation(context.getContextId(), event.getOperationName(), event.getCause());
					context.failed();
					break;
				}
				case IGNORED: {
					Context.logIngoredArgument(context.getContextId(), event.getOperationName());
					context.failed();
					break;
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	}

	private static class SyncOperationEventHandler extends AbstractScrapingEventHandler {

		public SyncOperationEventHandler(AbstractScrapingEventHandler successor) {
			super(successor);
		}

		@Override
		protected boolean supports(ScrapingEvent.Type type) {
			return true;
		}

		@Override
		protected boolean supports(String operation) {
			return OPERATION_NAME_SYNC.equals(operation);
		}

		@Override
		protected void onScrapingEvent(ScrapingEvent event) {
			final ScrapingEvent.Type type = event.getType();
			final Context context = event.getContext();
			switch(type) {
				case DONE: {					
					if(context.getSyncTaskCount() == 1) {
						context.done();
					}
					context.decreaseSyncTasksCount();
					break;
				}
				case FAILED: {
					Context.logFailedOperation(context.getContextId(), event.getOperationName(), event.getBroker(), event.getCause());
					if(context.getSyncTaskCount() == 1) {
						context.done();
					}
					context.decreaseSyncTasksCount();
					break;
				}
				case IGNORED: {
					Context.logIngoredArgument(context.getContextId(), event.getOperationName(), event.getBroker());
					if(context.getSyncTaskCount() == 1) {
						context.done();
					} 
					context.decreaseSyncTasksCount();
					break;
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	}

	
	private static final String OPERATION_NAME_FIND = "find";
	
	private static final String OERATION_NAME_MAP = "map";
	
	private static final String OPERATION_NAME_REDUCE = "reduce";
	
	private static final String OPERATION_NAME_SYNC = "sync";
	
	
	private static final Logger LOG = Logger.getLogger(Context.class);
	
	private static void logFailedOperation(long contextID, String operationName, IQuery<Broker> argument, Exception exception) {
		LOG.error("During the execution of " + objToString(operationName) + " with argument " + objToString(argument) +" in the context of " + Long.toString(contextID) + "wan error occured. Exception: " + objToString(exception));
	}
	
	private static void logFailedOperation(long contextID, String operationName, Broker argument, Exception exception) {
		LOG.error("During the execution of " + objToString(operationName) + " with argument " + objToString(argument) +" in the context of " + Long.toString(contextID) + "wan error occured. Exception: " + objToString(exception));
	}
	
	private static void logFailedOperation(long contextID, String operationName, Exception exception) {
		LOG.error("During the execution of " + objToString(operationName) + " in the context of " + Long.toString(contextID) + "an error occured. Exception: " + objToString(exception));
	}
	
	private static void logIngoredArgument(long contextID, String operationName, IQuery<Broker> argument) {
		LOG.warn("The invocation of " + objToString(operationName) + " in the context of " + Long.toString(contextID) + " was ingored for the argument " + objToString(argument) + ".");
	}
	
	private static void logIngoredArgument(long contextID, String operationName, Broker argument) {
		LOG.warn("The invocation of " + objToString(operationName) + " in the context of " + Long.toString(contextID) + " was ingored for the argument " + objToString(argument) + ".");
	}
	
	
	private static void logIngoredArgument(long contextID, String operationName) {
		LOG.warn("The invocation of " + objToString(operationName) + " in the context of " + Long.toString(contextID) + " was ingored.");
	}
	
	private static String objToString(Object obj) {
		if(obj == null) {
			return "null";
		}
		else {
			return obj.toString();
		}
	}
	
	
	private static int EXCLUSIVE_ACCESS = 1;
	
	
	private static List<State> getUpdateStates() {
		final List<State> updateStates = new LinkedList<>();
		updateStates.add(UPDATE_RUNNING);
		updateStates.add(UPDATE_DONE);
		updateStates.add(UPDATE_FAILED);
		updateStates.add(UPDATE_ABORTED);
		return updateStates;
	}
	
	private static List<State> getQueryStates() {
		final List<State> queryStates = new LinkedList<>();
		queryStates.add(QUERY_RUNNING);
		queryStates.add(QUERY_DONE);
		queryStates.add(QUERY_FAILED);
		queryStates.add(QUERY_ABORTED);
		return queryStates;
	}
	
	public static long getNextContextId() {
		synchronized(Context.class) {
			return System.currentTimeMillis();
		}
	}
	
	
	private final long contextId;

	private final Semaphore semaphore;
	
	private final List<Broker> queryResultList;
	
	private long completionTime;
	
	private String displayName;
	
	private final SearchEJBLocal searchEJB;
	
	private final ReducerEJBLocal reducerEJB;
	
	private final MappingEJBLocal mappingEJB;
	
	private final SynchronizerEJBLocal synchronizerEJB;
	
	private final Map<String, Long> taskCountMap;
	
	private LinearStateMaschine stateMachine;
	
	
	public Context(SearchEJBLocal searchEJB, ReducerEJBLocal reducerEJB, MappingEJBLocal mappingEJB, SynchronizerEJBLocal synchronizerEJB) {
		this.contextId = getNextContextId();
		this.semaphore = new Semaphore(EXCLUSIVE_ACCESS);
		final List<State> stateList = new LinkedList<>();
		stateList.add(INITIAL);
		this.stateMachine = new LinearStateMaschine(stateList, IContext.INITIAL);
		this.queryResultList = new LinkedList<>();
		this.completionTime = -1;
		this.taskCountMap = new ConcurrentHashMap<>();
		{
			this.taskCountMap.put(OPERATION_NAME_FIND, 0L);
			this.taskCountMap.put(OERATION_NAME_MAP, 0L);
			this.taskCountMap.put(OPERATION_NAME_REDUCE, 0L);
			this.taskCountMap.put(OPERATION_NAME_SYNC, 0L);
		}
		this.searchEJB = searchEJB;
		this.reducerEJB = reducerEJB;
		this.mappingEJB = mappingEJB;
		this.synchronizerEJB = synchronizerEJB;
	}
	
	
	@Override
	public long getContextId() {
		return contextId;
	} 

	@Override
	@JsonIgnore
	public State getState() {
		return this.stateMachine.getState();
	}
	
	public long getStateID() {
		return this.stateMachine.getState().getStateID();
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public long getCompletionTime() {
		return this.completionTime;
	}
	
	@Override
	public List<Broker> getQueryResultList() {
		this.stateMachine.assureIsEqual(QUERY_DONE);
		return this.queryResultList;
	}
	
	@Override
	public void abort() {
		final long currentTime = System.currentTimeMillis();
		final State curState = this.stateMachine.getState();
		if(QUERY_RUNNING.equals(curState)) {
			this.stateMachine.setState(QUERY_ABORTED);
			this.completionTime = currentTime;
		}
		else if(UPDATE_RUNNING.equals(curState)) {
			this.stateMachine.setState(UPDATE_ABORTED);
			this.completionTime = currentTime;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(ScrapingEvent.isScrapingEvent(evt)) {
			final AbstractScrapingEventHandler handler = AbstractScrapingEventHandler.getInstance();
			handler.handle(new ScrapingEvent(evt, this));
		}
	}
	
	@Override 
	public String toString() {
		return "ContextImpl [contextId=" + contextId + ", stateMachine=" + stateMachine + ", queryResultList="
				+ queryResultList + ", completionTime=" + completionTime + ", displayName=" + displayName
				+ ", taskCountMap=" + taskCountMap + "]";
	}
	
	
	void update(List<IQuery<Broker>> queries) {
		try {
			while(!this.lock());
			if(queries == null) {
				throw new NullPointerException();
			}
			else if(!IContext.INITIAL.equals(this.getState())) {
				throw new IllegalArgumentException();
			}
			else {
				this.updating();
				boolean findTasksDispatched = false;
				for(IQuery<Broker> query: queries) {
					this.increaseFindTaskCount(); 
					this.find(query);
					findTasksDispatched = true; 
				}	 
				if(!findTasksDispatched) {
					this.done(); 
				} 
			}
		} finally {
			this.unlock();
		}
	}
	
	void find(List<IQuery<Broker>> queries) {
		try { 
			while(!this.lock());
			if(queries == null) {
				throw new NullPointerException();
			}
			else if(!IContext.INITIAL.equals(this.getState())) {
				throw new IllegalStateException();
			}
			else {
				this.querying();
				boolean findTasksDispatched = false;
				for(IQuery<Broker> query: queries) {
					this.increaseFindTaskCount(); 
					this.find(query);
					findTasksDispatched = true; 
				}	 
				if(!findTasksDispatched) {
					this.done(); 
				}
			} 
		} finally {
			this.unlock();
		}
	}
	
	
	private long getFindTaskCount() {
		return this.taskCountMap.get(OPERATION_NAME_FIND);
	}
	
	private void increaseFindTaskCount() {
		System.out.println(this.taskCountMap);

		this.taskCountMap.put(OPERATION_NAME_FIND, this.taskCountMap.get(OPERATION_NAME_FIND) + 1);
	}
	
	private void decreaseFindTaskCount() {
		System.out.println(this.taskCountMap);

		this.taskCountMap.put(OPERATION_NAME_FIND, this.taskCountMap.get(OPERATION_NAME_FIND) - 1);
	}
	
	private long getMapTaskCount() {
		return this.taskCountMap.get(OERATION_NAME_MAP);
	}
	
	private void increaseMapTaskCount() {
		this.taskCountMap.put(OERATION_NAME_MAP, this.taskCountMap.get(OERATION_NAME_MAP) + 1);
	}
	
	private void decreaseMapTaskCount() {
		this.taskCountMap.put(OERATION_NAME_MAP, this.taskCountMap.get(OERATION_NAME_MAP) - 1);
	}
	
	private long getReduceTaskCount() {
		return this.taskCountMap.get(OPERATION_NAME_REDUCE);
	}
	
	private void increaseReduceTakCount() {
		this.taskCountMap.put(OPERATION_NAME_REDUCE, this.taskCountMap.get(OPERATION_NAME_REDUCE) + 1);
	}
	
	private void decreaseReduceTaskCount() {
		this.taskCountMap.put(OPERATION_NAME_REDUCE, this.taskCountMap.get(OPERATION_NAME_REDUCE) - 1);
	}
	
	private long getSyncTaskCount() {
		return this.taskCountMap.get(OPERATION_NAME_SYNC);
	}
	
	private void increaseSyncTaksCount() {
		this.taskCountMap.put(OPERATION_NAME_SYNC, this.taskCountMap.get(OPERATION_NAME_SYNC) + 1);
	}
	
	private void decreaseSyncTasksCount() {
		this.taskCountMap.put(OPERATION_NAME_SYNC, this.taskCountMap.get(OPERATION_NAME_SYNC) - 1);
	}
	
	private void addBroker(Broker broker) {
		this.stateMachine.assureIsEqual(QUERY_RUNNING);
		if(broker == null) {
			throw new NullPointerException();
		}
		else {
			this.queryResultList.add(broker);
		}
	}
	
	private void querying() {
		final State curState = this.stateMachine.getState();
		if(INITIAL.equals(curState)) {
			this.stateMachine = new LinearStateMaschine(Context.getQueryStates(), IContext.QUERY_RUNNING);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	private void updating() {
		final State curState = this.stateMachine.getState();
		if(INITIAL.equals(curState)) {
			this.stateMachine = new LinearStateMaschine(Context.getUpdateStates(), IContext.UPDATE_RUNNING);
		}
		else {
			throw new IllegalStateException();
		}
	}

	private void failed() {
		final long currentTime = System.currentTimeMillis();
		final State curState = this.stateMachine.getState();
		if(QUERY_RUNNING.equals(curState)) {
			this.stateMachine.setState(QUERY_FAILED);
			this.completionTime = currentTime;
		}
		else if(UPDATE_RUNNING.equals(curState)) {
			this.stateMachine.setState(UPDATE_FAILED);
			this.completionTime = currentTime;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	private void done() {
		final long currentTime = System.currentTimeMillis();
		final State curState = this.stateMachine.getState();
		if(QUERY_RUNNING.equals(curState)) {
			this.stateMachine.setState(QUERY_DONE);
			this.completionTime = currentTime;
		}
		else if(UPDATE_RUNNING.equals(curState)) {
			this.stateMachine.setState(UPDATE_DONE);
			this.completionTime = currentTime;
		}
		else {
			throw new IllegalStateException();
		}
	}

	private boolean lock() {
		boolean locked = false;
		try { 
			this.semaphore.acquire();
			locked = true;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return locked;
	} 
	  
	private void unlock() {
		this.semaphore.release();
	}
	
	// The following methods are used to access EJB functionality in context of this context object. 
	
	private void find(IQuery<Broker> query) {
		this.searchEJB.find(query, this, OPERATION_NAME_FIND, this.contextId);
	}
	
	private void map(Broker broker) {
		this.mappingEJB.map(broker, this, OERATION_NAME_MAP, this.contextId);
	}

	private void add(Broker broker) {
		this.reducerEJB.add(broker, this.contextId);
	}
	
	private void reduce() {
		this.reducerEJB.reduce(this, OPERATION_NAME_REDUCE, this.contextId);
	}
	
	private void sync(Broker broker) {
 		this.synchronizerEJB.sync(broker, this, OPERATION_NAME_SYNC, this.contextId);
 	}
}
   

