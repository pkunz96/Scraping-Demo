package de.kunz.scraping.util.filtering;

import java.util.Map;

final class SortableFilter extends AbstractFilter implements Comparable<Object> {

	private final AbstractFilter filter;
	
	public SortableFilter(AbstractFilter filter) {
		if(filter == null) {
			throw new NullPointerException();
		}
		else {
			this.filter = filter;
		}
	}

	@Override
	protected void filter(Object obj) 
			throws FilterException {
		this.filter.filter(obj);
	}

	@Override
	protected boolean isSupported(Object obj) {
		return this.filter.isSupported(obj);
	}

	@Override
	protected void loadConfiguration(Map<String, String> filterConfiguration) {
		this.filter.loadConfiguration(filterConfiguration);
	}
	
	@Override
	public int compareTo(Object o) {
		if(o == null) {
			return -1;
		}
		else if(!(o instanceof AbstractFilter)) {
			return -1;
		}
		else {
			AbstractFilter filter = (AbstractFilter) o;
			if(this.getPriorty() < filter.getPriorty()) {
				return -1;
			}
			else if(this.getPriorty() == filter.getPriorty()) {
				return 0;
			}
			else {
				return -1;
			}
		}
	}
}
