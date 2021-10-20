package de.kunz.scraping.sourcing.provider;

import java.util.*;

import org.json.simple.*;
import org.jsoup.nodes.*;

import de.kunz.scraping.conf.*;
import de.kunz.scraping.util.filtering.*;

import java.beans.*;

abstract class AbstractProvider implements PropertyChangeListener {

	private static final String DEFAULT_VALUE_PARAM_NAME = "default";

	private final Map<String, IFilterChain> filterChainMap;

	public AbstractProvider() {
		super();
		this.filterChainMap = new HashMap<>();
		{
			final List<IFilterChainConfiguration> filterChainConfList = IScrapingConfiguration.getInstance()
					.getSourcingConfiguration().getFilterChainConfigurationsList();
			for (IFilterChainConfiguration filterChainConf : filterChainConfList) {
				final String filterChainName = filterChainConf.getFilterChainName();
				if (filterChainName != null) {
					final IFilterChain filterChain = IFilterChain.getInstance(filterChainConf);
					filterChainMap.put(filterChainName, filterChain);
				}
			}
		}
	}

	@Override
	public final void propertyChange(PropertyChangeEvent evt) {
		onRootObjectChanged();
	}

	/**
	 * A callback method that is invoked if and only if the underlying root object
	 * has changed.
	 */
	abstract void onRootObjectChanged();

	/**
	 * Returns true if and only if the underlying instance of
	 * {@link MasterDataProviderImpl} must be able to provide instances of
	 * {@link JSONObject}.
	 * 
	 * @return true if the underlying instance of {@link MasterDataProviderImpl}
	 *         must be able to provide instances of {@link JSONObject}.
	 */
	abstract boolean isJSONSupportRequired();

	/**
	 * Returns true if an ony if the underlying instance of
	 * {@link MasterDataProviderImpl} must be able to provide instances of
	 * {@link Element}.
	 * 
	 * @return true if the underlying instance of {@link MasterDataProviderImpl}
	 *         must be able to provide instances of {@link Element}.
	 */
	abstract boolean isHTMLSupportRequired();

	/**
	 * Returns the value that is specified by the passed value name or null if it
	 * does not exist.
	 * 
	 * @param valueName - the name of the value to retrieve.
	 * @return the value as a string or null.
	 */
	abstract String getString(String valueName);

	/**
	 * Returns the value that is specified by the passed value name or null if it
	 * does not exist.
	 * 
	 * @param valueName - the name of the value to retrieve.
	 * @return the value as a boolean or null.
	 */
	abstract Boolean getBoolean(String valueName);

	/**
	 * Returns a list of value matchting the specified value name. If no such values
	 * exist, an empty list is returned.
	 * 
	 * @param valueName - the name of the values to retrieve.
	 * @return a list of values.
	 */
	abstract List<String> getStringCollection(String valueName);

	final String getString(IValueConfiguration valueConf, JSONObject jsonObject) {
		String extractedString = null;
		final String reductionChainName = valueConf.getReductionChainName();
		if (reductionChainName == null) {
			extractedString = ExtractionUtility.<String>extract(String.class, jsonObject,
					valueConf.getRelativeKeySequence(), null);
		} else {
			final IFilterChain reductionFilterChain = this.filterChainMap.get(reductionChainName);
			if (reductionFilterChain != null) {
				extractedString = ExtractionUtility.<String>extract(String.class, jsonObject,
						valueConf.getRelativeKeySequence(), reductionFilterChain);
			}
		} 
		extractedString = getDefaultValueIfNull(valueConf, extractedString);
		extractedString = applyFilterChainToStringIfNotNull(extractedString, valueConf.getFilterChainName());
		return extractedString;
	}

	final String getString(IValueConfiguration valueConf, Element element) {
		String extractedString = null;
		final String reductionChainName = valueConf.getReductionChainName();
		if (reductionChainName == null) { 
			extractedString = ExtractionUtility.<String>extract(String.class, element,
					valueConf.getRelativeKeySequence(), null);
		}
		else {
			final IFilterChain reductionFilterChain = this.filterChainMap.get(reductionChainName);
			if (reductionFilterChain != null) {
				extractedString = ExtractionUtility.<String>extract(String.class, element,
						valueConf.getRelativeKeySequence(), reductionFilterChain);
			}
		} 
		extractedString = getDefaultValueIfNull(valueConf, extractedString);
		extractedString = applyFilterChainToStringIfNotNull(extractedString, valueConf.getFilterChainName());
		return extractedString;
	}

