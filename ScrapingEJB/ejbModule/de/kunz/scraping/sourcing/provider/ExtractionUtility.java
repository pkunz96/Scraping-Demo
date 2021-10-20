package de.kunz.scraping.sourcing.provider;

import java.util.*;
import java.util.logging.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import de.kunz.scraping.util.filtering.*;
 
abstract class ExtractionUtility {
	
	private static boolean isCSSID(String key) {
		return (key != null) && (key.startsWith("#"));
	}
	
	private static boolean isCSSClass(String key) {
		return (key != null) && (key.startsWith("."));
	}
	
	private static boolean isHTMLTag(String key) {
		return (key != null) && (key.startsWith("$"));
	}
	
	private static String removePrefix(String key) {
		if(isCSSID(key)) {
			return key.replaceFirst("#", "");
		}
		else if(isCSSClass(key)) {
			return key.replaceFirst(".", "");
		}
		else if(isHTMLTag(key)) {
			return key.replaceFirst("$", "");
		}
		else {
			return key;
		}
	}

	private static Logger LOG = Logger.getLogger(ExtractionUtility.class.getName());
	
	public static <T> T extract(Class<T> expectedType, JSONObject jsonObj, List<String> keySeq, IFilterChain filterChain) {
		return extractValue(expectedType, jsonObj, keySeq, filterChain);
	}
	
	public static <T> T extract(Class<T> expectedType, Element curElement, List<String> keySeq, IFilterChain filterChain) {
		return extractValue(expectedType, curElement, keySeq, filterChain);
	}
	
