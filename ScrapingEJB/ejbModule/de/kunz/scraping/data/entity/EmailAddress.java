package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="EMAIL_ADDRESS")
public class EmailAddress {

	@Id
	@GeneratedValue
	@Column(name="email_address_id")
	private long emailAddressId;
	
	@Column(name="email_address", nullable = false)
	private String emailAddress;

	@ManyToOne
	@JoinColumn(name="fk_person_id" , referencedColumnName = "person_id")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="fk_business_id" , referencedColumnName = "business_id")
	private Business business;

	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@Transient
	private List<EmailAddress> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public EmailAddress() {
		super(); 
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}

	private EmailAddress(boolean isConflictVerions, String emailAddress, Datasource datasource) {
		this();
		this.emailAddress = emailAddress;
		this.datasource = datasource;
		this.isConflictVersion = isConflictVerions;
	}
	
	public long getEmailAddressId() {
		return emailAddressId;
	}

	public void setEmailAddressId(long emailAddressId) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.emailAddressId = emailAddressId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.emailAddress = emailAddress;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.person = person;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.business = business;
	}
	
	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.datasource = datasource;
	}	
	
	public List<EmailAddress> getConflictingVersionsList() {
		return EntityUtilities.<EmailAddress>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(EmailAddress emailAddress) {
		if(emailAddress == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final EmailAddress conflictVersion = new EmailAddress(true, emailAddress.getEmailAddress(), emailAddress.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}	
	
	public void removeConflictingVersion(EmailAddress emailAddress) {
		if(emailAddress  == null) {
			throw new NullPointerException();
		}
		else if(!emailAddress.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(EmailAddress conflictionVersion : this.conflictVersionsList) {
				if(emailAddress.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(emailAddress);
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
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + (int) (emailAddressId ^ (emailAddressId >>> 32));
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		EmailAddress other = (EmailAddress) obj;
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
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (emailAddressId != other.emailAddressId)
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.emailAddressId + ", " + this.emailAddress + "}";
	}

}
