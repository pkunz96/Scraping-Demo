package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IConcessionProvider extends Iterator<Void>{
	
	String getConcession();
	
	String getRegisterNo();
}
