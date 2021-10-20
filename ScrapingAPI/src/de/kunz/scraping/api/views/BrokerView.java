package de.kunz.scraping.api.views;

import java.util.*;

import de.exp.ai.scraping.data.entity.*;

public class BrokerView {

	static class IDGeneratator implements Iterator<Long> {

		long curID = 0;
			
		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public Long next() {
			return curID++;
		}
		
	}
	
	private Long brokerID = null;

	private PersonView person = null;
	
	private BusinessView business = null;
	
	private Long datasourceID = null;
			
	private List<BusinessRelationView> businessRelations = new LinkedList<>();
	 
	private List<ConcessionView> concessions = new LinkedList<>();
	
	public BrokerView(Broker broker, boolean generateIDs) {
		final IDGeneratator idGenerator = initIDGenerator(generateIDs);
		{
			if(idGenerator == null) {
				this.brokerID = broker.getBrokerId();
			}
			else {
				this.brokerID = idGenerator.next();
			}
		}
		final Person person = broker.getPerson();
		final Business business = broker.getBusiness();
		final List<BrokerConcessionMapping> brokerConcessionMapping = broker.getBrokerConcessionMappingList();
		final List<BusinessRelation> businessRelations = broker.getComissioningBusinessRelationsList();
		final Datasource datasource = broker.getDatasource();
		{
			if(person != null) {
				this.person = new PersonView(person, idGenerator);
			}
			if(business  != null) {
				this.business = new BusinessView(business, idGenerator);
			}
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
			if(brokerConcessionMapping != null) {
				for(BrokerConcessionMapping mapping: brokerConcessionMapping) {
					this.concessions.add(new ConcessionView(mapping));
				}
			}
			if(businessRelations != null) {
				for(BusinessRelation businessRelation: businessRelations) {
					this.businessRelations.add(new BusinessRelationView(businessRelation, idGenerator));
				}
			}
		}
	}
	
	public List<BusinessRelationView> getBusinessRelations() {
		return this.businessRelations;
	}
	
	public List<ConcessionView> getConcessions() {
		return this.concessions;
	}
	
	public Long getBrokerID() {
		return brokerID;
	}

	public PersonView getPerson() {
		return person;
	}

	public BusinessView getBusiness() {
		return business;
	}

	public Long getDatasourceID() {
		return datasourceID;
	}

	private IDGeneratator initIDGenerator(boolean generateIDs) {
		if(generateIDs) {
			return new IDGeneratator();
		}
		else {
			return null;
		}
	}
}
