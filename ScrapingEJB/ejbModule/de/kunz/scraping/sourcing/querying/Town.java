package de.kunz.scraping.sourcing.querying;

import java.util.*;

import de.kunz.scraping.data.querying.IAttribute;
import de.kunz.scraping.data.querying.Relation;

public final class Town implements IAttribute {

	private static final String REGEX_TOWN = "[a-zA-Z-]+@[A-Z]{2}";
	
	private final String attrbName;
	
	public Town() {
		this.attrbName = "town";
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
			return value.matches(REGEX_TOWN);
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
