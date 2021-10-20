package de.kunz.scraping.data.entity;

import java.util.*;
import javax.persistence.*;


@Entity
@Table(name="PERSON")
public final class Person {

	@Id
	@GeneratedValue
	@Column(name="person_id")
	private long personId;
	
	@Column(name="prename", nullable = false)
	private String prename;
	
	@Column(name="lastname", nullable = false)
	private String lastname;
	
	@Column(name="birthdate")
	private Date birthdate;

	@Column(name="protrait_img")
	private byte[] protrait;
	
	@OneToOne
	@JoinColumn(name="fk_address_id", referencedColumnName = "address_id")
	private Address address;
	
	@ManyToOne
	@JoinColumn(name="fk_salutation_id", referencedColumnName = "salutation_id", nullable = false)
	private Salutation salutation;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id", nullable = false)
	private Datasource datasource;
	
	@OneToOne(mappedBy="person")
	private Broker broker;
	
	@OneToMany(mappedBy="person")
	private List<PhoneNumber> phoneNumberList;
	
	@OneToMany(mappedBy="person")
	private List<EmailAddress> emailAddressList;
	
	@Transient
	private List<Person> conflictingVersionsList;
	
	@Transient
	private boolean isConflictingVersion;
		
	public Person() {
		super(); 
		this.phoneNumberList = new LinkedList<>();
		this.emailAddressList = new LinkedList<>();
		this.conflictingVersionsList = new LinkedList<>();
		this.isConflictingVersion = false;
	}
	
	private Person(boolean isConflictingVersion, String prename, String lastname, Date birthdate, byte[] portrait, Salutation salutation, Datasource datasource) {
		this();
		this.prename = prename;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.protrait = portrait;
		this.salutation = salutation;
		this.datasource = datasource;
		this.isConflictingVersion = isConflictingVersion;
	}
	
	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.prename = prename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.birthdate = birthdate;
	}
	
	public byte[] getProtrait() {
		return protrait;
	}

	public void setProtrait(byte[] protrait) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.protrait = protrait;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.address = address;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salutation) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.salutation = salutation;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.broker = broker;
	}
	
	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.datasource = datasource;
	}

	public List<PhoneNumber> getPhoneNumberList() {
		return EntityUtilities.<PhoneNumber>copyList(phoneNumberList);
	}

	public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.phoneNumberList.clear();
		this.phoneNumberList.addAll(phoneNumberList);
	}

	public List<EmailAddress> getEmailAddressList() {
		return EntityUtilities.<EmailAddress>copyList(emailAddressList); 		
	}

	public void setEmailAddressList(List<EmailAddress> emailAddressList) {
		if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		this.emailAddressList.clear();
		this.emailAddressList.addAll(emailAddressList);
	}

	public List<Person> getConflictingVersionsList() {
		return EntityUtilities.<Person>copyList(this.conflictingVersionsList); 		
	}
	
	public void addConflictingVersion(Person person) {
		if(person == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictingVersion) {
			throw new IllegalStateException();
		}
		final Date personBirhtdate = person.getBirthdate();
		final byte[] personPortrait = person.getProtrait();
		Date newPersonBirthdate = null;
		byte[] newPersonPortrait = null;
		if(personBirhtdate != null) {
			newPersonBirthdate = new Date(personBirhtdate.getTime());
		}
		if(personPortrait != null) {
			newPersonPortrait = Arrays.copyOf(personPortrait, personPortrait.length);
		}
		final Person conflictVersion = new Person(true,person.getPrename(), person.getLastname(), newPersonBirthdate, newPersonPortrait, person.getSalutation(), person.getDatasource());
		this.conflictingVersionsList.add(conflictVersion);
	}
	
	public void removeConflictingVersion(Person person) {
		if(person  == null) {
			throw new NullPointerException();
		}
		else if(!person.isConflictingVersion) {
			throw new IllegalArgumentException(); 
		}
		else {
			for(Person conflictionVersion : this.conflictingVersionsList) {
				if(person.equals(conflictionVersion)) {
					this.conflictingVersionsList.remove(person);
				}
			}
		}
	}
	
	public void addConflictingVersions(List<Person> conflictVersions) {
		for(Person conflictVersion : conflictVersions) {
			if(!conflictVersion.isConflictingVersion) {
				throw new IllegalArgumentException();
			}
			else if(!this.conflictingVersionsList.contains(conflictVersion)) {
				this.conflictingVersionsList.add(conflictVersion);
			}
		}
	}
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result + ((conflictingVersionsList == null) ? 0 : conflictingVersionsList.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + (int) (personId ^ (personId >>> 32));
		result = prime * result + ((prename == null) ? 0 : prename.hashCode());
		result = prime * result + Arrays.hashCode(protrait);
		result = prime * result + ((salutation == null) ? 0 : salutation.hashCode());
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
		Person other = (Person) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (birthdate == null) {
			if (other.birthdate != null)
				return false;
		} else if (!birthdate.equals(other.birthdate))
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
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (personId != other.personId)
			return false;
		if (prename == null) {
			if (other.prename != null)
				return false;
		} else if (!prename.equals(other.prename))
			return false;
		if (!Arrays.equals(protrait, other.protrait))
			return false;
		if (salutation == null) {
			if (other.salutation != null)
				return false;
		} else if (!salutation.equals(other.salutation))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.personId + ", " + this.prename + " " + this.lastname + "}";
	}
}
