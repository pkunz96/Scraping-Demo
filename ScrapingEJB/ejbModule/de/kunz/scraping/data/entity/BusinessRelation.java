package de.kunz.scraping.data.entity;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="BUSINESS_RELATION")
public class BusinessRelation {

	@Id
	@GeneratedValue
	@Column(name="business_relation_id")
	private long businessRelationId;
	
	@Column(name="begin_date")
	private Date beginDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="title" , nullable = false)
	private String title;
	
	@ManyToOne
	@JoinColumn(name="fk_comissioning_broker_id", referencedColumnName = "broker_id")
	private Broker comissioningBroker;
	
	@ManyToOne
	@JoinColumn(name="fk_comissioning_financial_product_provider_id", referencedColumnName = "financial_product_provider_id")
	private FinancialProductProvider comissioningProductProvider;
	 
	@ManyToOne
	@JoinColumn(name="fk_comissioned_broker_id", referencedColumnName = "broker_id")
	private Broker comissionedBroker;
	
	@ManyToOne
	@JoinColumn(name="fk_legal_status_id", referencedColumnName = "legal_status_id")
	private LegalStatus legalStatus;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id")
	private Datasource datasource;
	
	@OneToMany(mappedBy="businessRelation")
	private List<Revenue> revenueList;
	
	@Transient
	private List<BusinessRelation> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public BusinessRelation() {
		super();
		this.revenueList = new LinkedList<>();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
	
	private BusinessRelation(boolean isConflictVersion, Date beginDate, Date endDate, String title, LegalStatus legalStatus, Datasource datasource) {
		this();
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.title = title;
		this.legalStatus = legalStatus;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}
	
	public long getBusinessRelationId() {
		return businessRelationId;
	}

	public void setBusinessRelationId(long businessRelationId) {
		this.businessRelationId = businessRelationId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Broker getComissioningBroker() {
		return comissioningBroker;
	}

	public void setComissioningBroker(Broker comissioningBroker) {
		this.comissioningBroker = comissioningBroker;
	}

	public FinancialProductProvider getComissioningProductProvider() {
		return comissioningProductProvider;
	}

	public void setComissioningProductProvider(FinancialProductProvider comissioningProductProvider) {
		this.comissioningProductProvider = comissioningProductProvider;
	}

	public Broker getComissionedBroker() {
		return comissionedBroker;
	}

	public void setComissionedBroker(Broker comissionedBroker) {
		this.comissionedBroker = comissionedBroker;
	}

	public LegalStatus getLegalStatus() {
		return legalStatus;
	}

	public void setLegalStatus(LegalStatus legalStatus) {
		this.legalStatus = legalStatus;
	}
	
	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public List<Revenue> getRevenueList() {
		return EntityUtilities.<Revenue>copyList(revenueList);
	}

	public void setRevenueList(List<Revenue> revenueList) {
		this.revenueList.clear();
		this.revenueList.addAll(revenueList);
	}

	public List<BusinessRelation> getConflictingVersionsList() {
		return EntityUtilities.<BusinessRelation>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(BusinessRelation businessRelation) {
		if(businessRelation == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final Date beginDate = businessRelation.getBeginDate();
		final Date endDate = businessRelation.getEndDate();
		Date newBeginDate = null;
		Date newEndDate = null;
		if(beginDate != null) {
			newBeginDate = new Date(beginDate.getTime());
		}
		if(endDate != null) {
			newEndDate = new Date(endDate.getTime());
		}
		final BusinessRelation conflictVersion = new BusinessRelation(true, newBeginDate, newEndDate, businessRelation.getTitle(), businessRelation.getLegalStatus(), businessRelation.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}
		
	public void removeConflictingVersion(BusinessRelation businessRelation) {
		if(businessRelation  == null) {
			throw new NullPointerException();
		}
		else if(!businessRelation.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(BusinessRelation conflictionVersion : this.conflictVersionsList) {
				if(businessRelation.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(businessRelation);
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
		result = prime * result + (int) (businessRelationId ^ (businessRelationId >>> 32));
		result = prime * result + ((comissionedBroker == null) ? 0 : comissionedBroker.hashCode());
		result = prime * result + ((comissioningBroker == null) ? 0 : comissioningBroker.hashCode());
		result = prime * result + ((comissioningProductProvider == null) ? 0 : comissioningProductProvider.hashCode());
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((legalStatus == null) ? 0 : legalStatus.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessRelation other = (BusinessRelation) obj;
		if (beginDate == null) {
			if (other.beginDate != null)
				return false;
		} else if (!beginDate.equals(other.beginDate))
			return false;
		if (businessRelationId != other.businessRelationId)
			return false;
		if (comissionedBroker == null) {
			if (other.comissionedBroker != null)
				return false;
		} else if (!comissionedBroker.equals(other.comissionedBroker))
			return false;
		if (comissioningBroker == null) {
			if (other.comissioningBroker != null)
				return false;
		} else if (!comissioningBroker.equals(other.comissioningBroker))
			return false;
		if (comissioningProductProvider == null) {
			if (other.comissioningProductProvider != null)
				return false;
		} else if (!comissioningProductProvider.equals(other.comissioningProductProvider))
			return false;
		if (conflictVersionsList == null) {
			if (other.conflictVersionsList != null)
				return false;
		} else if (!conflictVersionsList.equals(other.conflictVersionsList))
			return false;
		if (datasource == null) {
			if (other.datasource != null)
				return false;
		} else if (!datasource.equals(other.datasource))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (legalStatus == null) {
			if (other.legalStatus != null)
				return false;
		} else if (!legalStatus.equals(other.legalStatus))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.businessRelationId + ", " + this.comissionedBroker +", " + this.comissioningProductProvider + "}";
	}
	  
}
