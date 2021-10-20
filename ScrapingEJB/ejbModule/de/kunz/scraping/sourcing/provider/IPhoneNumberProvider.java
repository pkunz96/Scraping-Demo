package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IPhoneNumberProvider extends Iterator<Void> {

	String getPhoneNumber();
	
	Boolean isPrivatePhoneNumber();
	
}