	private static <T> T extractValue(Class<T> expectedType, Object src, List<String> keySeq, IFilterChain filterChain) {
		T extractedValue = null;
		final List<T> extractedObjectList;
		if(src instanceof Element) {
			final Element elementSrc = (Element) src;
			extractedObjectList = extractCollection(expectedType, elementSrc, keySeq);
		}
		else if(src instanceof JSONObject) {
			final JSONObject jsonObjSrc = (JSONObject) src;
			extractedObjectList = extractCollection(expectedType, jsonObjSrc, keySeq);
		}
		else {
			throw new IllegalArgumentException();
		}
		{
			final int ONE_ELEMENT = 1;
			final int FIRST_ELEMENT = 0;
			if(extractedObjectList.size() == ONE_ELEMENT) {
				extractedValue = extractedObjectList.get(FIRST_ELEMENT);
			}
			else if((extractedObjectList.size() > ONE_ELEMENT) 
					&& (filterChain != null) && (filterChain.isSupported(extractedObjectList))){
				try {
					filterChain.filter(extractedObjectList);
					if(extractedObjectList.size() == ONE_ELEMENT) {
						extractedValue = extractedObjectList.get(FIRST_ELEMENT);
					}
				}catch(FilterException e0) {
					LOG.log(Level.FINEST, e0.getMessage());
				}
			}
		}
		return extractedValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> extractCollection(Class<T> expectedType, JSONObject jsonObj , List<String> keySeq) {
		final List<T> valueList = new LinkedList<>();
		final int ONE_ELEMENT = 1;
		final int NEXT_KEY = 0;
		if(jsonObj == null) {
			throw new NullPointerException();
		}
		else if(keySeq.isEmpty()){
			T arrValueAsT = ExtractionUtility.<T>convertTo(expectedType, jsonObj);
			if(arrValueAsT != null) {
				valueList.add(arrValueAsT);
			}
		}
		else if(keySeq.size() == ONE_ELEMENT) {
			String nextKey = keySeq.get(NEXT_KEY);
			final Object objForKey = jsonObj.get(nextKey);
			if(objForKey instanceof JSONArray) { 
				final JSONArray jsonValueArr = (JSONArray) objForKey;
				for(Object arrValue : jsonValueArr) {
					if(expectedType.isInstance(arrValue)) {
						valueList.add((T) arrValue);
					}
					else {
						T arrValueAsT = ExtractionUtility.<T>convertTo(expectedType, arrValue);
						if(arrValueAsT != null) {
							valueList.add(arrValueAsT);
						}
					}
				}
			}
			else if(expectedType.isInstance(objForKey)) {
				valueList.add((T)objForKey);
			} 
			else {
				T arrValueAsT = ExtractionUtility.<T>convertTo(expectedType, objForKey);
				if(arrValueAsT != null) {
					valueList.add(arrValueAsT);
				}
			}
		}
		else {
			final String nextKey = keySeq.get(NEXT_KEY);
			final Object objForKey = jsonObj.get(nextKey);
			if(objForKey instanceof JSONObject) { 
				JSONObject nextJSONObj = (JSONObject) objForKey;
				keySeq.remove(NEXT_KEY);
				valueList.addAll(extractCollection(expectedType, nextJSONObj, keySeq));
			}	
			else if(objForKey instanceof JSONArray) {
				JSONArray nextJSONArr = (JSONArray) objForKey;
				for(Object arrObj : nextJSONArr) {
					if(arrObj instanceof JSONObject) {
						keySeq.remove(NEXT_KEY);
						final JSONObject nextJSONObj = (JSONObject)arrObj;
						valueList.addAll(extractCollection(expectedType, nextJSONObj, keySeq));
					}
				}
			}
		}
		return valueList;
	}	
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> extractCollection(Class<T> expectedType, Element curElement, List<String> keySeq) {
		final List<T> valueList = new LinkedList<>();
		final int ONE_ELEMENT = 1;
		final int NEXT_KEY = 0;
		if(curElement == null) {
			throw new NullPointerException();
		}
		else if(keySeq.isEmpty()){
			T arrValueAsT = ExtractionUtility.<T>convertTo(expectedType, curElement);
			if(arrValueAsT != null) {
				valueList.add(arrValueAsT);
			} 
		}
		else if(keySeq.size() == ONE_ELEMENT) {
			String nextKey = keySeq.get(NEXT_KEY);
			final List<Element> elementList = new LinkedList<>();
			if(isCSSID(nextKey)) {
				final Element lastElement = curElement.getElementById(removePrefix(nextKey));
				if(lastElement != null) {
					elementList.add(lastElement);
				}
			}
			else if(isCSSClass(nextKey)) {
				final Elements lastElements = curElement.getElementsByClass(removePrefix(nextKey));
				elementList.addAll(lastElements);
			}
			else if(isHTMLTag(nextKey)) {
				final Elements lastElements = curElement.getElementsByTag(removePrefix(nextKey));
				elementList.addAll(lastElements);
			}
			for(Element element : elementList) {
				T value;
				if(expectedType.isInstance(element)) {
					value = (T) element;
				}
				else {
					value = convertTo(expectedType, element.text());
				}
				if(value != null) {
					valueList.add(value);
				}
			}
  		}
		else {
			String nextKey = keySeq.get(NEXT_KEY);
			keySeq.remove(NEXT_KEY);
			if(isCSSID(nextKey)) {
				Element nextElement = curElement.getElementById(removePrefix(nextKey));
				if(nextElement != null) {
					valueList.addAll(extractCollection(expectedType, nextElement, keySeq));
				}
			}
			else if (isCSSClass(nextKey)) {
				Elements nextElements = curElement.getElementsByClass(removePrefix(nextKey));
				for(Element nextElement : nextElements) {
					valueList.addAll(extractCollection(expectedType, nextElement, keySeq));
				}
			}
			else if(isHTMLTag(nextKey)) {
				Elements nextElements = curElement.getElementsByTag(removePrefix(nextKey));
				for(Element nextElement : nextElements) {
					valueList.addAll(extractCollection(expectedType, nextElement, keySeq));
				}
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		return valueList; 
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T convertTo(Class<T> targetType,  Object obj) {
		if(targetType == null) {
			throw new NullPointerException();
		}
		else if((obj == null)) {
			return null;
		}
		else if(targetType.equals(obj.getClass())) {
			return (T) obj;
		}
		else if(String.class.equals(targetType)) {
			return (T) obj.toString();
		}
		else if(obj instanceof String) {
			final String objAsString = (String) obj;
			if(Integer.class.equals(targetType)) {
				Integer objAsInteger;
				try {
			 		objAsInteger = Integer.parseInt(objAsString);
				}catch(NumberFormatException e0) {
					objAsInteger = null;
				}
				return (T) objAsInteger;
			}
			else if(Float.class.equals(targetType)) {
				Float objAsFloat;
				try {
					objAsFloat = Float.parseFloat(objAsString);
					
				}catch(NumberFormatException e0) {
					objAsFloat = null;
				}
				return (T) objAsFloat;
			}
			else if(Double.class.equals(targetType)) {
				Double objAsDouble;
				try {
					objAsDouble = Double.parseDouble(objAsString);
				}catch(NumberFormatException e0) {
					objAsDouble = null;
				}
				return (T) objAsDouble;
			}
			else if(Boolean.class.equals(targetType)) {
				Boolean objAsBoolean;
				if(objAsString.toLowerCase().matches("true")) {
					objAsBoolean = true;
				}
				else if(objAsString.toLowerCase().matches("false")) {
					objAsBoolean = false;
				}
				else {
					objAsBoolean = null;
				}
				return (T) objAsBoolean;
			}
			else if(JSONObject.class.equals(targetType)) {
				JSONObject objAsJSONObject;
				try {
					objAsJSONObject = (JSONObject) new JSONParser().parse("{\"value\":" + "\"" +  objAsString + "\"}");
				} catch (ParseException e) {
					objAsJSONObject = null;
				}
				return (T) objAsJSONObject;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
 	}
	
	private ExtractionUtility() {}
}