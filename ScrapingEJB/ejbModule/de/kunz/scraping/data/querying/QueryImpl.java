package de.kunz.scraping.data.querying;

import java.util.*;

class QueryImpl<T extends Queryable> implements IQuery<T> {

	private Class<T> queriedType;
	private IPredicate<T> predicate;
	private IFilter<T> filter;
	
	private final List<IDatasource<T>> datasourceList;

	public QueryImpl() {
		super();
		this.datasourceList = new LinkedList<>();
	}

	@Override
	public Class<T> getQueriedType() {
		return this.queriedType;
	}

	void setQueriedType(Class<T> queriedType) {
		this.queriedType = queriedType;
	}
	
	@Override
	public IPredicate<T> getPredicate() {
		return this.predicate;
	}

	void setPredicate(IPredicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	public List<IDatasource<T>> getDatasources() {
		return new LinkedList<>(this.datasourceList);
	}

	void addDatasource(IDatasource<T> datasource) {
		this.datasourceList.add(datasource);
	}

	@Override
	public List<T> execute() 
			throws QueryingException {
		final List<T> resultList = new LinkedList<>();
		try {
			for(IDatasource<T> datasource : this.datasourceList) {
				final List<T> datasourceResult = datasource.executeQuery(this);
				if(datasourceResult != null) {
					resultList.addAll(datasourceResult);
				}
			}
		}catch(Exception e0) {
			e0.printStackTrace();
			throw new QueryingException(e0);
		}
		return resultList;
	}
 
	@Override
	public IFilter<T> getFilter() {
		return filter;
	} 
	
	void setFilter(IFilter<T> filter) {
		this.filter = filter;
	}
}
 