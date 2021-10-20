package de.kunz.scraping.util.graph;

import java.util.*;

public interface GraphNode<T> {

	int getKey();
	
	T getValue();
	
	void setValue(T value);
	
	Set<GraphNode<T>> getAdjacentNodes();
	
	boolean isVisited();
	
	void setVisited(boolean visited);
	
}
