package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;
import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface LegalStatusAccessEJBLocal {
	
	/**
	 * There is a finite set of legal statuses. Each legal status is identified by an 
	 * instance of LegalStatusIdentifiert.
	 * 
	 * @author Philipp Kunz
	 */
	public enum LegalStatusIdentifier {
		
		EMPLOYEE(0, "Angestellter"), AGENT(1, "Handelvertreter nach § 84 HGB"), BROKER(2, "Makler nach § 93 HGB");

		public static List<LegalStatusIdentifier> getLegalStatusIdentifiers() {
			final List<LegalStatusIdentifier> legalStatusIdentifiersList = new LinkedList<>();
			{
				legalStatusIdentifiersList.add(EMPLOYEE);
				legalStatusIdentifiersList.add(AGENT);
				legalStatusIdentifiersList.add(BROKER);
			}
			return legalStatusIdentifiersList;
		}
		
		private long legalStatusId;
		private String legalStatusName;
		
		private LegalStatusIdentifier(long legalStatusId, String legalStatusName) {
			this.legalStatusId = legalStatusId;
			this.legalStatusName = legalStatusName;
		}

		public long getLegalStatusId() {
			return legalStatusId;
		}

		public String getLegalStatusName() {
			return legalStatusName;
		}
	}
	
	public List<LegalStatus> getLegalStatuses()
			throws IOException;
	
	/**
	 * Returns an instance of {@link LegalStatus} identified by the passed instance of
	 * {@link LegalStatusIdentifier}.
	 * 
	 * @param legalStatusIdentifier - an instance of {@link LegalStatusIdentifier}
	 * @return - an instance of {@link LegalStatus}
	 * @throws NullPointerException - if legalStatusIdentifier is null.
	 * @throws IOException - if an I/O error occurs.
	 */
	public LegalStatus getLegalStatus(LegalStatusIdentifier legalStatusIdentifier)
			throws IOException;
}
