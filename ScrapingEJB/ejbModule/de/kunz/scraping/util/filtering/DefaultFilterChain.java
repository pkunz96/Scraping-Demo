package de.kunz.scraping.util.filtering;

import java.util.*;

final class DefaultFilterChain implements IFilterChain {

	private String chainName;
	
	private List<SortableFilter> filterList;
	
	public DefaultFilterChain() {
		super();
		this.filterList = new LinkedList<>();
	}

	@Override
	public void filter(Object obj) 
			throws FilterException {
		if(obj == null) {
			throw new NullPointerException();
		}
		else {
			for(AbstractFilter filter : filterList) {
				filter.filter(obj);
			}
		}
	}

	@Override
	public boolean isSupported(Object obj) {
		boolean isSupported = true;
		for(AbstractFilter filter : filterList) {
			if(!filter.isSupported(obj)) {
				isSupported = false;
				break;
			}
		}
		return isSupported;
	}
	
	void setFilterList(List<AbstractFilter> filterList) {
		this.filterList.clear();		
		for(AbstractFilter abstractFilter : filterList) {
			this.filterList.add(new SortableFilter(abstractFilter));
		}
		Collections.sort(this.filterList);
	}
	
	@Override
	public String getFilterChainName() {
		return chainName;
	}
	
	void setFilterChainName(String chainName) {
		this.chainName = chainName;
	}
}	
