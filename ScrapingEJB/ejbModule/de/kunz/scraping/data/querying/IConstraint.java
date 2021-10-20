package de.kunz.scraping.data.querying;

public interface IConstraint<T extends Queryable> {
	
	IAttribute getId();
	
	String getValue();

	Relation getRelation();
	
}
