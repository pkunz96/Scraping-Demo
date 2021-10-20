package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * Session Bean implementation class SalutationAccessEJB
 */
@Stateless
@LocalBean
public class SalutationAccessEJB implements SalutationAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityMananger;
	
    /**
     * Constructs an instance of SalutationAccessEJB. 
     */
    public SalutationAccessEJB() {
    	super();
    }

	@Override
	public List<Salutation> getSalutations() throws IOException {
		final List<Salutation> salutationList = new LinkedList<>();
		final List<SalutationIdentifier> salutationIdentifierList = SalutationIdentifier.getSaluationIdentifiers();
		for(SalutationIdentifier salutationIdentifier : salutationIdentifierList) {
			final Salutation salutation = getSalutation(salutationIdentifier);
			salutationList.add(salutation);
		}
		return salutationList;
	}
    
	@Override
	public Salutation getSalutation(SalutationIdentifier saluationIdentifier)
			throws IOException {
		Salutation salutation;
		if(saluationIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			try {
				final long salutationId = saluationIdentifier.getSalutationId();
				salutation = entityMananger.find(Salutation.class, salutationId);
				if(salutation == null) {
					final String salutationFemaleForm = saluationIdentifier.getSalutationFemaleForm();
					final String salutationMaleForm = saluationIdentifier.getSalutationMaleForm();
					salutation = new Salutation();
					salutation.setSalutationFemaleForm(salutationFemaleForm);
					salutation.setSaltuationMaleForm(salutationMaleForm);
					entityMananger.persist(salutation);
				}
			}catch(PersistenceException e0) {
				throw new IOException(e0);
			}
		}
		return salutation;
	}
}
