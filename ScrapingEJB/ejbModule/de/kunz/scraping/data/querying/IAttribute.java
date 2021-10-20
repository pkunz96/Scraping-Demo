package de.kunz.scraping.data.querying;

import java.util.*;

public interface IAttribute {
	
	String getName();
	
	boolean isValidValue(String value);
	
	Iterator<String> getUniverse();
	
	boolean supports(Relation relation);
	
}
