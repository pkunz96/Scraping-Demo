package de.kunz.scraping.sourcing.provider;

import java.util.*;

import de.kunz.scraping.data.entity.*;

public class BrokerBuilderImpl implements IBrokerBuilder {

	private static boolean isInitialized(BrokerBuilderImpl instance) {
		return (instance.masterDataProvider != null) 
				&& (instance.salutations != null)
					&& (instance.countries != null)
						&& (instance.concessions != null)
							&& (instance.productProviders != null)
								&& (instance.legalStatuses != null)
									&& (instance.datasource != null);

	}
	
	private IMasterDataProvider masterDataProvider;
	private List<Salutation> salutations;
	private List<Country> countries;
	private List<Concession> concessions;
	private List<FinancialProductProvider> productProviders;
	private List<LegalStatus> legalStatuses;
	private Datasource datasource;
	
	private Broker curBroker;
	
	public BrokerBuilderImpl() {
		super();
	}

	@Override
	public void setMasterDataProvider(IMasterDataProvider masterDataProvider) {
		if(masterDataProvider == null) {
			throw new NullPointerException();
		}
		else {
			this.masterDataProvider = masterDataProvider;
		}
	}

	@Override
	public void setSaluations(List<Salutation> salutations) {
		if(salutations == null) {
			throw new NullPointerException();
		}
		else {
			this.salutations = salutations;
		}		
	}

	@Override
	public void setCountries(List<Country> countries) {
		if(countries == null) {
			throw new NullPointerException();
		}
		else {
			this.countries = countries;
		}		
	}

	@Override
	public void setConcessions(List<Concession> concessions) {
		if(concessions == null) {
			throw new NullPointerException();
		}
		else {
			this.concessions = concessions;
		}		
	}
	

	@Override
	public void setLegalStatuses(List<LegalStatus> legalStatuses) {
		if(legalStatuses == null) {
			throw new NullPointerException();
		}
		else {
			this.legalStatuses = legalStatuses;
		}			
	}

	@Override
	public void setFinancialProductProviders(List<FinancialProductProvider> productProviders) {
		if(productProviders == null) {
			throw new NullPointerException();
		}
		else {
			this.productProviders = productProviders;
		}		
	}

	@Override
	public void setDatasource(Datasource datasource) {
		if(datasource == null) {
			throw new NullPointerException();
		}
		else {
			this.datasource = datasource;
		}		
	}

	@Override
	public List<Broker> build() {
		final List<Broker> brokerList = new LinkedList<>();
		if(!isInitialized(this)) {
			throw new IllegalStateException();
		}
		else {
			while(this.masterDataProvider.hasNext()) {
				this.masterDataProvider.next();
				nextBroker();
				brokerList.add(this.curBroker);
			}
		}
		return brokerList;
	}
	
	private void nextBroker() {
		this.curBroker = new Broker();
		if(this.masterDataProvider.getBusinessProvider() != null) {
			initBusiness();
		}
		if(this.masterDataProvider.getPersonProvider() != null) {
			initPerson();
		}
		initPhoneNumbers();
		initEmailAddresses(); 
		initConcessions();
		initBusinessRelations();
	}
	
	private void initBusiness() {
		final Business business = new Business();
		final IBusinessProvider businessProvider = this.masterDataProvider.getBusinessProvider();
		{
			final boolean isSmallBusiness = businessProvider.isSmallBusiness();
			final String firm = businessProvider.getFirm();
			final String companyRegistrationNo = businessProvider.getCompanyRegistrationNo();
			business.setSmallBusiness(isSmallBusiness);
			business.setFirm(firm);
			business.setCompanyRegistrationNo(companyRegistrationNo);
		}
		{
			final String street = businessProvider.getStreet();
			final String houseNo = businessProvider.getHouseNo();
			final String zipCode = businessProvider.getZipCode(); 
			final String town = businessProvider.getTown();
			final Country country = getCountry(businessProvider.getCountryCode());
			final Address address = new Address();
			address.setStreet(street);
			address.setHouseNo(houseNo);
			address.setZipCode(zipCode);
			address.setTown(town);
			address.setCountry(country);
			business.setAddress(address);	
			address.setDatasource(datasource);
		}
		this.curBroker.setBusiness(business);
		business.setBroker(this.curBroker);
		business.setDatasource(datasource);
	}
	
