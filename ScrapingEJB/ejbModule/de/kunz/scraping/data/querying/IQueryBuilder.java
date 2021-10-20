package de.kunz.scraping.data.querying;


public interface IQueryBuilder<T extends Queryable> {
	
	public static <V extends Queryable> IQueryBuilder<V> getInstance(Class<V> quriedType) {
		return new QueryBuilderImpl<>(quriedType);
	}
	
	IQueryBuilder<T> addDatasource(IDatasource<T> datasource);
		
	IQueryBuilder<T> startPredicate(LogicalConnective connective);
	
	IQueryBuilder<T> closePredicate();

	IQueryBuilder<T> addConstraint(IAttribute attribute, String value, Relation relation);

	IQuery<T> getQuery();	
}
	

