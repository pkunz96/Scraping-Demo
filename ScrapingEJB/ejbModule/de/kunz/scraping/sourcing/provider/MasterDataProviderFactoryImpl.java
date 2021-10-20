package de.kunz.scraping.sourcing.provider;

import java.util.*;
import java.lang.reflect.*;

import org.jsoup.*;
import org.jsoup.nodes.*;


import org.json.simple.*;
import org.json.simple.parser.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.sourcing.provider.adapter.*;

final class MasterDataProviderFactoryImpl implements IMasterDataProviderFactory {
	
	
	private static final String VALUE_CONTEXT_NAME_BUSINESS_RELATIONS = "businessRelations";
	
	private static final String VALUE_CONTEXT_NAME_CONCESSIONS = "concessions";
	
	private static final String VALUE_CONTEXT_NAME_PHONE_NUMBERS = "phoneNumbers";
	
	private static final String VALUE_CONTEXT_NAME_MOBILE_PHONE_NUMBERS = "mobilePhoneNumbers";
	
	private static final String VALUE_CONTEXT_NAME_EMAIL_ADDRESSES = "emailAddresses";
	
	private static final String VALUE_CONTEXT_NAME_PROPERTIES = "properties";
	
	public MasterDataProviderFactoryImpl() {
		super();
	}

	@Override
	public IMasterDataProvider createMasterDataProvider(IProviderConfiguration providerConf, String src) {
		final MasterDataProviderImpl masterDataProvider;
		if((providerConf == null) || (src == null)) {
			throw new NullPointerException();
		}
		else {
			masterDataProvider = new MasterDataProviderImpl();
			masterDataProvider.startInit();
			masterDataProvider.setProviderConfiguration(providerConf);
			initAdapter(providerConf, masterDataProvider);
			initData(providerConf, src, masterDataProvider);			
			initProviders(providerConf, masterDataProvider);
			masterDataProvider.finalizeInit();
		}
		return masterDataProvider;
	}

	private void initAdapter(IProviderConfiguration providerConf, MasterDataProviderImpl masterDataProvider) {
		final String jsonAdapterClassName = providerConf.getJSONAdapterClassName();
		final String htmlAdapterClassName = providerConf.getHTMLAdapterClassName();
		try {
			if(jsonAdapterClassName != null) {
				Class<?> adapterClass = Class.forName(jsonAdapterClassName);
				Constructor<?> constructor = adapterClass.getConstructor(new Class[]{});
				IJSONAdapter jsonAdapter = (IJSONAdapter) constructor.newInstance();	
				masterDataProvider.setAdapter(jsonAdapter);
			}
			else if(htmlAdapterClassName != null) {
				Class<?> adapterClass = Class.forName(htmlAdapterClassName);
				Constructor<?> constructor = adapterClass.getConstructor(new Class[]{});
				IHTMLAdapter htmlAdapter = (IHTMLAdapter) constructor.newInstance();	
				masterDataProvider.setAdapter(htmlAdapter);
			}
		} catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e0) {
			throw new IllegalArgumentException(e0);
		}
	}
	
	private void initData(IProviderConfiguration providerConf, String src, MasterDataProviderImpl masterDataProvider) {
		final SourceType srcType = providerConf.getSourceType();
		if(SourceType.JSON.equals(srcType)) {
			try {
				final Object parsedObject = new JSONParser().parse(src);
				final List<String> baseBaseKeySeq = providerConf.getBaseKeySequence();
				final List<JSONObject> jsonObjectList = ExtractionUtility.<JSONObject>extractCollection(JSONObject.class, (JSONObject) parsedObject, baseBaseKeySeq);
				masterDataProvider.setJSONData(jsonObjectList);
			}catch(ParseException e0) {
				throw new IllegalArgumentException(e0);
			}
		}
		else if(SourceType.HTML.equals(srcType)) {
			final Document parsedDocument = Jsoup.parse(src);
			final List<String> baseBaseKeySeq = providerConf.getBaseKeySequence();
			final List<Element> elementList = ExtractionUtility.<Element>extractCollection(Element.class, parsedDocument, baseBaseKeySeq);
			masterDataProvider.setHTMLData(elementList);
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	private void initProviders(IProviderConfiguration providerConf, MasterDataProviderImpl masterDataProvider) {
		final DefaultPersonProvider personProvider = new DefaultPersonProvider();
		{
			personProvider.startInit();
			personProvider.setMasterDataProvider(masterDataProvider);
			personProvider.finalizeInit();
		}
		final DefaultBusinessProvider businessProvider = new DefaultBusinessProvider();
		{
			businessProvider.startInit();
			businessProvider.setMasterDataProvider(masterDataProvider);
			businessProvider.finalizeInit();
		}
		final DefaultBusinessRelationProvider businessRelationProvider = new DefaultBusinessRelationProvider();
		{
			businessRelationProvider.startInit();
			businessRelationProvider.setMasterDataProvider(masterDataProvider);
			businessRelationProvider.setValueContextName(VALUE_CONTEXT_NAME_BUSINESS_RELATIONS);
			businessRelationProvider.finalizeInit();
		}
		final DefaultConcessionProvider concessionProvider = new DefaultConcessionProvider();
		{
			concessionProvider.startInit();
			concessionProvider.setMasterDataProvider(masterDataProvider);
			concessionProvider.setValueContextName(VALUE_CONTEXT_NAME_CONCESSIONS);
			concessionProvider.finalizeInit();
		}
		final DefaultPhoneNumberProvider phoneNumberProvider = new DefaultPhoneNumberProvider();
		{
			phoneNumberProvider.startInit();
			phoneNumberProvider.setMasterDataProvider(masterDataProvider);
			phoneNumberProvider.setValueContextName(VALUE_CONTEXT_NAME_PHONE_NUMBERS);
			phoneNumberProvider.finalizeInit();
		}
		final DefaultMobilePhoneNumberProvider mobilePhoneNumberProvider = new DefaultMobilePhoneNumberProvider();
		{
			mobilePhoneNumberProvider.startInit();
			mobilePhoneNumberProvider.setMasterDataProvider(masterDataProvider);
			mobilePhoneNumberProvider.setValueContextName(VALUE_CONTEXT_NAME_MOBILE_PHONE_NUMBERS);
			mobilePhoneNumberProvider.finalizeInit();
		}
		final DefaulEmailAddressProvider emailAddressProvider = new DefaulEmailAddressProvider();
		{
			emailAddressProvider.startInit();
			emailAddressProvider.setMasterDataProvider(masterDataProvider);
			emailAddressProvider.setValueContextName(VALUE_CONTEXT_NAME_EMAIL_ADDRESSES);
			emailAddressProvider.finalizeInit();
		}
		final DefaultPropertyProvider propertyProvider = new DefaultPropertyProvider();
		{
			propertyProvider.startInit();
			propertyProvider.setMasterDataProvider(masterDataProvider);
			propertyProvider.setValueContextName(VALUE_CONTEXT_NAME_PROPERTIES);
			propertyProvider.finalizeInit();
		}
		masterDataProvider.setPersonProvider(personProvider);
		masterDataProvider.setBusinessProvider(businessProvider);
		masterDataProvider.setBusinessRelationProvider(businessRelationProvider);
		masterDataProvider.setConcessionProvider(concessionProvider);
		masterDataProvider.setPhoneNumberProvider(phoneNumberProvider);
		masterDataProvider.setMobilePhoneNumberProvider(mobilePhoneNumberProvider);
		masterDataProvider.setEmailAddressProvider(emailAddressProvider);
		masterDataProvider.setPropertyProvider(propertyProvider);
	}
} 
