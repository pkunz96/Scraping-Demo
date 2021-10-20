package de.kunz.scraping.sourcing.provider.adapter;

import org.json.simple.*;
import org.jsoup.nodes.*;


public interface IJSONAdapter {
	JSONObject convert(Element element);
}
