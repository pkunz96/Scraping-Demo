package de.kunz.scraping.sourcing.provider;

import java.util.*;

public interface IEmailAdressProvider extends Iterator<Void> {

	String getEmailAddress();
	
	Boolean isPrivateEmailAddress();
}
