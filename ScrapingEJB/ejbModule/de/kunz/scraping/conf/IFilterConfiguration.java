package de.kunz.scraping.conf;

import java.util.*;

public interface IFilterConfiguration {

	String getName();
	
	void setName(String name);
	
	String getClassname();
	
	void setClassname(String classname);
	
	int getPriority();
	
	void setPrioty(int priority);
	
	boolean isEnabled();
	
	void enable();
	
	void disbale();
	
	List<IFilterParameter> getParamterList();
	
	IFilterParameter addParameter(String name, String value);
	
	boolean removeParamter(String name);
}
