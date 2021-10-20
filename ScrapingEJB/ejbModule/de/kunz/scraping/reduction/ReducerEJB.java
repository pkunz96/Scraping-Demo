package de.kunz.scraping.reduction;

import java.beans.*;
import java.util.*;
import java.util.concurrent.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;
import de.kunz.scraping.identification.*;
import de.kunz.scraping.util.concurrent.*;
import de.kunz.scraping.util.concurrent.AsyncReport.AsyncJobState;
import de.kunz.scraping.util.graph.*;

/**
 * Session Bean implementation class ReductionEJB
 */
@Stateful
@LocalBean
public class ReducerEJB implements ReducerEJBLocal {

	private static class Edge {
		
		private final int firstNode;
		
		private final int secondNode;
		
		public Edge(int firstNode, int secondNode) {
			this.firstNode = firstNode;
			this.secondNode = secondNode;
		}

		public int getFirstNodeKey() {
			return firstNode;
		}

		public int getSecondNodeKey() {
			return secondNode;
		}
	}
	
	private class MergingBrokerIterator implements Iterator<Broker> {

		private final Graph<Broker> brokerGraph;
		
		private final Iterator<GraphNode<Broker>> entryPointIterator;
		
		
		public MergingBrokerIterator(Graph<Broker> brokerGraph) {
			if(brokerGraph == null) {
				throw new NullPointerException();
			}
			this.brokerGraph = brokerGraph;
			this.entryPointIterator = brokerGraph.getEntryPoints().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return this.entryPointIterator.hasNext();
		}

		@Override
		public Broker next() {
			final Broker nextBroker;
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			else {
				final List<Broker> brokerList = new LinkedList<>();
				this.brokerGraph.lock();
				final Stack<GraphNode<Broker>> dfsStack = new Stack<>();
				{
					final GraphNode<Broker> entryPoint = this.entryPointIterator.next();
					entryPoint.setVisited(true);
					dfsStack.add(entryPoint);
				}
				while(!dfsStack.isEmpty()) {
					final GraphNode<Broker> curNode = dfsStack.pop();
					GraphNode<Broker> nextNode = null;
					for(GraphNode<Broker> adjacentNode : curNode.getAdjacentNodes()) {
						if(!adjacentNode.isVisited()) {
							adjacentNode.setVisited(true);
							nextNode = adjacentNode;
							break;
						}
					}
					if(nextNode == null) {
						brokerList.add(curNode.getValue());
					}
					else {
						nextNode.setVisited(true);
						dfsStack.push(nextNode);
					}
				}
				this.brokerGraph.unlock();
				final int FIRST_ELEMENT = 0;
				if(!brokerList.isEmpty()) {
					nextBroker = brokerList.get(FIRST_ELEMENT);
					brokerList.remove(FIRST_ELEMENT);
					brokerList.forEach(broker -> ReducerEJB.this.mergingBean.merge(nextBroker, broker));
				}
				else {
					nextBroker = null;
				}
			}
			return nextBroker;
		}
				
	}
	
	private final Map<Long, Queue<Broker>> contextIDBrokerCollectionMapping;
	
	@EJB
	private IdentityBeanLocal identityBean;
	
	@EJB
	private MergingEJBLocal mergingBean;
	
	public ReducerEJB() {
		this.contextIDBrokerCollectionMapping = new ConcurrentHashMap<>();
	}
	
	@Override
	public boolean add(Broker broker, long contextID) {		
		if(broker == null) {
			throw new NullPointerException();
		}
		else {
			final Queue<Broker> brokerCollection = getBrokerCollection(contextID);
			if(!brokerCollection.contains(broker)) {
				brokerCollection.add(broker);
			}
		}	
		return true;
	}
	
