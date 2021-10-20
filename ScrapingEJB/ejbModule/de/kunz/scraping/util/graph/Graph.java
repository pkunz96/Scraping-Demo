package de.kunz.scraping.util.graph;

import java.util.*;

public interface Graph<T> {
	
	GraphNode<T> addNode();
	
	GraphNode<T> getNode(int key);

	GraphNode<T> removeNode(int key);

	boolean addEdge(int firstKey, int secKey);
	
	boolean removeEdge(int firstKey, int secKey);

	double getEdgeWeight(int firstKey, int secKey);
	
	Set<GraphNode<T>> getEntryPoints();
	
	Iterator<GraphNode<T>> getIterator();
	
	void lock();
	
	void unlock();
	
	void clearVisitedMarks();
}	
 