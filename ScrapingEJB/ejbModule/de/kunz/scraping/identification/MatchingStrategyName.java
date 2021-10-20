package de.kunz.scraping.identification;

import java.util.*;
import java.util.stream.*;

import de.kunz.scraping.data.entity.*;

public final class MatchingStrategyName extends AbstractMatchingStrategy {

	private static final String PARAM_NAME_TYPE = "type";

	private static final String PARAM_NAME_MIN_NUMBER_OF_MATCHING_TOKENS = "minNumOfMatchingTokens";
	
	private static final String PARAM_NAME_EDIT_DISTANCE = "maxEditDistance";
	
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_PERSON = "person";
	
	private static final String PARAM_DEFAULT_VALUE_TYPE_BUSINESS = "business";
	
	private static final int PARAM_DEFAULT_VALUE_MIN_NUMBER_OF_MATCHING_TOKENS = 2;
	
	private static final int PARAM_DEFAULT_VALUE_EDIT_DISTANCE = 2;

	private static final String PARAM_REGEX_INTEGER = "[0-9]+";
	
		
	private static Set<Integer> generateIntegerSet(int min, int max) {
		final Set<Integer> set = new HashSet<>();
		if(min > max) {
			throw new IllegalArgumentException();
		}
		while(max >= min) {
			set.add(max);
			max--;
		}
		return set;
	}
	
	private static Set<Set<Integer>> computerPowerSet(Set<Integer> set) {
		final Set<Set<Integer>> powerSet = new HashSet<>();
		computePowerSet(set, powerSet);
		return powerSet;
	}
	
	private static void computePowerSet(Set<Integer> set, Set<Set<Integer>> powerSet) {
		if(set == null || powerSet == null) {
			throw new NullPointerException();
		}
		final Integer nextInteger = fetchAndRemove(set);
		if(nextInteger != null) {
			computePowerSet(set, powerSet);
			
			final Iterator<Set<Integer>> powerSetItertor = new HashSet<>(powerSet).iterator();
			while(powerSetItertor.hasNext()) {
				Set<Integer> subset = powerSetItertor.next();
				
				
				final Set<Integer> extendedSubset = new HashSet<>(subset);
				
				
				extendedSubset.add(nextInteger);
				powerSet.add(subset);
				powerSet.add(extendedSubset);
			}
		}
		else {
			powerSet.add(new HashSet<>()); // The empty set is a subset of the empty set. 
		}
	}
	
	private static Integer fetchAndRemove(Set<Integer> set) {
		Integer nextInt = null;
		final Iterator<Integer> setIterator = set.iterator();
		if(setIterator.hasNext()) {
			nextInt = setIterator.next();
			setIterator.remove();
		}
		return nextInt;
	}
	
	
	private String type;
	
	private int minNumOfMatchingTokens;
	
	private int maxEditDistance;

	
	public MatchingStrategyName() {
		this.type = PARAM_DEFAULT_VALUE_TYPE_PERSON;
		this.minNumOfMatchingTokens = PARAM_DEFAULT_VALUE_MIN_NUMBER_OF_MATCHING_TOKENS;
		this.maxEditDistance = PARAM_DEFAULT_VALUE_EDIT_DISTANCE;
	}
	
	
	@Override
	protected void onInit(Map<String, String> paramterMap) {
		final String typeStr = this.extractParameter(paramterMap, PARAM_NAME_TYPE);		
		final String minNumOfMatchingTokensStr = this.extractParameter(paramterMap, PARAM_NAME_MIN_NUMBER_OF_MATCHING_TOKENS);
		final String maxEditDistanceStr = this.extractParameter(paramterMap, PARAM_NAME_EDIT_DISTANCE);
		if(PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(type) || PARAM_DEFAULT_VALUE_TYPE_BUSINESS.equals(type)) {
			this.type = typeStr;
		}
		if((minNumOfMatchingTokensStr != null) && (PARAM_REGEX_INTEGER.matches(minNumOfMatchingTokensStr))) {
			this.minNumOfMatchingTokens = Integer.parseInt(minNumOfMatchingTokensStr);
		}
		if((maxEditDistanceStr != null) && PARAM_REGEX_INTEGER.matches(maxEditDistanceStr)) {
			this.maxEditDistance = Integer.parseInt(maxEditDistanceStr);
		}
	}

