package de.kunz.scraping.data.querying;

import java.util.*;

public interface IQuery<T extends Queryable> {
 	
	Class<T> getQueriedType();
	
	List<IDatasource<T>> getDatasources();
	
	IPredicate<T> getPredicate();
	
	List<T> execute()
			throws QueryingException;

	IFilter<T> getFilter();
}