	private void initPerson() {
		final Person person = new Person();
		final IPersonProvider personProvider = this.masterDataProvider.getPersonProvider();
		{
			final Salutation salutation = getSalutation(personProvider.getSalutation());
			final String prename = personProvider.getPrename();
			final String lastname = personProvider.getLastname();
			person.setSalutation(salutation);
			person.setPrename(prename);
			person.setLastname(lastname);
		}
		{
			//TODO
			//final byte[] protrait = personProvider.getPortrait();
			//person.setProtrait(protrait);
		}
		{
			final Date birthdate = personProvider.getBirthDate();
			person.setBirthdate(birthdate);
		}
		{
			final String street = personProvider.getStreet();
			final String houseNo = personProvider.getHouseNo();
			final String zipCode = personProvider.getZipCode();
			final String town = personProvider.getTown();
			final Country country = getCountry(personProvider.getCountryCode());
			final Address address = new Address();
			address.setStreet(street);
			address.setHouseNo(houseNo);
			address.setZipCode(zipCode);
			address.setTown(town);
			address.setCountry(country);
			person.setAddress(address);	
			address.setDatasource(datasource);
		}
		this.curBroker.setPerson(person);
		person.setBroker(this.curBroker);		
		person.setDatasource(datasource);
	}
	
	private void initPhoneNumbers() {
		final Person person = this.curBroker.getPerson();
		final Business business = this.curBroker.getBusiness();
		final List<PhoneNumber> personPhoneNumbers = new LinkedList<>();
		final List<PhoneNumber> businessPhoneNumbers = new LinkedList<>();
		final IPhoneNumberProvider phoneNumberProvider = this.masterDataProvider.getPhoneNumberProvider();
		final IMobilePhoneNumberProvider mobilePhoneNumberProvider = this.masterDataProvider.getMobilePhoneNumberProvider();
		while(phoneNumberProvider.hasNext()) {
			phoneNumberProvider.next(); 
			final PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setPhoneNumber(phoneNumberProvider.getPhoneNumber());
			if((person != null) && (phoneNumberProvider.isPrivatePhoneNumber())) {
				personPhoneNumbers.add(phoneNumber); 
				phoneNumber.setPerson(person);
			}
			else if(business != null) { 
				businessPhoneNumbers.add(phoneNumber);
				phoneNumber.setBusiness(business);
			}
			phoneNumber.setDatasource(datasource);
		}
		while(mobilePhoneNumberProvider.hasNext()) {
			mobilePhoneNumberProvider.next();
			final PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setPhoneNumber(mobilePhoneNumberProvider.getPhoneNumber());
			phoneNumber.setMobilePhoneNumber(true);
			if((person != null) && (mobilePhoneNumberProvider.isPrivatePhoneNumber())) {
				personPhoneNumbers.add(phoneNumber);
				phoneNumber.setPerson(person);
			}
			else if(business != null) {
				businessPhoneNumbers.add(phoneNumber);
				phoneNumber.setBusiness(business);
			}
			phoneNumber.setDatasource(datasource);
		}
		if(person != null) person.setPhoneNumberList(personPhoneNumbers);
		if(business != null) business.setPhoneNumberList(businessPhoneNumbers);
	}
	
	private void initEmailAddresses() {
		final Person person = this.curBroker.getPerson();
		final Business business = this.curBroker.getBusiness();
		final List<EmailAddress> personEmailAddresses = new LinkedList<>();
		final List<EmailAddress> businessEmailAddresses = new LinkedList<>();
		final IEmailAdressProvider emailAddressProvider  = this.masterDataProvider.getEmailAddressProvider();
 		while(emailAddressProvider.hasNext()) {
			emailAddressProvider.next();
			final EmailAddress emailAddress = new EmailAddress();
			emailAddress.setEmailAddress(emailAddressProvider.getEmailAddress());
			if((person != null) && emailAddressProvider.isPrivateEmailAddress()) {
				personEmailAddresses.add(emailAddress);
				emailAddress.setPerson(person);
			}
			else if(business != null) {
				businessEmailAddresses.add(emailAddress);
				emailAddress.setBusiness(business);
			}
			emailAddress.setDatasource(datasource);
		}
		if(person != null) person.setEmailAddressList(personEmailAddresses);
		if(business != null) business.setEmailAddressList(businessEmailAddresses);
	}
	
