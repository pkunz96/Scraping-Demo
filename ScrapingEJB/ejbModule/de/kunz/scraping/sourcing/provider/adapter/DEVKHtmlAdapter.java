package de.kunz.scraping.sourcing.provider.adapter;

import java.io.*;
import java.util.logging.*;

import org.jsoup.*; 
import org.json.simple.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import de.kunz.scraping.util.net.IURLConnectionFactory;
import de.kunz.scraping.util.net.URLConnection;

public class DEVKHtmlAdapter implements IHTMLAdapter {

	private static final String JSON_KEY_LOC = "loc";
	
	private static final String JSON_KEY_URLS = "urls";
	
	private static final String JSON_KEY_EXTERNAL = "external";
	
	private static final String JSON_KEY_DISPLAY_URL = "displayUrl";
	
	private static final String JSON_KEY_CUSTOM_BY_NAME = "customByName";
	
	private static final String JSON_KEY_TITLE = "TITEL";
	
	private static final String IMPRINT_SUFFIX = "/footer/rechtliches/impressum/index.jsp";
	
	private static final Logger LOG = Logger.getLogger(DEVKHtmlAdapter.class.getName());
	
	private final Element EMPTY_RESULT;
	
	public DEVKHtmlAdapter() {
		final String EMPTY_RESULT_HTML_STR = "<div></div>";
		EMPTY_RESULT = Jsoup.parse(EMPTY_RESULT_HTML_STR);
	}
	
	@Override
	public Element convert(JSONObject element) {
		Element result = EMPTY_RESULT;
		if(element != null) {
			final String imprintUrl = getImprintUrl(element);
			final String title = getTitle(element);
			if(imprintUrl != null && !imprintUrl.startsWith("https://www.devk.de")) {
				try {
					String response = read(imprintUrl);
					result = Jsoup.parse(response);
					final Elements children = result.selectFirst("body").children();
					final int numberOfChildren = children.size();
					if(title != null && numberOfChildren > 0) {
						String contentStr = "";
						for(Element child : children) {
							contentStr = contentStr + child.toString() + "\n";	
						}
						contentStr = contentStr + "<div id=\"jobtitle\">"+ title + "</div>";
						contentStr = contentStr + "<div id=\"legalStatus\">Handelvertreter nach § 84 HGB</div>";
						result = Jsoup.parse("<html><head></head><body><div id=\"imprint\">" + contentStr + "</div></body></html>");
					}
				}catch(IOException e0) { 
					LOG.info(e0.getMessage());
				} 
			}    
			else {
				result = Jsoup.parse("<html><head></head><body><div id=\"imprint\"><div id=\"jobtitle\">"+ title + "</div><div id=\"legalStatus\">Angestellter</div></div></body></html>");
			}
		} 
		return result;  
	}  
	
	private String getImprintUrl(JSONObject element) {
		String imprintUrl = null;
		Object curObj = element.get(JSON_KEY_LOC);
		if(curObj instanceof JSONObject) {
			curObj = ((JSONObject) curObj).get(JSON_KEY_URLS);
			if(curObj instanceof JSONObject) {
				curObj = ((JSONObject) curObj).get(JSON_KEY_EXTERNAL);
				if(curObj instanceof JSONObject) {
					curObj = ((JSONObject) curObj).get(JSON_KEY_DISPLAY_URL);
					if(curObj instanceof String) {
						imprintUrl = ((String) curObj) + IMPRINT_SUFFIX; 
						imprintUrl = imprintUrl.replace("http:", "https:");
					}
				}
			}
		}
		return imprintUrl;
	}
	
	private String getTitle(JSONObject element) {
		String title = null;
		Object curObj = element.get(JSON_KEY_LOC);
		if(curObj instanceof JSONObject) {
			curObj = ((JSONObject) curObj).get(JSON_KEY_CUSTOM_BY_NAME);
			if(curObj instanceof JSONObject) {
				curObj = ((JSONObject) curObj).get(JSON_KEY_TITLE);
				if(curObj instanceof String) {
					title = ((String) curObj); 
				}
			}
		}
		return title;
	}
	
	private String read(String urlStr) 
			throws IOException {
		StringBuilder response = new StringBuilder("");
    	try (URLConnection connection =IURLConnectionFactory.getInstance().getURLConnection(urlStr, "devk"); InputStream in = connection.getInputStream();)
    	{
    		final BufferedReader bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	String tmp; 
	    	while((tmp = bufReader.readLine()) != null) {
	    		response.append(tmp);
	    	}
    	} catch(InterruptedException e0) {
    		Thread.currentThread().interrupt();
    	}
		return response.toString();
	}
}
