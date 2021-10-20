package de.kunz.scraping.data.querying;

import java.util.*;

public interface IFilter<T> {
	
	boolean filter(T queryable);
	
	void filter(List<T> queryables);

}
