package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * An instance of LegalStatusAccessEJBLocal.
 * 
 * @author Philipp Kunz
 */
@Stateless
@LocalBean
public class LegalStatusAccessEJB implements LegalStatusAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityMananger;
	
    /**
     * Constructs an instance of LegalStatusAccessEJB.
     */
    public LegalStatusAccessEJB() {
    	super();
    }

    public List<LegalStatus> getLegalStatuses() 
    		throws IOException {
    	final List<LegalStatus> legalStatusesList = new LinkedList<>();
    	final List<LegalStatusIdentifier> legalStatusesIdentifierList = LegalStatusIdentifier.getLegalStatusIdentifiers();
    	{
    		for(LegalStatusIdentifier legalStatusIdentifier : legalStatusesIdentifierList) {
    			final LegalStatus legalStatus = getLegalStatus(legalStatusIdentifier);
    			legalStatusesList.add(legalStatus);
    		}
    	}
    	return legalStatusesList;
    }
    
	@Override
	public LegalStatus getLegalStatus(LegalStatusIdentifier legalStatusIdentifier) 
			throws IOException {
		LegalStatus legalStatus;
		if(legalStatusIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			final long legalStatusId  = legalStatusIdentifier.getLegalStatusId();
			legalStatus = entityMananger.find(LegalStatus.class, legalStatusId);
			if(legalStatus == null) {
				final String legalStatusName = legalStatusIdentifier.getLegalStatusName();
				legalStatus = new LegalStatus();
				legalStatus.setLegalStatusId(legalStatusId);
				legalStatus.setLegalStatusDescription(legalStatusName);
				entityMananger.persist(legalStatus);
			}
		}
		return legalStatus;
	}

}
