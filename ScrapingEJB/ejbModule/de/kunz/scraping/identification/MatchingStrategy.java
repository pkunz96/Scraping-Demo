package de.kunz.scraping.identification;

import java.util.*;

import de.kunz.scraping.data.entity.*;

interface MatchingStrategy {
	
	void init(Map<String, String> paramterMap);
	
	boolean isTrueFinal();
	
	boolean isFalseFinal();
	
	boolean match(Broker firstBroker, Broker secondBroker);
}
  