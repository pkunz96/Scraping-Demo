package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name="PHONE_NUMBER")
public class PhoneNumber {

	@Id
	@GeneratedValue
	@Column(name="phone_number_id")
	private long phoneNumberId;
	
	@Column(name="phone_number", nullable = false)
	private String phoneNumber;
	 
	@Column(name="is_mobile_phone_number")
	private Boolean isMobilePhoneNumber;
	
	@ManyToOne
	@JoinColumn(name="fk_person_id", referencedColumnName = "person_id")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="fk_business_id", referencedColumnName = "business_id")
	private Business business;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
		
	@Transient
	private boolean isConflictVersion;
	
	@Transient
	private List<PhoneNumber> conflictVersionsList;
	
	public PhoneNumber() {
		super();
		this.conflictVersionsList = new LinkedList<>();
	}
	
	private PhoneNumber(boolean isConflictVersion,  String phoneNumber, boolean isMobilePhoneNumber, Datasource datasource) {
		this();
		this.phoneNumber = phoneNumber;
		this.isMobilePhoneNumber = isMobilePhoneNumber;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVersion;
	}
	
	public long getPhoneNumberId() { 
		return phoneNumberId;
	}

	public void setPhoneNumberId(long phoneNumberId) {
		if(this.isConflictVersion) { 
			throw new IllegalStateException();
		}
		this.phoneNumberId = phoneNumberId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.phoneNumber = phoneNumber;
	}

	public Boolean isMobilePhoneNumber() {
		return isMobilePhoneNumber;
	}

	public void setMobilePhoneNumber(Boolean isMobilePhoneNumber) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.isMobilePhoneNumber = isMobilePhoneNumber;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.person = person;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		this.business = business;
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
	
	public List<PhoneNumber> getConflictingVersionsList() {
		return EntityUtilities.<PhoneNumber>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(PhoneNumber phoneNumber) {
		if(phoneNumber == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final PhoneNumber conflictVersion = new PhoneNumber(true, phoneNumber.getPhoneNumber(), phoneNumber.isMobilePhoneNumber(), phoneNumber.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}	
	
	public void removeConflictingVersion(PhoneNumber phoneNumber) {
		if(phoneNumber  == null) {
			throw new NullPointerException();
		}
		else if(!phoneNumber.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(PhoneNumber conflictionVersion : this.conflictVersionsList) {
				if(phoneNumber.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(phoneNumber);
				}
			} 
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((business == null) ? 0 : business.hashCode());
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((isMobilePhoneNumber == null) ? 0 : isMobilePhoneNumber.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + (int) (phoneNumberId ^ (phoneNumberId >>> 32));
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
		PhoneNumber other = (PhoneNumber) obj;
		if (business == null) {
			if (other.business != null)
				return false;
		} else if (!business.equals(other.business))
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
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (isMobilePhoneNumber == null) {
			if (other.isMobilePhoneNumber != null)
				return false;
		} else if (!isMobilePhoneNumber.equals(other.isMobilePhoneNumber))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (phoneNumberId != other.phoneNumberId)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.phoneNumberId + ", " + this.phoneNumber + "}";
	}

}
