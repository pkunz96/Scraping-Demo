package de.kunz.scraping.reduction;

import java.util.*;

import javax.ejb.*;

import de.kunz.scraping.data.entity.*;


@Stateless
@LocalBean
public class MergingEJB implements MergingEJBLocal {

	private enum MergingMode {
		DISCARD_FIRST, PRIORIZE_FIRST, PRIORIZE_SECOND, EQUAL_PRIORITY;
	}
	
	private static abstract class DatasourceComparator<T> implements Comparator<T> {
		
		protected int compareDatasources(Datasource o1, Datasource o2) { 
			if(o1 != null) {
				if(o2 != null) {
					if(o1.getPriority() == o2.getPriority()) {
						return 0;
					}
					else if(o1.getPriority() > o2.getPriority()) {
						return 1;
					}
					else {
						return -1;
					}
				}
				else {
					return 1;
				}
			}
			else if(o2 != null) {
				return -1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	private static class PhoneNumberComparator extends DatasourceComparator<PhoneNumber>{

		@Override
		public int compare(PhoneNumber o1, PhoneNumber o2) {
			return super.compareDatasources(o1.getDatasource(), o2.getDatasource());
		}
	}
	
	/*private static class EmailAddressComparator extends DatasourceComparator<EmailAddress> {
		
		@Override
		public int compare(EmailAddress o1, EmailAddress o2) {
			return super.compareDatasources(o1.getDatasource(), o2.getDatasource());
		}
	}*/
	private static MergingMode getMergingMode(Datasource firstDatasource, Datasource secondDatasource) {
		if((firstDatasource == null) || (secondDatasource == null)) {
			throw new NullPointerException();
		}
		else if(secondDatasource.equals(firstDatasource)) {
			 return MergingMode.DISCARD_FIRST;
		}
		else if(firstDatasource.getPriority() == secondDatasource.getPriority()) {
			return MergingMode.EQUAL_PRIORITY;
		}
		else if(firstDatasource.getPriority() >  secondDatasource.getPriority()) {
			return MergingMode.PRIORIZE_FIRST;
		}
		else {
			return MergingMode.PRIORIZE_SECOND;
		}
	}
 	
	private boolean isConflicting(Object firstObj, Object secObj) {
		if(firstObj == null && secObj == null) {
			return false;
		}
		else if((firstObj != null) && (secObj != null)) {
			return firstObj.equals(secObj);
		}
		else {
			return true;
		}
	}

	private static String extractDigits(String str) {
		final StringBuilder strBuilder = new StringBuilder("");
		for(int index = 0; index < str.length(); index++) {
			if(Character.isDigit(str.charAt(index))) {
				strBuilder.append(str.charAt(index));
			}
		}
		return strBuilder.toString();
	}
	
	@Override
	public Broker merge(Broker firstBroker, Broker secondBroker) {
		final Broker target = new Broker();
		if((firstBroker == null) || (secondBroker == null)) {
			throw new NullPointerException();
		}
		else {
			this.mergeBroker(target, firstBroker, secondBroker);
			this.mergePerson(target, firstBroker, secondBroker);
			this.mergeBusiness(target, firstBroker, secondBroker);
			this.mergePersonAddress(target, firstBroker, secondBroker);
			this.mergeBusinessAddress(target, firstBroker, secondBroker);
			this.mergePersonPhoneNumbers(target, firstBroker, secondBroker);
			this.mergeBusinessPhoneNumbers(target, firstBroker, secondBroker);
		}
		return target;
	}
	
	private void mergeBroker(Broker target, Broker firstBroker, Broker secondBroker) {
		final MergingMode mode = getMergingMode(firstBroker.getDatasource(), secondBroker.getDatasource());
		switch(mode) {
			case DISCARD_FIRST: {
				target.setUserContextId(secondBroker.getUserContextId());
				target.addConflictingVersions(secondBroker.getConflictingVersionsList());
				break; 
			}
			case EQUAL_PRIORITY: {
				final long firstUserContextId = secondBroker.getUserContextId();
				target.setUserContextId(firstUserContextId);
				if(firstUserContextId != firstBroker.getUserContextId()) {
					target.addConflictingVersion(firstBroker);
				}
				target.addConflictingVersions(firstBroker.getConflictingVersionsList());
				target.addConflictingVersions(secondBroker.getConflictingVersionsList());
				break;
			}
			case PRIORIZE_FIRST: {
				target.setUserContextId(firstBroker.getUserContextId());
				target.addConflictingVersions(firstBroker.getConflictingVersionsList());
				break;
			}
			case PRIORIZE_SECOND: {
				target.setUserContextId(secondBroker.getUserContextId());
				target.addConflictingVersions(secondBroker.getConflictingVersionsList());
			}
		}
	}
	
	private void mergePerson(Broker target, Broker firstBroker, Broker secondBroker) {
		final Person firstPerson = firstBroker.getPerson();
		final Person secondPerson = secondBroker.getPerson();
		Person targetPerson = null;
		if(firstPerson != null) {
			targetPerson = new Person();
			if(secondPerson != null) {
				final MergingMode mode = getMergingMode(firstPerson.getDatasource(), secondPerson.getDatasource());
				switch(mode) {
					case DISCARD_FIRST: {
						cpyPersonFields(secondPerson, targetPerson);
						targetPerson.addConflictingVersions(secondPerson.getConflictingVersionsList());
						break;
					}
					case EQUAL_PRIORITY: {
						cpyPersonFields(secondPerson, targetPerson);
						targetPerson.addConflictingVersions(firstPerson.getConflictingVersionsList());
						targetPerson.addConflictingVersions(secondPerson.getConflictingVersionsList());
						if(isConflicting(firstPerson.getPrename(), secondPerson.getPrename()) 
								|| isConflicting(firstPerson.getLastname(),	secondPerson.getLastname()) 
									|| isConflicting(firstPerson.getBirthdate(), secondPerson.getBirthdate()) 
										|| isConflicting(firstPerson.getSalutation(), secondPerson.getSalutation())) {
							targetPerson.addConflictingVersion(firstPerson);
						}		
						break;
					}
					case PRIORIZE_FIRST: {
						cpyPersonFields(firstPerson, targetPerson);
						targetPerson.addConflictingVersions(firstPerson.getConflictingVersionsList());
						break;
					}
					case PRIORIZE_SECOND: {
						cpyPersonFields(secondPerson, targetPerson);
						targetPerson.addConflictingVersions(secondPerson.getConflictingVersionsList());
						break;
					}
				}
			}
			else {
				cpyPersonFields(firstPerson, targetPerson);
				targetPerson.addConflictingVersions(firstPerson.getConflictingVersionsList());
			}
		}
		else if(secondPerson != null) {
			targetPerson = new Person();
			cpyPersonFields(secondPerson, targetPerson);
			targetPerson.addConflictingVersions(secondPerson.getConflictingVersionsList());
		}
		targetPerson.setBroker(target);
		target.setPerson(targetPerson);
	}

	private void mergeBusiness(Broker target, Broker firstBroker, Broker secondBroker) {
		final Business firstBusiness = firstBroker.getBusiness();
		final Business secondBusiness = secondBroker.getBusiness();
		Business targetBusiness = null;
		if(firstBusiness != null) {
			targetBusiness = new Business();
			if(secondBusiness != null) {
				final MergingMode mode = getMergingMode(firstBusiness.getDatasource(), secondBusiness.getDatasource());
				switch(mode) {
					case DISCARD_FIRST: {
						cpyBusiness(secondBusiness, targetBusiness);
						targetBusiness.addConflictingVersions(secondBusiness.getConflictingVersionsList());
						break;
					}
					case EQUAL_PRIORITY: {
						cpyBusiness(secondBusiness, targetBusiness);
						targetBusiness.addConflictingVersions(firstBusiness.getConflictingVersionsList());
						targetBusiness.addConflictingVersions(secondBusiness.getConflictingVersionsList());
						if(isConflicting(firstBusiness.isSmallBusiness(), secondBusiness.isSmallBusiness()) 
								|| isConflicting(firstBusiness.getFirm(), secondBusiness.getFirm())
									|| isConflicting(firstBusiness.getCompanyRegistrationNo(), secondBusiness.getCompanyRegistrationNo())) {
							targetBusiness.addConflictingVersion(firstBusiness);
						}
						break;
					}
					case PRIORIZE_FIRST: {
						cpyBusiness(firstBusiness, targetBusiness);
						targetBusiness.addConflictingVersions(firstBusiness.getConflictingVersionsList());
						break;
					}
					case PRIORIZE_SECOND: {
						cpyBusiness(secondBusiness, targetBusiness);
						targetBusiness.addConflictingVersions(secondBusiness.getConflictingVersionsList());
						break;
					}
				}
			}
			else {
				cpyBusiness(firstBusiness, targetBusiness);
				targetBusiness.addConflictingVersions(firstBusiness.getConflictingVersionsList());
			}
		}
		else if(secondBusiness  != null) {
			targetBusiness = new Business();
			cpyBusiness(secondBusiness, targetBusiness);
			targetBusiness.addConflictingVersions(secondBusiness.getConflictingVersionsList());
		}
		if(targetBusiness != null) {
			targetBusiness.setBroker(target);
			target.setBusiness(targetBusiness);
		}
	}
	
	private void mergePersonAddress(Broker target, Broker firstBroker, Broker secondBroker) {
		Address firstAddress = null;
		Address secondAddress = null;
		Person person = firstBroker.getPerson();
		if(person != null) {
			firstAddress = person.getAddress();
		}
		person = secondBroker.getPerson();
		if(person != null) {
			secondAddress = person.getAddress();
		}
		if((firstAddress != null) || (secondAddress != null)) {
			final Person targetPerson = target.getPerson();
			if(targetPerson == null) {
				throw new IllegalStateException();
			}
			else {
				targetPerson.setAddress(mergeAddress(firstAddress, secondAddress));
			}
		}
	}
	
	private void mergeBusinessAddress(Broker target, Broker firstBroker, Broker secondBroker) {
		Address firstAddress = null;
		Address secondAddress = null;
		Business business = firstBroker.getBusiness();
		if(business != null) {
			firstAddress = business.getAddress();
		}
		business = secondBroker.getBusiness();
		if(business != null) {
			secondAddress = business.getAddress();
		}
		if((firstAddress != null) || (secondAddress != null)) {
			final Business targetBusiness = target.getBusiness();
			if(targetBusiness == null) {
				throw new IllegalStateException();
			}
			else {
				targetBusiness.setAddress(mergeAddress(firstAddress, secondAddress));
			}
		} 
	}
	
	private Address mergeAddress(Address firstAddress, Address secondAddress) {
		Address targetAddress = null;
		if(firstAddress != null) {
			targetAddress = new Address();
			if(secondAddress != null) {
				final MergingMode mode = getMergingMode(firstAddress.getDatasource(), secondAddress.getDatasource());
				switch(mode) {
					case DISCARD_FIRST: {
						cpyAddress(secondAddress, targetAddress);
						targetAddress.addConflictingVersions(secondAddress.getConflictingVersionsList());
						break;
					}
					case EQUAL_PRIORITY: {
						cpyAddress(targetAddress, secondAddress);
						targetAddress.addConflictingVersions(firstAddress.getConflictingVersionsList());
						targetAddress.addConflictingVersions(secondAddress.getConflictingVersionsList());
						if(isConflicting(firstAddress.getStreet(), secondAddress.getStreet()) 
								|| isConflicting(firstAddress.getHouseNo(), secondAddress.getHouseNo()) 
								 	|| isConflicting(firstAddress.getZipCode(), secondAddress.getZipCode()) 
								 		|| isConflicting(firstAddress.getTown(), secondAddress.getTown()) 
								 			|| isConflicting(firstAddress.getCountry(), secondAddress.getCountry())) {
							targetAddress.addConflictingVersion(firstAddress);
						}
						break;
					}
					case PRIORIZE_FIRST: {
						cpyAddress(firstAddress, targetAddress);
						targetAddress.addConflictingVersions(firstAddress.getConflictingVersionsList());
						break;
					}
					case PRIORIZE_SECOND: {
						cpyAddress(secondAddress, targetAddress);
						targetAddress.addConflictingVersions(secondAddress.getConflictingVersionsList());
						break;
					}
				}
			}
			else {
				cpyAddress(firstAddress, targetAddress);
				targetAddress.addConflictingVersions(firstAddress.getConflictingVersionsList());
			}
		}
		else if(secondAddress != null){
			targetAddress = new Address();
			cpyAddress(secondAddress, targetAddress);
			targetAddress.addConflictingVersions(secondAddress.getConflictingVersionsList());
		}
		return targetAddress;
	}
	
	private void mergePersonPhoneNumbers(Broker target, Broker firstBroker, Broker secondBroker) {
		List<PhoneNumber> firstPhoneNumberList = null;
		List<PhoneNumber> secondPhoneNumberList = null;
		Person person = firstBroker.getPerson();
		if(person != null) {
			firstPhoneNumberList = person.getPhoneNumberList();
		}
		person = secondBroker.getPerson();
		if(person != null) {
			secondPhoneNumberList = person.getPhoneNumberList();
		}
		if((firstPhoneNumberList != null) || (secondPhoneNumberList != null)) {
			final Person targetPerson = target.getPerson();
			if(targetPerson == null) {
				throw new IllegalStateException();
			}
			else {
				final List<PhoneNumber> mergedPhoneNumbersList = mergePhoneNumbers(firstPhoneNumberList, secondPhoneNumberList);
				targetPerson.setPhoneNumberList(mergedPhoneNumbersList);
				mergedPhoneNumbersList.forEach(phoneNumber -> phoneNumber.setPerson(targetPerson));
			}
		}
	}
	
	private void mergeBusinessPhoneNumbers(Broker target, Broker firstBroker, Broker secondBroker) {
		List<PhoneNumber> firstPhoneNumberList = null;
		List<PhoneNumber> secondPhoneNumberList = null;
		Business business = firstBroker.getBusiness();
		if(business != null) {
			firstPhoneNumberList = business.getPhoneNumberList();
		}
		business = secondBroker.getBusiness();
		if(business != null) {
			secondPhoneNumberList = business.getPhoneNumberList();
		}
		if((firstPhoneNumberList != null) || (secondPhoneNumberList != null)) {
			final Business targetBusiness = target.getBusiness();
			if(targetBusiness == null) {
				throw new IllegalStateException();
			}
			else {
				final List<PhoneNumber> mergedPhoneNumbersList = mergePhoneNumbers(firstPhoneNumberList, secondPhoneNumberList);
				targetBusiness.setPhoneNumberList(mergedPhoneNumbersList);
				mergedPhoneNumbersList.forEach(phoneNumber -> phoneNumber.setBusiness(targetBusiness));
			}
		}
		
 	}
	
	private List<PhoneNumber> mergePhoneNumbers(List<PhoneNumber> firstPhoneNumberList, List<PhoneNumber> secondPhoneNumberList) {
		final List<PhoneNumber> targetPhoneNumberList = new LinkedList<>();
		final Map<String, List<PhoneNumber>> firstPhoneNumberMap = buildPhoneNumberMap(firstPhoneNumberList);
		final Map<String, List<PhoneNumber>> secondPhoneNumberMap = buildPhoneNumberMap(secondPhoneNumberList);
		final Set<String> commonKeys = new HashSet<>(firstPhoneNumberMap.keySet());
		commonKeys.retainAll(secondPhoneNumberMap.keySet());
		final Set<String> firstMapKeys = new HashSet<>(firstPhoneNumberMap.keySet());
		firstMapKeys.removeAll(commonKeys);
		final Set<String> secondMapKeys = new HashSet<>(secondPhoneNumberMap.keySet());
		secondMapKeys.removeAll(commonKeys);
		for(String key : commonKeys) { 
			final List<PhoneNumber> candidateList = new LinkedList<>();
			{ 
				candidateList.addAll(firstPhoneNumberMap.get(key));
				candidateList.addAll(secondPhoneNumberMap.get(key));				
				targetPhoneNumberList.add(getPhoneNumber(candidateList));
			}
		}
		for(String key : firstMapKeys) {
			final List<PhoneNumber> candidateList = new LinkedList<>();
			{
				candidateList.addAll(firstPhoneNumberMap.get(key));
				targetPhoneNumberList.add(getPhoneNumber(candidateList));
			}
		}
		for(String key : secondMapKeys) {
			final List<PhoneNumber> candidateList = new LinkedList<>();
			{
				candidateList.addAll(secondPhoneNumberMap.get(key));
				targetPhoneNumberList.add(getPhoneNumber(candidateList));
			}
		}
		return targetPhoneNumberList;
	}
	
	private PhoneNumber getPhoneNumber(List<PhoneNumber> phoneNumberCandidatesList)  {
		PhoneNumber primaryCandidateCpy = null;
		if(!phoneNumberCandidatesList.isEmpty()) {
			final int FIRST_ELEMENT = 0;
			Collections.sort(phoneNumberCandidatesList, new PhoneNumberComparator().reversed());
			primaryCandidateCpy = new PhoneNumber();
			{	
				final PhoneNumber primaryCandidate = phoneNumberCandidatesList.get(FIRST_ELEMENT);
				cpyPhoneNumber(primaryCandidate, primaryCandidateCpy);
				primaryCandidateCpy.addConflictingVersions(primaryCandidate.getConflictingVersionsList());
			}
			for(int index = 1; index < phoneNumberCandidatesList.size(); index++) {
				final PhoneNumber conflictCandidate = phoneNumberCandidatesList.get(index);
				if(new PhoneNumberComparator().compare(conflictCandidate, primaryCandidateCpy) != 0) {
					break;
				}
				else if(primaryCandidateCpy.isMobilePhoneNumber() != conflictCandidate.isMobilePhoneNumber()) {
					primaryCandidateCpy.addConflictingVersion(conflictCandidate);
				}
				for(PhoneNumber conflictCandidateConflictVersion : conflictCandidate.getConflictingVersionsList()) {
					if(primaryCandidateCpy.isMobilePhoneNumber() != conflictCandidateConflictVersion.isMobilePhoneNumber()) {
						primaryCandidateCpy.addConflictingVersion(conflictCandidateConflictVersion);
					}
				}
			}
		}
		return primaryCandidateCpy;
	}
	
	private Map<String, List<PhoneNumber>> buildPhoneNumberMap(List<PhoneNumber> phoneNumberList) {
		final Map<String, List<PhoneNumber>> phoneNumberMap = new HashMap<>();
		for(PhoneNumber phoneNumber : phoneNumberList) {
			final String phoneNumeberStr = phoneNumber.getPhoneNumber();
			if(phoneNumeberStr  != null) {
				final String phoneNumberStrDigits = extractDigits(phoneNumeberStr);
				List<PhoneNumber> internalPhoneNumberList = phoneNumberMap.computeIfAbsent(phoneNumberStrDigits, key -> new LinkedList<PhoneNumber>());
				internalPhoneNumberList.add(phoneNumber);
				phoneNumberMap.put(phoneNumberStrDigits, internalPhoneNumberList);
			}
		}
		return phoneNumberMap;
	}
 		
	private void cpyPersonFields(Person src, Person dest) {
		dest.setPrename(src.getPrename());
		dest.setLastname(src.getLastname());
		final Date birthdate = src.getBirthdate();
		if(birthdate != null) {
			dest.setBirthdate(new Date(birthdate.getTime()));
		}
		final byte[] portrait = src.getProtrait();
		if(portrait != null) {
			dest.setProtrait(Arrays.copyOf(portrait, portrait.length));
		}
		dest.setSalutation(src.getSalutation());
		dest.setDatasource(src.getDatasource());
	}
	
	private void cpyBusiness(Business src, Business dest) {
		dest.setSmallBusiness(src.isSmallBusiness());
		dest.setFirm(src.getFirm());
		dest.setCompanyRegistrationNo(src.getCompanyRegistrationNo());
		dest.setDatasource(src.getDatasource());
	}

	private void cpyAddress(Address src, Address dest) {
		dest.setStreet(src.getStreet());
		dest.setHouseNo(src.getHouseNo());
		dest.setZipCode(src.getZipCode());
		dest.setTown(src.getTown());
		dest.setCountry(src.getCountry());
		dest.setDatasource(src.getDatasource());
	}
	
	private void cpyPhoneNumber(PhoneNumber src, PhoneNumber dest) {
		dest.setPhoneNumber(src.getPhoneNumber());
		dest.setMobilePhoneNumber(src.isMobilePhoneNumber());
		dest.setDatasource(src.getDatasource());
	}
}
 