	final Boolean getBoolean(IValueConfiguration valueConf, JSONObject jsonObject) {
		Boolean extractedBoolean = null;
		final String reductionChainName = valueConf.getReductionChainName();
		if (reductionChainName == null) {
			extractedBoolean = ExtractionUtility.<Boolean>extract(Boolean.class, jsonObject, valueConf.getRelativeKeySequence(), null);
		} else {
			final IFilterChain reductionFilterChain = this.filterChainMap.get(reductionChainName);
			if (reductionFilterChain != null) {
				extractedBoolean = ExtractionUtility.<Boolean>extract(Boolean.class, jsonObject, valueConf.getRelativeKeySequence(), reductionFilterChain);
			}
		}
		extractedBoolean = getDefaultBooleanValueIfNull(valueConf, extractedBoolean);
		return extractedBoolean;
	}

	final Boolean getBoolean(IValueConfiguration valueConf, Element element) {
		Boolean extractedBoolean = null;
		final String reductionChainName = valueConf.getReductionChainName();
		if (reductionChainName == null) {
			extractedBoolean = ExtractionUtility.<Boolean>extract(Boolean.class, element, valueConf.getRelativeKeySequence(), null);
		} else {
			final IFilterChain reductionFilterChain = this.filterChainMap.get(reductionChainName);
			if (reductionFilterChain != null) {
				extractedBoolean = ExtractionUtility.<Boolean>extract(Boolean.class, element, valueConf.getRelativeKeySequence(), reductionFilterChain);
			}
		}
		extractedBoolean = getDefaultBooleanValueIfNull(valueConf, extractedBoolean);
		return extractedBoolean;
	}

	final List<String> getStringCollection(IValueConfiguration valueConf, JSONObject jsonObject) {
		List<String> extractedCollection = new LinkedList<>(); // Return empty list. 
		extractedCollection = ExtractionUtility.<String>extractCollection(String.class, jsonObject, valueConf.getRelativeKeySequence());
		insertDefaultValueIfEmpty(valueConf, extractedCollection);
		extractedCollection = applyFilterChainToStringCollectionIfNotNull(extractedCollection, valueConf.getFilterChainName());
		return extractedCollection;
	}

	final List<String> getStringCollection(IValueConfiguration valueConf, Element element) {
		List<String> extractedCollection = new LinkedList<>(); // Return empty list. 
		extractedCollection = ExtractionUtility.<String>extractCollection(String.class, element, valueConf.getRelativeKeySequence());
		insertDefaultValueIfEmpty(valueConf, extractedCollection);
		extractedCollection = applyFilterChainToStringCollectionIfNotNull(extractedCollection, valueConf.getFilterChainName());
		return extractedCollection;	}

	private String getDefaultValueIfNull(IValueConfiguration valueConf, String extractedString) {
		if (extractedString == null) {
			return valueConf.getParameterMap().get(DEFAULT_VALUE_PARAM_NAME);
		} else {
			return extractedString;
		}
	}

	private Boolean getDefaultBooleanValueIfNull(IValueConfiguration valueConf, Boolean extractedBoolean) {
		if (extractedBoolean == null) {
			final String defaultValueStr = valueConf.getParameterMap().get(DEFAULT_VALUE_PARAM_NAME);
			if (defaultValueStr != null) {
				if (defaultValueStr.toLowerCase().matches("true")) {
					return true;
				} else if (defaultValueStr.toLowerCase().matches("false")) {
					return false;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return extractedBoolean;
		}
	}

	private void insertDefaultValueIfEmpty(IValueConfiguration valueConf, List<String> extractedCollection) {
		if (extractedCollection != null) {
			if (extractedCollection.isEmpty()) {
				final String defaultValue = valueConf.getParameterMap().get(DEFAULT_VALUE_PARAM_NAME);
				if (defaultValue != null) {
					extractedCollection.add(defaultValue);
				}
			}
		}
	}

	private String applyFilterChainToStringIfNotNull(String str, String filterChainName) {
		String result = null;
		try {
			if (str != null) {
				StringBuilder strBuilder = new StringBuilder(str);
				strBuilder = this.<StringBuilder>applyFilterChainIfNotNull(strBuilder, filterChainName);
				result = (strBuilder == null) ? null : strBuilder.toString();
			}
		} catch (FilterException e0) {
			result = null;
		}		
		return result;
	}

	private List<String> applyFilterChainToStringCollectionIfNotNull(List<String> strCollection,
			String filterChainName) {
		List<String> result;
		try {
			result = applyFilterChainIfNotNull(strCollection, filterChainName);

		} catch (FilterException e0) {
			result = null;
		}
		return result;
	}

	private <T> T applyFilterChainIfNotNull(T obj, String filterChainName) throws FilterException {
		T result = null;
		if ((obj == null) || (filterChainName == null)) {
			result = obj;

		} else {
			final IFilterChain filterChain = this.filterChainMap.get(filterChainName);
			if ((filterChain != null) && (filterChain.isSupported(obj))) {
				filterChain.filter(obj);
				result = obj;
			}
		} 
		return result;
	}

}
