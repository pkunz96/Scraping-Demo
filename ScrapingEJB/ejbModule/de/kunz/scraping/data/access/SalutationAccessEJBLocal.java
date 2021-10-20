package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.Local;

import de.kunz.scraping.data.entity.*;

@Local
public interface SalutationAccessEJBLocal {

	public enum SalutationIdentifier {
		
		DEFAULT_SALUTATION(0, "Sehr geehrte Frau", "Sehr geehrter Herr" );
		
		public static List<SalutationIdentifier> getSaluationIdentifiers() {
			final List<SalutationIdentifier> salutationIdentifierList = new LinkedList<>();
			{
				salutationIdentifierList.add(DEFAULT_SALUTATION);
			}
			return salutationIdentifierList;
		}
		
		private final long salutationId;
		
		private final String salutationFemaleForm;
		
		private final String salutationMaleForm;
		
		private SalutationIdentifier(long salutationId, String salutationFemaleForm, String salutationMaleForm) {
			this.salutationId= salutationId;
			this.salutationFemaleForm = salutationFemaleForm;
			this.salutationMaleForm = salutationMaleForm;
		}
		
		public long getSalutationId() {
			return salutationId;
		}

		public String getSalutationFemaleForm() {
			return salutationFemaleForm;
		}

		public String getSalutationMaleForm() {
			return salutationMaleForm;
		}
	
	}

	public List<Salutation> getSalutations() 
			throws IOException;
	
	/**
	 * Returns the managed instance of {@link Salutation} identified by the passed instance of {@link SalutationIdentifier}.
	 * 
	 * @param saluationIdentifier identifies an instance of {@link Salutation}.
	 * @return an instance of {@link Salutation}.
	 * @throws NullPointerException if saluationIdentifier is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public Salutation getSalutation(SalutationIdentifier saluationIdentifier)
			throws IOException;
	
}

