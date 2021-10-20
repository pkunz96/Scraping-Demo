package de.kunz.scraping.data.querying;

import java.util.*;

class QueryBuilderImpl<T extends Queryable> implements IQueryBuilder<T> {

	
	private static final String STACK_ELEMENT_QUERY = "query";
	private static final String STACK_ELEMENT_PREDICATE = "predicate";
	private static final String STACK_ELEMENT_COMPOSITE_PREDICATE = "composite_predicate";
	private static final String STACK_ELEMENT_CONSTRAINT_PREDICATE = "constraint_predicate";
	
	private final Stack<String> queryBuilderStack;
	private final Stack<PredicateImpl<T>> predicateStack;
	
	private final QueryImpl<T> query;

	private boolean datasourcesRequired;
	
	public QueryBuilderImpl(Class<T> queriedType) {
		super();
		if(queriedType == null) {
			throw new NullPointerException();
		}
		else {
			this.datasourcesRequired = true;
			this.queryBuilderStack = new Stack<>(); 
			{
				this.queryBuilderStack.push(STACK_ELEMENT_QUERY);
			}
			this.predicateStack = new Stack<>();
			this.query = new QueryImpl<>();
			{
				this.query.setQueriedType(queriedType); 
			}			
		}
	}

	@Override
	public IQueryBuilder<T> addDatasource(IDatasource<T> datasource) {
		if(datasource == null) {
			throw new NullPointerException();
		}
		else if(this.datasourcesRequired) {
			this.query.addDatasource(datasource);
			this.datasourcesRequired = false;
		}
		else {
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public IQueryBuilder<T> startPredicate(LogicalConnective connective) {
		if(connective == null) {
			throw new NullPointerException();
		}
		else if(datasourcesRequired) {
			throw new IllegalStateException();
		}
		else {
			PredicateImpl<T> newPredicate = new PredicateImpl<>();
			newPredicate.setLogicalConnective(connective);
			String curQueryBuilderStackElement = this.queryBuilderStack.pop();
			if(STACK_ELEMENT_QUERY.equals(curQueryBuilderStackElement)) {
				this.queryBuilderStack.push(STACK_ELEMENT_QUERY);
				this.queryBuilderStack.push(STACK_ELEMENT_PREDICATE);
				this.query.setPredicate(newPredicate);
				this.predicateStack.push(newPredicate);
			}
			else if(STACK_ELEMENT_PREDICATE.equals(curQueryBuilderStackElement) 
					|| STACK_ELEMENT_COMPOSITE_PREDICATE.equals(curQueryBuilderStackElement)) {
				this.queryBuilderStack.push(STACK_ELEMENT_COMPOSITE_PREDICATE);
				this.queryBuilderStack.push(STACK_ELEMENT_PREDICATE);
				PredicateImpl<T> curPredicate = this.predicateStack.pop();
				curPredicate.addSubPredicate(newPredicate);
				this.predicateStack.push(curPredicate);
				this.predicateStack.push(newPredicate);
			}
			else {
				throw new IllegalStateException();
			}
		}
		return this;
	}
 
	@Override
	public IQueryBuilder<T> closePredicate() {
		String curQueryBuilderStackElement = this.queryBuilderStack.pop();
		if(!STACK_ELEMENT_CONSTRAINT_PREDICATE.equals(curQueryBuilderStackElement) 
				&& !STACK_ELEMENT_COMPOSITE_PREDICATE.equals(curQueryBuilderStackElement)) {
			throw new IllegalStateException();
		}
		return this;
	}

	@Override
	public IQueryBuilder<T> addConstraint(IAttribute attribute, String value, Relation relation) {
		String curQueryBuilderStackElement = this.queryBuilderStack.pop();
		if((attribute == null) || (value == null) || (relation == null)) {
			throw new NullPointerException();
		}
		else if(!STACK_ELEMENT_PREDICATE.equals(curQueryBuilderStackElement) 
				&& !STACK_ELEMENT_CONSTRAINT_PREDICATE.equals(curQueryBuilderStackElement)) {		
			throw new IllegalStateException();
		}
		else if(!attribute.isValidValue(value) || !attribute.supports(relation)) {
			System.out.println(2 + ": " + value);
			this.queryBuilderStack.add(curQueryBuilderStackElement);

			//throw new IllegalArgumentException(); 
		}
		else {
			final ConstraintImpl<T> constraint = new ConstraintImpl<>();
			constraint.setId(attribute);
			constraint.setValue(value);
			constraint.setRelation(relation);
			PredicateImpl<T> curPredicate = this.predicateStack.pop();
			curPredicate.addConstrain(constraint);
			this.predicateStack.push(curPredicate);
			this.queryBuilderStack.push(STACK_ELEMENT_CONSTRAINT_PREDICATE);
		}
		return this;
	}

	@Override
	public IQuery<T> getQuery() {
		if((datasourcesRequired) || !STACK_ELEMENT_QUERY.equals(this.queryBuilderStack.pop())) {
			throw new IllegalStateException();
		}
		else {
			for(IDatasource<T> datasource : this.query.getDatasources()) {
				if(!datasource.supports(this.query)) {
					throw new IllegalStateException();
				}
			}
			query.setFilter(new FilterImpl<>());
			return this.query;
		}
	}
} 
