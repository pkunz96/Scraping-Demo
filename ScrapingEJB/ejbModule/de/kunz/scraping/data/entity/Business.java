package de.kunz.scraping.data.entity;


import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="BUSINESS")
public class Business {
	
	@Id
	@GeneratedValue
	@Column(name="business_id")
	private long businessId;
	
	@Column(name="is_small_business", nullable = false)
	private Boolean isSmallBusiness; 
	
	@Column(name="firm", nullable = false)
	private String firm;
	
	@Column(name="company_registration_no", unique = true)
	private String companyRegistrationNo;
	
	@OneToOne
	@JoinColumn(name="fk_address_id", referencedColumnName = "address_id", nullable = false)
	private Address address;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@OneToOne(mappedBy="business")
	private Broker broker;
	
	@OneToMany(mappedBy="business")
	private List<PhoneNumber> phoneNumberList;
	
	@OneToMany(mappedBy="business")
	private List<EmailAddress> emailAddressList;
	
	@Transient
	private List<Business> conflictingVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public Business() {
		super();
		this.phoneNumberList = new LinkedList<>();
		this.emailAddressList = new LinkedList<>();
		this.conflictingVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}
	
	private Business(boolean isConflictVersion, boolean isSmallBusiness, String firm, String companyRegistrationNo, Datasource datasource) {
		this();
		this.isSmallBusiness = isSmallBusiness;
		this.firm = firm;
		this.companyRegistrationNo = companyRegistrationNo;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long companyId) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.businessId = companyId;
	}

	
	public boolean isSmallBusiness() {
		return isSmallBusiness;
	}

	public void setSmallBusiness(boolean isSmallBusiness) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.isSmallBusiness = isSmallBusiness;
	}

	public String getFirm() {
		return firm;
	}

	public void setFirm(String firm) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.firm = firm;
	}
	
	public String getCompanyRegistrationNo() {
		return companyRegistrationNo;
	}

	public void setCompanyRegistrationNo(String companyRegistrationNo) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.companyRegistrationNo = companyRegistrationNo;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.address = address;
	}
	
	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.broker = broker;
	}
	
	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.datasource = datasource;
	}

	public List<PhoneNumber> getPhoneNumberList() {
		return EntityUtilities.<PhoneNumber>copyList(this.phoneNumberList);
	}

	public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.phoneNumberList.clear();
		this.phoneNumberList.addAll(phoneNumberList);
	}

	public List<EmailAddress> getEmailAddressList() {
		return EntityUtilities.<EmailAddress>copyList(this.emailAddressList);
	}

	public void setEmailAddressList(List<EmailAddress> emailAddressList) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.emailAddressList.clear();
		this.emailAddressList.addAll(emailAddressList);
	}

	public List<Business> getConflictingVersionsList() {
		return EntityUtilities.<Business>copyList(this.conflictingVersionsList); 		
	}
	
	public void addConflictingVersion(Business business) {
		if(business == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final Business conflictVersion = new Business(true, business.isSmallBusiness(), business.getFirm(), business.getCompanyRegistrationNo(), business.getDatasource());
		this.conflictingVersionsList.add(conflictVersion);
	}	
	
	public void addConflictingVersions(List<Business> conflictVersions) {
		for(Business conflictVersion : conflictVersions) {
			if(!conflictVersion.isConflictVersion) {
				throw new IllegalArgumentException();
			}
			else if(!this.conflictingVersionsList.contains(conflictVersion)) {
				this.conflictingVersionsList.add(conflictVersion);
			}
		}
	}
	
	public void removeConflictingVersion(Business business) {
		if(business  == null) {
			throw new NullPointerException();
		}
		else if(!business.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(Business conflictionVersion : this.conflictingVersionsList) {
				if(business.equals(conflictionVersion)) {
					this.conflictingVersionsList.remove(business);
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (int) (businessId ^ (businessId >>> 32));
		result = prime * result + ((companyRegistrationNo == null) ? 0 : companyRegistrationNo.hashCode());
		result = prime * result + ((conflictingVersionsList == null) ? 0 : conflictingVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + ((firm == null) ? 0 : firm.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((isSmallBusiness == null) ? 0 : isSmallBusiness.hashCode());
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
		Business other = (Business) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (businessId != other.businessId)
			return false;
		if (companyRegistrationNo == null) {
			if (other.companyRegistrationNo != null)
				return false;
		} else if (!companyRegistrationNo.equals(other.companyRegistrationNo))
			return false;
		if (conflictingVersionsList == null) {
			if (other.conflictingVersionsList != null)
				return false;
		} else if (!conflictingVersionsList.equals(other.conflictingVersionsList))
			return false;
		if (datasource == null) {
			if (other.datasource != null)
				return false;
		} else if (!datasource.equals(other.datasource))
			return false;
		if (firm == null) {
			if (other.firm != null)
				return false;
		} else if (!firm.equals(other.firm))
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (isSmallBusiness == null) {
			if (other.isSmallBusiness != null)
				return false;
		} else if (!isSmallBusiness.equals(other.isSmallBusiness))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.businessId + ", " + this.firm + "}";
	}
}
