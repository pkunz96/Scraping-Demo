package de.kunz.scraping.data.access;

import java.io.IOException;

import javax.ejb.Local;

import de.kunz.scraping.data.entity.*;

@Local
public interface LineAccessEJBLocal {
	
	public enum LineIdentifier {
		
		HEALTH_INSURANCE(0, "Krankenversicherung"),
		
		LIFE_INSURANCE(1, "Lebensversicherung"),
		
		PROPERTY_INSURANCE(2, "Sachversicherung"),

		ACCIDENT_INSURANCE(3, "Unfallversicherung"),

		CAR_INSURANCE(4, "Kraftfahrtversicherung");
		
		private final long lineId;
		
		private final String lineDescription;
		
		private LineIdentifier(long lineId, String lineDescription) {
			this.lineId = lineId;
			this.lineDescription = lineDescription;
		}

		public long getLineId() {
			return lineId;
		}

		public String getLineDescription() {
			return lineDescription;
		}
	}
	
	/**
	 * Returns the managed instance of {@link Line} identified by the passed instance of {@link LineIdentifier}.
	 * 
	 * @param lineIdentifier identifies an instance of {Line}.
	 * @return an instance of {@link Line}.
	 * @throws NullPointerException if lineIdentifier is null.
	 * @throws IOException if an I/O error occurs.
	 */
	public Line getLine(LineIdentifier lineIdentifier)
			throws IOException;
}
