package de.kunz.scraping.sourcing.querying;

import java.util.*;

import de.kunz.scraping.data.querying.IAttribute;
import de.kunz.scraping.data.querying.Relation;

public final class ZipCode implements IAttribute {

	private static final String REGEX_ZIP_CODE = "[0-9]{4,6}@[A-Z]{2}";
	
	private final String attrbName;
	
	public ZipCode() {
		this.attrbName = "zip_code";
	}

	@Override
	public String getName() {
		return this.attrbName;
	}

	@Override
	public boolean isValidValue(String value) {
		if(value == null) {
			return false;
		}
		else {
			return value.matches(REGEX_ZIP_CODE);
		} 
	}
	
	@Override
	public Iterator<String> getUniverse() {
		return null;
	}

	@Override
	public boolean supports(Relation relation) {
		return Relation.EUQAL.equals(relation);
	}
		
}
  