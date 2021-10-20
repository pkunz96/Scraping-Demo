package de.kunz.scraping.api.views;

import java.util.*;

import de.exp.ai.scraping.data.entity.*;
import de.kunz.scraping.api.views.BrokerView.IDGeneratator;

public class BusinessRelationView {

	private Long businessRelationID = null;
	
	private Date beginDate = null;
	
	private Date endDate = null;
	
	private String title = null;
	
	private Long legalStautusID = null;	
	
	private FinancialProductProviderView commissioningFinancialProductProvider = null;

	private Long datasourceID;
	
	public BusinessRelationView(BusinessRelation businessRelation, IDGeneratator idGenerator) {
		if(idGenerator == null) {
			this.businessRelationID = businessRelation.getBusinessRelationId();
		}
		else { 
			this.businessRelationID = idGenerator.next();
		}
		{
			this.beginDate = businessRelation.getBeginDate();
			this.endDate = businessRelation.getEndDate();
			this.title = businessRelation.getTitle();
		}
		final LegalStatus legalStatus = businessRelation.getLegalStatus();
		final FinancialProductProvider financialProductProvider = businessRelation.getComissioningProductProvider();
		final Datasource datasource = businessRelation.getDatasource();
		{
			if(legalStatus != null) {
				this.legalStautusID = legalStatus.getLegalStatusId();
			}
			if(financialProductProvider != null) {
				this.commissioningFinancialProductProvider = new FinancialProductProviderView(financialProductProvider, idGenerator);
			}
			if(datasource != null) {
				this.datasourceID = datasource.getDatasourceId();
			}
		}
	}

	public Long getBusinessRelationID() {
		return businessRelationID;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getTitle() {
		return title;
	}

	public Long getLegalStautusID() {
		return legalStautusID;
	}

	public FinancialProductProviderView getCommissioningFinancialProductProvider() {
		return commissioningFinancialProductProvider; 
	}

	public Long getDatasourceID() { 
		return datasourceID;
	}	
}
