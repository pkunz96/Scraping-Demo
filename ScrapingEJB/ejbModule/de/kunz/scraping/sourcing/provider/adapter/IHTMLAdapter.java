package de.kunz.scraping.sourcing.provider.adapter;

import org.json.simple.*;
import org.jsoup.nodes.*;

public interface IHTMLAdapter {
	Element convert(JSONObject element);
}