	private void initConcessions() {
		final List<BrokerConcessionMapping> brokerConcessionMappings = new LinkedList<>();
		final IConcessionProvider concessionProvider = this.masterDataProvider.getConcessionProvider();
		while(concessionProvider.hasNext()) {
			concessionProvider.next();
			final String concessionStr = concessionProvider.getConcession();
			final String registerNo = concessionProvider.getRegisterNo();
			final Concession concession = getConcession(concessionStr);
			if(concession != null) {
				final BrokerConcessionMapping mapping = new BrokerConcessionMapping();
				mapping.setBroker(this.curBroker);
				mapping.setConcession(concession); 
				mapping.setRegisterNo(registerNo);
				brokerConcessionMappings.add(mapping);
				mapping.setDatasource(datasource);
			}
		}
		this.curBroker.setBrokerConcessionMappingList(brokerConcessionMappings);
	}
	
	private void initBusinessRelations() {
		final List<BusinessRelation> commisioningBusinessRelations = new LinkedList<>();
		final IBusinessRelationProvider businessRelationProvider = this.masterDataProvider.getBusinessRelationProvider();
		while(businessRelationProvider.hasNext()) {
			businessRelationProvider.next();
			final String commissioningProductProviderName = businessRelationProvider.getCommissioningFinancialProductProviderName();			
			System.out.println("- " + commissioningProductProviderName);
			final String legalStatusStr = businessRelationProvider.getLegalStatus();			
			final FinancialProductProvider commissioningProductProvider = getFinancialProductProvider(commissioningProductProviderName);
			final LegalStatus legalStatus  = getLegalStatus(legalStatusStr);
			final String title = businessRelationProvider.getTitle();
			final BusinessRelation businessRelation = new BusinessRelation();
			businessRelation.setComissioningProductProvider(commissioningProductProvider);
			businessRelation.setLegalStatus(legalStatus);
			businessRelation.setTitle(title);
			commisioningBusinessRelations.add(businessRelation);
			businessRelation.setDatasource(datasource);
		}
		this.curBroker.setComissioningBusinessRelationsList(commisioningBusinessRelations);
	}
	
	private Salutation getSalutation(String salutationStr) {
		return null;
	} 
	
	private Country getCountry(String countryCodeStr) {
		if(countryCodeStr != null) {
			for(Country country : this.countries) {
				final String curCountryCode = country.getCountryCode();
				if((curCountryCode != null) 
						&& (curCountryCode.equals(countryCodeStr))) {
					return country;
				}
			}
		}
		return null;
	}
	
	private Concession getConcession(String concessionStr) {
		if(concessionStr != null) {
			for(Concession concession : this.concessions) {
				final String concessionDescription = concession.getConcessionDescription();
				if((concessionDescription != null) 
						&& (concessionDescription.contains(concessionStr))) {
					return concession;
				}
	 		}
		}
		return null;
	}
	
	private FinancialProductProvider getFinancialProductProvider(String financialProductProviderStr) {
		if(financialProductProviderStr != null) {
			for(FinancialProductProvider productProvider : this.productProviders) {
				final String curFinancialProductProviderName = productProvider.getFinancialProductName();
				System.out.println(financialProductProviderStr);
				if((curFinancialProductProviderName != null) 
						&& (curFinancialProductProviderName.contains(financialProductProviderStr))) {
					return productProvider;
				}
	 		}
		}
		return null;
	}
	
	private LegalStatus getLegalStatus(String legalStatusStr) {
		if(legalStatusStr != null) {
			for(LegalStatus legalStatus : this.legalStatuses) {
				final String legalStatusDescription = legalStatus.getLegalStatusDescription();
				if((legalStatusDescription != null) 
						&& (legalStatusDescription.contains(legalStatusStr))) {
					return legalStatus;
				}
	 		}
		}
		return null;
	}
}