	@Override
	protected boolean onMatch(Broker firstBroker, Broker secondBroker) {
		boolean matches = false;
		final String firstBrokerName = fetchFullName(firstBroker);
		final String secondBrokerName = fetchFullName(secondBroker);
		if(firstBrokerName != null && secondBrokerName != null) {
			final String[] firstBrokerNameTokenArr = firstBrokerName.split(" ");
			final String[] secondBrokerNameTokenArr = secondBrokerName.split(" ");
			final int minLength = Integer.min(firstBrokerNameTokenArr.length, secondBrokerNameTokenArr.length);
			for(int numOfMatchingTokens = this.minNumOfMatchingTokens; (numOfMatchingTokens <= minLength) && !matches; numOfMatchingTokens++) {
				final int numOfMatchingTokensFinal = numOfMatchingTokens;				
				final Set<Set<Integer>> firstBrokerNameTokenArrIndices = computerPowerSet(generateIntegerSet(0, firstBrokerNameTokenArr.length - 1))
						.stream().filter(set -> set.size() == numOfMatchingTokensFinal).collect(Collectors.toSet());
				final Set<Set<Integer>> secondBrokerNameTokenArrIndices = computerPowerSet(generateIntegerSet(0, secondBrokerNameTokenArr.length - 1))
						.stream().filter(set -> set.size() == numOfMatchingTokensFinal).collect(Collectors.toSet());
				for(Set<Integer> firstBrokerIndices : firstBrokerNameTokenArrIndices) {
					for(Set<Integer> secondBrokerIndices: secondBrokerNameTokenArrIndices) {
						final List<Integer> firstBrokerIndicesList = new LinkedList<>(firstBrokerIndices);
						final List<Integer> secondBrokerIndicesList = new LinkedList<>(secondBrokerIndices);
						Collections.sort(firstBrokerIndicesList);
						Collections.sort(secondBrokerIndicesList);
						final StringBuilder firstBrokerTokenSeq = new StringBuilder("");
						final StringBuilder secondBrokerTokenSeq = new StringBuilder("");
						for(int index = 0; index < firstBrokerIndicesList.size(); index++) {
							final int firstBrokerNameTokenArrIndex = firstBrokerIndicesList.get(index);
							final int secondBrokerNameTokenArrIndex = secondBrokerIndicesList.get(index);
							firstBrokerTokenSeq.append(firstBrokerNameTokenArr[firstBrokerNameTokenArrIndex]);
							secondBrokerTokenSeq.append(secondBrokerNameTokenArr[secondBrokerNameTokenArrIndex]);
						}						
						final int editDistance = EditDistance.calculateEditDistance(firstBrokerTokenSeq.toString(), secondBrokerTokenSeq.toString());
						matches = editDistance <= this.maxEditDistance;
						if(matches) {
							break;
						}
					}
					if(matches) {
						break;
					}
				}
			}	
		}
		return matches; 
	}
	
	private String fetchFullName(Broker broker) {
		final StringBuilder fullName = new StringBuilder("");
		final Person person = broker.getPerson();
		final Business business = broker.getBusiness();
		if(PARAM_DEFAULT_VALUE_TYPE_PERSON.equals(this.type) && person != null) {
			final String firstname = person.getPrename();
			final String lastname = person.getLastname();
			if(firstname != null) {
				fullName.append(firstname.toLowerCase());
				fullName.append(" ");
			}
			if(lastname != null) {
				fullName.append(lastname);
			}
		}
		else if(business != null) {
			final String firm = business.getFirm();
			if(firm != null) {
				fullName.append(firm.toLowerCase());
			}
		}
		if(fullName.length() == 0) {
			return null; 
		}
		else { 
			return fullName.toString();
		}
	}
	

}
