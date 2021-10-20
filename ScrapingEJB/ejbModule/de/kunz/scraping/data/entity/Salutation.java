package de.kunz.scraping.data.entity;

import javax.persistence.*;

@Entity 
@Table(name="SALUTATION")
public class Salutation {

	@Id
	@Column(name="salutation_id")
	private long salutationId;
	
	@Column(name="salutation_female_form", nullable = false, unique = true)
	private String salutationFemaleForm;
	
	@Column(name="salutation_male_form", nullable = false, unique = true)
	private String saltuationMaleForm;
	
	public Salutation() {
		super();
	}

	public long getSalutationId() {
		return salutationId;
	}

	public void setSalutationId(long salutationId) {
		this.salutationId = salutationId;
	}

	public String getSalutationFemaleForm() {
		return salutationFemaleForm;
	}

	public void setSalutationFemaleForm(String salutationFemaleForm) {
		this.salutationFemaleForm = salutationFemaleForm;
	}

	public String getSaltuationMaleForm() {
		return saltuationMaleForm;
	}

	public void setSaltuationMaleForm(String saltuationMaleForm) {
		this.saltuationMaleForm = saltuationMaleForm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((saltuationMaleForm == null) ? 0 : saltuationMaleForm.hashCode());
		result = prime * result + ((salutationFemaleForm == null) ? 0 : salutationFemaleForm.hashCode());
		result = prime * result + (int) (salutationId ^ (salutationId >>> 32));
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
		Salutation other = (Salutation) obj;
		if (saltuationMaleForm == null) {
			if (other.saltuationMaleForm != null)
				return false;
		} else if (!saltuationMaleForm.equals(other.saltuationMaleForm))
			return false;
		if (salutationFemaleForm == null) {
			if (other.salutationFemaleForm != null)
				return false;
		} else if (!salutationFemaleForm.equals(other.salutationFemaleForm))
			return false;
		if (salutationId != other.salutationId)
			return false;
		return true;
	}
	
	@Override 
	public String toString() {
		return "Entity (" + this.getClass().getName() + "): {" + this.salutationId + ", " + this.salutationFemaleForm + " " + this.saltuationMaleForm + "}";
	}

}
