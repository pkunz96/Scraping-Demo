package de.kunz.scraping.data.entity;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="ADDRESS")
public class Address {

	@Id
	@GeneratedValue
	@Column(name="address_id")
	private long addressId;
	
	@Column(name="street", nullable = false)
	private String street;
	
	@Column(name="house_no", nullable = false)
	private String houseNo;
	
	@Column(name="zip_code", nullable = false)
	private String zipCode;
	
	@Column(name="town", nullable = false)
	private String town;
	
	@ManyToOne 
	@JoinColumn(name="fk_country_id", referencedColumnName = "country_id", nullable = false)
	private Country country;
	
	@ManyToOne
	@JoinColumn(name="fk_datasrouce_id", referencedColumnName = "datasrouce_id")
	private Datasource datasource;
	
	@Transient
	private List<Address> conflictVersionsList;
	
	@Transient
	private boolean isConflictVersion;
	
	public Address() {
		super();
		this.conflictVersionsList = new LinkedList<>();
		this.isConflictVersion = false;
	}

	public Address(boolean isConflictVersion, String street, String houseNo, String zipCode, String town, Country country, Datasource datasource) {
		this();
		this.isConflictVersion = isConflictVersion;
		this.street = street;
		this.houseNo = houseNo;
		this.zipCode = zipCode;
		this.town = town;
		this.country = country;
		this.datasource = datasource;
	} 

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.addressId = addressId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.street = street;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.houseNo = houseNo;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.zipCode = zipCode;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.town = town;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		if(isConflictVersion) {
			throw new IllegalStateException();
		}
		this.country = country;
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
	
	public List<Address> getConflictingVersionsList() {
		return EntityUtilities.<Address>copyList(this.conflictVersionsList); 		
	}
	
	public void addConflictingVersion(Address address) {
		if(address == null) {
			throw new NullPointerException(); 
		}
		else if(this.isConflictVersion) {
			throw new IllegalStateException();
		}
		final Address conflictVersion = new Address(true, address.getStreet(), address.getHouseNo(), address.getZipCode(), address.getTown(), address.getCountry(), address.getDatasource());
		this.conflictVersionsList.add(conflictVersion);
	}
	
	public void addConflictingVersions(List<Address> conflictVersions) {
		for(Address conflictVersion : conflictVersions) {
			if(!conflictVersion.isConflictVersion) {
				throw new IllegalArgumentException();
			}
			else if(!this.conflictVersionsList.contains(conflictVersion)) {
				this.conflictVersionsList.add(conflictVersion);
			}
		}
	}
	
	public void removeConflictingVersion(Address address) {
		if(address  == null) {
			throw new NullPointerException();
		}
		else if(!address.isConflictVersion) {
			throw new IllegalArgumentException();
		}
		else {
			for(Address conflictionVersion : this.conflictVersionsList) {
				if(address.equals(conflictionVersion)) {
					this.conflictVersionsList.remove(address);
				}
			}
		}
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (addressId ^ (addressId >>> 32));
		result = prime * result + ((conflictVersionsList == null) ? 0 : conflictVersionsList.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((datasource == null) ? 0 : datasource.hashCode());
		result = prime * result + ((houseNo == null) ? 0 : houseNo.hashCode());
		result = prime * result + (isConflictVersion ? 1231 : 1237);
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Address other = (Address) obj;
		if (addressId != other.addressId)
			return false;
		if (conflictVersionsList == null) {
			if (other.conflictVersionsList != null)
				return false;
		} else if (!conflictVersionsList.equals(other.conflictVersionsList))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (datasource == null) {
			if (other.datasource != null)
				return false;
		} else if (!datasource.equals(other.datasource))
			return false;
		if (houseNo == null) {
			if (other.houseNo != null)
				return false;
		} else if (!houseNo.equals(other.houseNo))
			return false;
		if (isConflictVersion != other.isConflictVersion)
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (town == null) {
			if (other.town != null)
				return false;
		} else if (!town.equals(other.town))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.addressId + ", " + this.zipCode + "}";
	}
}
