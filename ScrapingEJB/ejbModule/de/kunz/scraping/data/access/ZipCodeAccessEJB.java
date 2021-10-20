package de.kunz.scraping.data.access;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import javax.ejb.*;

import javax.annotation.*;

import de.kunz.scraping.data.access.CountryAccessEJBLocal.CountryIdentifier;
import de.kunz.scraping.data.entity.*;

@Singleton 
@LocalBean
public class ZipCodeAccessEJB implements ZipCodeAccessEJBLocal {
			
	private static final Logger LOG = Logger.getLogger(ZipCodeAccessEJB.class.getName());
	
	private static final String ZIP_CODE_FILE_PATH = "./conf/de/exp/ai/scraping/zipCodes.csv";
	
	private static List<ZipCode> fetchZipCodes(Map<String, Country> countryCodeCountryMap)
			throws IOException {
		final List<ZipCode> zipCodeList = new LinkedList<>();
		final File zipCodeRawFile = new File(ZIP_CODE_FILE_PATH);
		final boolean exists = (zipCodeRawFile.exists() || zipCodeRawFile.createNewFile());
		if(exists) {
			BufferedReader reader = null;
			try { 
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(zipCodeRawFile)));
				String tmp;
				final String DELIMITER = ",";
				while((tmp = reader.readLine()) != null) {
					String[] record = tmp.split(DELIMITER);
					if(record.length == 5) {
						try {
							final Country country = countryCodeCountryMap.get(record[0]);
							final String zipCodeStr = record[1];
							final String townStr = record[2]; 
							final double longitude = Double.parseDouble(record[3]);
							final double latitude = Double.parseDouble(record[4]);
							if(country != null) {
								zipCodeList.add(new ZipCode(country,zipCodeStr, townStr, longitude, latitude, false));
							}
						}catch (NullPointerException | NumberFormatException e0) {
							e0.printStackTrace();
							LOG.warning(e0.getMessage());
						}
					}
				}
			}  
			finally {
				if(reader != null) { 
					reader.close();
				}
			}
		}
		return zipCodeList;
	}
	
	private final Map<String, Map<String, ZipCode>> countryIDzipCodeStrZipCodeMapMap;

	public ZipCodeAccessEJB() {
		this.countryIDzipCodeStrZipCodeMapMap = new ConcurrentHashMap<String, Map<String, ZipCode>>();
	}
	
	@EJB
	private CountryAccessEJB countryAccess;
		
	@Override
	public List<ZipCode> getZipCodes() 
			throws IOException {
		final List<ZipCode> zipCodeList = new LinkedList<>();
		for(Map<String, ZipCode> zipCodeStrZipCodeMap: this.countryIDzipCodeStrZipCodeMapMap.values()) {
			zipCodeList.addAll(zipCodeStrZipCodeMap.values());
		}
		return zipCodeList;
	}
 	
	@Override
	public List<ZipCode> getZipCodes(CountryIdentifier countryID) 
			throws IOException {
		final List<ZipCode> zipCodeList = new LinkedList<>();
		if(countryID == null) {
			throw new NullPointerException();
		}
		else {
			final String countryCode = countryID.getCountryCode();
			final Map<String, ZipCode> zipCodeStrZipCodeMap = this.countryIDzipCodeStrZipCodeMapMap.get(countryCode);
			if(zipCodeStrZipCodeMap != null) {
				zipCodeList.addAll(zipCodeStrZipCodeMap.values());
			}
		}
		return zipCodeList;
	}

	@Override
	public double getDistance(String firstCountryCode, String firstZipCodeStr, String secondCountryCode, String secondZipCodeStr) {
		double distance = -1;
		if(firstCountryCode == null || firstZipCodeStr == null || secondCountryCode == null || secondZipCodeStr == null) {
			throw new NullPointerException();
		}
		else {
			final Map<String, ZipCode> firstZipCodeStrZipCodeMap = this.countryIDzipCodeStrZipCodeMapMap.get(firstCountryCode);
			final Map<String, ZipCode> secondZipCodeStrZipCodeMap = this.countryIDzipCodeStrZipCodeMapMap.get(secondCountryCode);
			if((firstZipCodeStrZipCodeMap != null) && (secondZipCodeStrZipCodeMap != null)) {
				final ZipCode firstZipCode = firstZipCodeStrZipCodeMap.get(firstZipCodeStr);
				final ZipCode secondZipCode = secondZipCodeStrZipCodeMap.get(secondZipCodeStr);
				if((firstZipCode != null) && (secondZipCode != null)) {
					double firstLatitude = firstZipCode.getLatitude();
					double firstLongitude = firstZipCode.getLongitude();
					double secondLatitude = secondZipCode.getLatitude();
					double secondLongitude = secondZipCode.getLongitude();
				    final double R = 6372.8; 
			        double dLat = Math.toRadians(secondLatitude - firstLatitude);
			        double dLon = Math.toRadians(secondLongitude - firstLongitude);
			        firstLatitude = Math.toRadians(firstLatitude);
			        secondLatitude = Math.toRadians(secondLatitude);
			        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(firstLatitude) * Math.cos(secondLatitude);
			        double c = 2 * Math.asin(Math.sqrt(a));
			        distance = R * c;
 				}
			}
		}
		System.out.println("first: " + firstZipCodeStr + " second: " + secondZipCodeStr + " distance: " + distance);
		return distance;
	}	
	
	@Override
	public List<ZipCode> getZipCodePatterns() 
			throws IOException {
		final List<ZipCode> zipCodePatterns = new LinkedList<>();
		for(CountryIdentifier countryID : CountryIdentifier.getCountryIdentifiers()) {
			zipCodePatterns.addAll(getZipCodePatterns(countryID));

		}
		return zipCodePatterns;
	}

	@Override
	public List<ZipCode> getZipCodePatterns(CountryIdentifier countryID) 
			throws IOException {
		final List<ZipCode> zipCodePatterns = new ArrayList<>();
		final Set<String> addedPatterns = new HashSet<>();
		try {
			final List<ZipCode> zipCodeList = getZipCodes(countryID);
			for(ZipCode zipCode : zipCodeList) {
				final String zipCodeStr = zipCode.getZipCode();
				final int zipCodeLength = zipCodeStr.length();
				for(int ignoredDigitCount = 1; ignoredDigitCount <= 3; ignoredDigitCount++) {
					final int minCharIndex = zipCodeLength - ignoredDigitCount;
					final StringBuilder patternBuilder = new StringBuilder(zipCodeStr);
					for(int index = minCharIndex; index < zipCodeLength; index++) {
						patternBuilder.setCharAt(index, 'x');
					}
					
					final String patternStr = patternBuilder.toString(); 
					if(!addedPatterns.contains(patternStr)) {  
						final ZipCode pattern = new ZipCode(zipCode);
						pattern.setZipCode(patternStr); 
						pattern.setPattern(true); 
						zipCodePatterns.add(pattern); 
						addedPatterns.add(patternStr);
					}  
				} 
			}			   
		} catch(IOException e0) {   
			LOG.warning(e0.getMessage()); 
		} 
		return zipCodePatterns;
	}
 
	@PostConstruct 
	private void init()  {
		try {
			final List<ZipCode> zipCodeList = fetchZipCodes(getCountryCodeCountryMap());
			for(ZipCode zipCode: zipCodeList) {
				final Country country;
				final String countryCode;
				if((country = zipCode.getCountry()) != null && ((countryCode = country.getCountryCode()) != null)) {
					Map<String, ZipCode> zipCodeStrZipCodeMap = this.countryIDzipCodeStrZipCodeMapMap.get(countryCode);
					if(zipCodeStrZipCodeMap == null) {
						zipCodeStrZipCodeMap = new HashMap<>();
						this.countryIDzipCodeStrZipCodeMapMap.put(countryCode, zipCodeStrZipCodeMap);
					}
					final String zipCodeStr = zipCode.getZipCode();
					zipCodeStrZipCodeMap.put(zipCodeStr, zipCode);
				}
			}
		}catch(IOException e0) {
			throw new IllegalStateException(e0);
		}
	}
	
	private Map<String, Country> getCountryCodeCountryMap()
			throws IOException {
		final Map<String, Country> countryCodeCountryMap = new HashMap<>();
		{
			final List<Country> countryList = this.countryAccess.getCountries();
			for(Country country : countryList) {
				final String countryCode = country.getCountryCode();
				if(countryCode != null) {
					countryCodeCountryMap.put(countryCode, country);
				}
			}
		}
		return countryCodeCountryMap;
	}
}
  
