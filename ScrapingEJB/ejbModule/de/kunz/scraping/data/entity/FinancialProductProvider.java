package de.kunz.scraping.data.entity;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="FINANCIAL_PRODUCT_PROVIDER")
public class FinancialProductProvider {

	@Id
	@Column(name="financial_product_provider_id")
	private long financialProductProviderId;

	@Column(name="financial_product_name", nullable = false, unique = true)
	private String financialProductName;

	@JoinColumn(name="fk_address_id" , referencedColumnName = "address_id", nullable = false, unique = true)
	private Address address;
	
	@OneToMany(mappedBy="comissioningProductProvider")
	private List<BusinessRelation> comissionedBusinessRelationsList;
	
	public FinancialProductProvider() {
		super();
		this.comissionedBusinessRelationsList = new LinkedList<>();
	}

	public long getFinancialProductProviderId() {
		return financialProductProviderId;
	}

	public void setFinancialProductProviderId(long financialProductProviderId) {
		this.financialProductProviderId = financialProductProviderId;
	}

	public String getFinancialProductName() {
		return financialProductName;
	}

	public void setFinancialProductName(String financialProductName) {
		this.financialProductName = financialProductName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<BusinessRelation> getComissionedBusinessRelationsList() {
		return EntityUtilities.<BusinessRelation>copyList(comissionedBusinessRelationsList);
	}

	public void setComissionedBusinessRelationsList(List<BusinessRelation> comissionedBusinessRelationsList) {
		this.comissionedBusinessRelationsList.clear();
		this.comissionedBusinessRelationsList.addAll(comissionedBusinessRelationsList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((financialProductName == null) ? 0 : financialProductName.hashCode());
		result = prime * result + (int) (financialProductProviderId ^ (financialProductProviderId >>> 32));
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
		FinancialProductProvider other = (FinancialProductProvider) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (financialProductName == null) {
			if (other.financialProductName != null)
				return false;
		} else if (!financialProductName.equals(other.financialProductName))
			return false;
		if (financialProductProviderId != other.financialProductProviderId)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.financialProductProviderId + ", " + this.financialProductName + "}";
	}
	
}
