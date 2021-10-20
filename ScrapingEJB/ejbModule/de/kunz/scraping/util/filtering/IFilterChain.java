package de.kunz.scraping.util.filtering;

import java.util.*;
import java.util.logging.*;

import de.kunz.scraping.conf.*;

import java.lang.reflect.*;

public interface IFilterChain {
	
	public static IFilterChain getInstance(IFilterChainConfiguration chainConf) {
		final DefaultFilterChain defaultFilterChain = new DefaultFilterChain();
		if(chainConf == null) {
			throw new NullPointerException();
		}
		else {
			Logger LOG = Logger.getLogger(IFilterChain.class.getName());
			final String filterChainName = chainConf.getFilterChainName();
			final List<AbstractFilter> filterList = new LinkedList<>();
			for(IFilterConfiguration filterConfiguration : chainConf.getFilterConfigurationsList()) { 
				if(filterConfiguration.isEnabled()) {
					final String filterClassname = filterConfiguration.getClassname();
					try {
						Class<?> filterClass = Class.forName(filterClassname);
						Constructor<?> constructor = filterClass.getConstructor(new Class[]{});
						AbstractFilter filter = (AbstractFilter) constructor.newInstance();
						filterList.add(filter);
						filter.loadConfiguration(convert(filterConfiguration.getParamterList()));
					} catch(Exception  e0) {
						e0.printStackTrace();
						LOG.log(Level.WARNING, e0.toString());
					}
				}
			} 
			defaultFilterChain.setFilterChainName(filterChainName);
			defaultFilterChain.setFilterList(filterList);
		}
		return defaultFilterChain;
	}

	private static Map<String, String> convert(List<IFilterParameter> filterParameterList) {
		 final Map<String, String> parameterMap = new HashMap<>();
		for(IFilterParameter paramter: filterParameterList) {
			final String parameterName = paramter.getName();
			final String parameterValue = paramter.getValue();
			if((parameterName != null) && (parameterValue != null)) { 
				parameterMap.put(parameterName, parameterValue);
			}
		}	
		return parameterMap;
	}
	
	void filter(Object obj)
		throws FilterException;
	
	boolean isSupported(Object obj);
	
	String getFilterChainName();
	
}
