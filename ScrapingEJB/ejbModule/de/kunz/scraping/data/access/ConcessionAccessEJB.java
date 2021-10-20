package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * An implementation of ConcessionAccessEJBLocal.
 * 
 */
@Stateless
@LocalBean
public class ConcessionAccessEJB implements ConcessionAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityMananger;
	
    /**
     * Constructs an instance of ConcessionAccessEJB.
     */
    public ConcessionAccessEJB() {
    	super ();
    }


	@Override
	public List<Concession> getConcessions()
			throws IOException {
		final List<Concession> concessionsList = new LinkedList<>();
		final List<ConcessionIdentifier> concessionIdentifiersList = ConcessionIdentifier.getConcessionIdentifiers();
		for(ConcessionIdentifier concessionIdentifier : concessionIdentifiersList) {
			final Concession concession = getConcession(concessionIdentifier);
			concessionsList.add(concession);
		}
		return concessionsList;
	}    
    
	@Override
	public Concession getConcession(ConcessionIdentifier concessionIdentifier) 
			throws IOException {
		Concession concession;
		if(concessionIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			try {
				final long concessionId = concessionIdentifier.getConcessionId();
				concession = entityMananger.find(Concession.class, concessionId);
				if(concession == null) {
					final String concessionDescription = concessionIdentifier.getConcessionDescrption();
					concession = new Concession();
					concession.setConcessionId(concessionId);
					concession.setConcessionDescription(concessionDescription);
					entityMananger.persist(concession);
				}
			}catch(PersistenceException e0) {
				throw new IOException(e0);
			}
		}
		return concession;
	}
}
