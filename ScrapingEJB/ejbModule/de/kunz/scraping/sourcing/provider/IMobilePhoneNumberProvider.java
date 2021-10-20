package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IMobilePhoneNumberProvider extends Iterator<Void> {

	String getPhoneNumber();
	
	Boolean isPrivatePhoneNumber();
	
}
