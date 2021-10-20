package de.kunz.scraping.util.graph;

import java.util.*;
import java.util.logging.*;
import java.util.concurrent.*;

public class UndirectedGraph<T> implements Graph<T> {

	private static class UndirectedGraphIterator<T> implements Iterator<UndirectedGraphNode<T>> {
		
		private final Set<UndirectedGraphNode<T>> todoSet;
		private final Set<UndirectedGraphNode<T>> doneSet;
		
		public UndirectedGraphIterator(Set<UndirectedGraphNode<T>> entryPoints) {
			if(entryPoints == null) {
				throw new NullPointerException();
			}
			else {
				this.todoSet = new HashSet<>(entryPoints);
				this.doneSet = new HashSet<>(); 
			}
		}
		
		@Override
		public boolean hasNext() {
			return !todoSet.isEmpty();
		}

		@Override
		public UndirectedGraphNode<T> next() {
			UndirectedGraphNode<T> nextNode = null;
			if(hasNext()) {
				nextNode = todoSet.iterator().next();
				for(UndirectedGraphNode<T> adjacentNode : nextNode.getAdjacentUndirectedGraphNode()) {
					if(!this.doneSet.contains(adjacentNode) 
							&& !this.todoSet.contains(adjacentNode)) {
						this.todoSet.add(adjacentNode);
					}
				}
				this.todoSet.remove(nextNode);
				this.doneSet.add(nextNode);
			}
			return nextNode;
		}
	}
	
	private static class GraphIteratorAdapter<T> implements Iterator<GraphNode<T>> {

		private final UndirectedGraphIterator<T> iterator;
		
		public GraphIteratorAdapter(UndirectedGraphIterator<T> iterator) {
			if(iterator == null) {
				throw new NullPointerException();
			}
			else {
				this.iterator = iterator;
			}
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public GraphNode<T> next() {
			return iterator.next();
		}
	}
	
	private static final Logger LOG = Logger.getAnonymousLogger();
	
	private final Semaphore semaphore;

	private final Set<UndirectedGraphNode<T>> entryPoints;
	
	private int curKey;

	public UndirectedGraph() {
		final int EXCLUSIVE_ACCESS = 1;
		this.semaphore = new Semaphore(EXCLUSIVE_ACCESS);
		this.entryPoints = new HashSet<>();
	}

	@Override
	public GraphNode<T> addNode() {
		final UndirectedGraphNode<T> newNode = new UndirectedGraphNode<>(curKey++);
		this.entryPoints.add(newNode);
		return newNode;
	}

	@Override
	public GraphNode<T> getNode(int key) {
		return this.getUndirectedGraphNode(key);
	}

	@Override
	public GraphNode<T> removeNode(int key) {
		final UndirectedGraphNode<T> node = getUndirectedGraphNode(key);
		if(node != null) {
			final Set<UndirectedGraphNode<T>> adjacentNodeList = node.getAdjacentUndirectedGraphNode();
			this.entryPoints.remove(node);
			for(UndirectedGraphNode<T> adjacentNode : adjacentNodeList) {
				adjacentNode.removeAdjacentNode(node);
				node.removeAdjacentNode(node);
			}
			for(UndirectedGraphNode<T> adjacentNode : adjacentNodeList) {
				final int adjacentNodeKey = adjacentNode.getKey();
				if(getUndirectedGraphNode(adjacentNodeKey) == null) {
					this.entryPoints.add(adjacentNode);
				}
			}
		}
		return node;
	}

	@Override
	public boolean addEdge(int firstKey, int secKey) {
		boolean added = false;
		final UndirectedGraphNode<T> firstNode = getUndirectedGraphNode(firstKey);
		final UndirectedGraphNode<T> secondNode = getUndirectedGraphNode(secKey);
		if((firstNode != null)
				&& (secondNode != null)) {		
			this.entryPoints.remove(firstNode);
			this.entryPoints.remove(secondNode);
			added = firstNode.addAdjacentNode(secondNode) 
					&& secondNode.addAdjacentNode(firstNode);
			if(getUndirectedGraphNode(firstKey) == null) {
				this.entryPoints.add(firstNode);
			}
		}
		return added;
	}

	@Override
	public boolean removeEdge(int firstKey, int secKey) {
		boolean removed = false;
		final UndirectedGraphNode<T> firstNode = getUndirectedGraphNode(firstKey);
		final UndirectedGraphNode<T> secondNode = getUndirectedGraphNode(secKey);
		if((firstNode != null)
				&& (secondNode != null)) {
			removed = firstNode.removeAdjacentNode(secondNode) 
					&& secondNode.removeAdjacentNode(firstNode);
			if(getUndirectedGraphNode(firstKey) == null) {
				this.entryPoints.add(firstNode);
			}
			if(getUndirectedGraphNode(secKey) == null) {
				this.entryPoints.add(secondNode);
			}
		}
		return removed;
	}

	@Override
	public double getEdgeWeight(int firstKey, int secKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<GraphNode<T>> getEntryPoints() {
		return Set.copyOf(this.entryPoints);
	}

	@Override
	public Iterator<GraphNode<T>> getIterator() {
		return new GraphIteratorAdapter<>(new UndirectedGraphIterator<>(this.entryPoints));
	}
	
	@Override
	public void lock() {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e0) {
			LOG.log(Level.FINEST, e0.toString());
		}
	}

	@Override
	public void unlock() {
		semaphore.release();
	}
	  
	@Override
	public void clearVisitedMarks() {
		this.getIterator().forEachRemaining((node) -> node.setVisited(false));
	}

	private UndirectedGraphNode<T> getUndirectedGraphNode(int key) {
		UndirectedGraphNode<T> node = null;
		final UndirectedGraphIterator<T> iterator = new UndirectedGraphIterator<T>(this.entryPoints);
		while(iterator.hasNext()) {
			UndirectedGraphNode<T> curNode = iterator.next();
			if(curNode.getKey() == key) {
				node = curNode;
				break;
			}
		}
		return node;
	}
	
	
	
}
