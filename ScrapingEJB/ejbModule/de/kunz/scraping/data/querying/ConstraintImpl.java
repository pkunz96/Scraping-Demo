package de.kunz.scraping.data.querying;

class ConstraintImpl<T extends Queryable> implements IConstraint<T>{

	private IAttribute id;
	
	private String value;
	
	private Relation relation;
	
	public ConstraintImpl() {
		super();
	}

	@Override
	public IAttribute getId() {
		return this.id;
	}
	
	void setId(IAttribute id) {
		this.id = id;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}

	void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public Relation getRelation() {
		return this.relation;
	}
	
	void setRelation(Relation relation) {
		this.relation = relation;
	}
}
