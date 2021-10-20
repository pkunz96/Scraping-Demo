package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;
import javax.ejb.*;

import de.kunz.scraping.data.entity.*;

@Local
public interface ConcessionAccessEJBLocal {

	public enum ConcessionIdentifier {
		
		EXCLUSIVE_INSURANCE_AGENT(0, "Gebundener Versicherungsvertreter nach § 34d Abs. 7 GewO"), 
		
		INSURANCE_AGENT(1, "Versicherungsvertreter mit Erlaubnis nach § 34d Abs. 1 GewO"), 
		
		INSURANCE_BROKER(2, "Versicherungsmakler mit Erlaubnis nach § 34d Abs. 1 GewO"),
		
		MORTAGE_BROKER(3, "Immobiliardarlehensvermittler (§ 34i Abs. 1 S. 1 GewO)"),
		
		INVESTMENT_BROKER(4, "Finanzanlagen­vermittler nach § 34f Abs. 1 S. 1 GewO");
		
		public static List<ConcessionIdentifier> getConcessionIdentifiers() {
			final List<ConcessionIdentifier> concessionIdentifierList = new LinkedList<>();
			{
				concessionIdentifierList.add(EXCLUSIVE_INSURANCE_AGENT);
				concessionIdentifierList.add(INSURANCE_AGENT);
				concessionIdentifierList.add(INSURANCE_BROKER);
				concessionIdentifierList.add(MORTAGE_BROKER);
				concessionIdentifierList.add(INVESTMENT_BROKER);
			}
			return concessionIdentifierList;
		}
		
		private final long concessionId;
		
		private final String concessionDescrption;
		
		private ConcessionIdentifier(long concessionId, String concessionDescrption) {
			this.concessionId = concessionId;
			this.concessionDescrption = concessionDescrption;
		}

		public long getConcessionId() {
			return concessionId;
		}

		public String getConcessionDescrption() {
			return concessionDescrption;
		}
	}

	public List<Concession> getConcessions() 
		throws IOException;
	
	/**
	 * Returns the managed instance of {@link Concession} identified by the passed instance of {@link ConcessionIdentifier}.
	 * 
	 * @param concessionIdentifier identifies an instance of {Concession}.
	 * @return an instance of {@link Concession}.
	 * @throws NullPointerException if concessionIdentifier is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public Concession getConcession(ConcessionIdentifier concessionIdentifier)
			throws IOException;
	
}
