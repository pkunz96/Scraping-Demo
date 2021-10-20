package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IPropertyProvider extends Iterator<Void> {
	
	String getName();
	
	String getValue();
}
