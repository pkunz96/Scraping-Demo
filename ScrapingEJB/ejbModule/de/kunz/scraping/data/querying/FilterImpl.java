package de.kunz.scraping.data.querying;

import java.util.List;

final class FilterImpl<T> implements IFilter<T> {

	@Override
	public boolean filter(T queryable) {
		return true;
	}

	@Override
	public void filter(List<T> queryables) {
		return;
	}

}
  