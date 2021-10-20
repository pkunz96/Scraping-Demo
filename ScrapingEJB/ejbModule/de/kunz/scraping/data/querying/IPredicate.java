package de.kunz.scraping.data.querying;

import java.util.*;

public interface IPredicate<T extends Queryable> {

	LogicalConnective getLogicalConnective();
	
	List<IConstraint<T>> getConstraints();
	
	List<IPredicate<T>> getSubPredicates();
}
