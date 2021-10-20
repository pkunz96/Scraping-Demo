package de.kunz.scraping.data.querying;

import java.util.*;

public interface IDatasource<T extends Queryable> {
	
	List<T> executeQuery(IQuery<T> query) 
			throws QueryingException;
	
	boolean supports(IQuery<T> query);
	
}
 