	@Override
	@Asynchronous
	public Future<AsyncReport<Void, Iterator<Broker>>> reduce(PropertyChangeListener listener, String operationName, long contextID) {
		final AsyncReport<Void, Iterator<Broker>> asyncReport = buildAsyncReport(listener, operationName, contextID);
		try {			
			final Queue<Broker> brokerQueue = getBrokerCollection(contextID);
			if(brokerQueue != null) {
				final Graph<Broker> brokerGraph = new UndirectedGraph<>();
				brokerGraph.lock();
				initBrokerGraph(brokerGraph, brokerQueue);
				while(!brokerQueue.isEmpty()) {
					final Set<GraphNode<Broker>> entryPoints = brokerGraph.getEntryPoints();	
					final GraphNode<Broker> curBrokerNode = insertBroker(brokerQueue, brokerGraph);
					final Set<Edge> newEdges = computeEdges(curBrokerNode, entryPoints);
					newEdges.forEach(edge -> brokerGraph.addEdge(edge.getFirstNodeKey(), edge.getSecondNodeKey()));
				}	
				brokerGraph.unlock();
				finalizeAsyncReport(asyncReport, this.new MergingBrokerIterator(brokerGraph));
			}
			else {
				finalizeAsyncReport(asyncReport, new IllegalArgumentException("No such context."));
			}
			
		} catch(Exception e0) {
			finalizeAsyncReport(asyncReport, e0);
		}
		this.contextIDBrokerCollectionMapping.remove(contextID);
		return new AsyncResult<AsyncReport<Void, Iterator<Broker>>>(asyncReport); 
	}	
	
	private AsyncReport<Void, Iterator<Broker>> buildAsyncReport(PropertyChangeListener listener, String operationName, long contextID) {
		AsyncReport<Void, Iterator<Broker>> asyncReport = new DefaultAsyncReport<>();
		asyncReport.setOperationName(operationName); 
		asyncReport.addPropertyChangeListener(listener);
		asyncReport.setContextId(contextID);
		return asyncReport;
	}
	
	private void finalizeAsyncReport(AsyncReport<Void, Iterator<Broker>> asyncReport, Iterator<Broker> result) {
		asyncReport.setResult(result);
		asyncReport.setState(AsyncJobState.DONE);
	}
	
	private void finalizeAsyncReport(AsyncReport<Void, Iterator<Broker>> asyncReport, Exception e0) {
		asyncReport.setChause(e0);
		asyncReport.setState(AsyncJobState.IS_FAILED);
	}
	
	private void initBrokerGraph(Graph<Broker> brokerGraph, Queue<Broker> brokerQueue) {
		if(!brokerQueue.isEmpty()) {
			final Broker broker = brokerQueue.poll();
			final GraphNode<Broker> brokerNode = brokerGraph.addNode();
			brokerNode.setValue(broker);
		}
	}
	
	private GraphNode<Broker> insertBroker(Queue<Broker> brokerQueue, Graph<Broker> brokerGrapher) {
		final GraphNode<Broker> graphNode;
		final Broker broker = brokerQueue.poll();
		if(broker != null) {
			graphNode = brokerGrapher.addNode();
			graphNode.setValue(broker);
		}
		else {
			throw new IllegalArgumentException();
		}
		return graphNode;
	}
	
	private Set<Edge> computeEdges(GraphNode<Broker> curBrokerNode, Set<GraphNode<Broker>> entryPoints) {
		final Set<Edge> newEdges = new HashSet<>();	
		for(GraphNode<Broker> entryPoint : entryPoints) {
			final Stack<GraphNode<Broker>> dfsStack = new Stack<>();
			entryPoint.setVisited(true);
			dfsStack.add(entryPoint);
			while(!dfsStack.isEmpty()) {
				final GraphNode<Broker> curNode = dfsStack.pop();
				GraphNode<Broker> nextNode = null;
				for(GraphNode<Broker> adjacentNode : curNode.getAdjacentNodes()) {
					if(!adjacentNode.isVisited()) {
						nextNode = adjacentNode;
						break;
					}
				}
				if(nextNode == null) {
					if(this.identityBean.isIdentic(curBrokerNode.getValue(), curNode.getValue())) {
						newEdges.add(new Edge(curNode.getKey(), curBrokerNode.getKey()));
					}
				}
				else {
					nextNode.setVisited(true);
					dfsStack.push(nextNode);
				}
			}
		}
		return newEdges;
	}
	
	private Queue<Broker> getBrokerCollection(long contextID) {
		final Queue<Broker> brokerCollection;
		if(this.contextIDBrokerCollectionMapping.containsKey(contextID)) {
			brokerCollection = this.contextIDBrokerCollectionMapping.get(contextID);
		}
		else {
			brokerCollection = new LinkedList<>();
			this.contextIDBrokerCollectionMapping.put(contextID, brokerCollection);
		}
		return brokerCollection;
	}  
	
}
