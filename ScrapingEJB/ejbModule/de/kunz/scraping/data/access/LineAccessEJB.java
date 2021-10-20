package de.kunz.scraping.data.access;

import java.io.*;

import javax.ejb.*;
import javax.persistence.*;

import de.kunz.scraping.data.entity.*;

/**
 * Session Bean implementation class LineAccess
 */
@Stateless
@LocalBean
public class LineAccessEJB implements LineAccessEJBLocal {

	@PersistenceContext
	private EntityManager entityMananger;
	
    /**
     * Constructs an instance of LineAccess.
     */
    public LineAccessEJB() {
    	super();
    }

	@Override
	public Line getLine(LineIdentifier lineIdentifier)
			throws IOException {
		Line line;
		if(lineIdentifier == null) {
			throw new NullPointerException();
		}
		else {
			try {
				final long lineId  = lineIdentifier.getLineId();
				line = entityMananger.find(Line.class, lineId);
				if(line == null) {
					final String lineDescription = lineIdentifier.getLineDescription();
					line = new Line();
					line.setLineId(lineId);
					line.setLineDescription(lineDescription);
					entityMananger.persist(line);
				}
			}catch(PersistenceException e0) {
				throw new IOException(e0);
			}
		}
		return line;
	}

}
