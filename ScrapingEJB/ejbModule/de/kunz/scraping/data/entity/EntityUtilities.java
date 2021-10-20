package de.kunz.scraping.data.entity;

import java.util.*;

abstract class EntityUtilities {

	public static <T> List<T> copyList(List<T> list) {
		if(list == null) {
			return null;
		}
		else {
			List<T> resultList = new LinkedList<>();
			resultList.addAll(list);
			return resultList;
		}
	}
	
}
