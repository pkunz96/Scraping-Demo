package de.kunz.scraping.data.querying;

import java.util.*;

class PredicateImpl<T extends Queryable> implements IPredicate<T> {

	private LogicalConnective connective;
	
	private final List<ConstraintImpl<T>> constraintList;
	private final List<PredicateImpl<T>> predicateList;
	
	public PredicateImpl() {
		super();
		this.constraintList = new LinkedList<>();
		this.predicateList = new LinkedList<>();
	}

	@Override
	public LogicalConnective getLogicalConnective() {
		return this.connective;
	}

	void setLogicalConnective(LogicalConnective  connective) {
		this.connective = connective;
	}
	
	@Override
	public List<IConstraint<T>> getConstraints() {
		return new LinkedList<>(this.constraintList);
	}

	@Override
	public List<IPredicate<T>> getSubPredicates() {
		return new LinkedList<>(this.predicateList);
	}

	void addConstrain(ConstraintImpl<T> constraint) {
		this.constraintList.add(constraint);
	}
	
	void addSubPredicate(PredicateImpl<T> subPredicate) {
		this.predicateList.add(subPredicate);
	}
}
 