package de.kunz.scraping.util.graph;

import java.util.*;

class UndirectedGraphNode<T> implements GraphNode<T> {
	
	private final int key;
	
	private Set<UndirectedGraphNode<T>> adjacentNodeSet;
	
	private T value;
	
	private boolean visited;
	
	public UndirectedGraphNode(int key) {
		super();
		this.key = key;
		this.adjacentNodeSet = new HashSet<>();
		this.value = null;
		this.visited = false;
	}

	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public Set<GraphNode<T>> getAdjacentNodes() {
		return Set.copyOf(this.adjacentNodeSet);
	}
	
	@Override
	public boolean isVisited() {
		return this.visited;
	}

	@Override
	public void setVisited(boolean visited) {
		this.visited = visited;		 
	}

	boolean removeAdjacentNode(UndirectedGraphNode<T> node)  {
		return this.adjacentNodeSet.remove(node);
	}
	
	boolean addAdjacentNode(UndirectedGraphNode<T> node) {
		return this.adjacentNodeSet.add(node);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key;
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
		@SuppressWarnings("rawtypes")
		UndirectedGraphNode other = (UndirectedGraphNode) obj;
		if (key != other.key)
			return false;
		return true;
	}

	Set<UndirectedGraphNode<T>> getAdjacentUndirectedGraphNode() {
		return Set.copyOf(this.adjacentNodeSet);
	}
	
}
