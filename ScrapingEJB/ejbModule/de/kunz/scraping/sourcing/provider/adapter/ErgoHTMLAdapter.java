package de.kunz.scraping.sourcing.provider.adapter;

import java.io.*;
import java.util.logging.Logger;

import org.json.simple.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import de.kunz.scraping.util.net.*;

import org.jsoup.*;

public class ErgoHTMLAdapter implements IHTMLAdapter {

	private static final String JSON_KEY_HOMEPAGE = "homepage";
	
	private static final String IMPRINT_SUFFIX = "/de/Agentur/Erstkontaktinformationen";
	
	private static final Logger LOG = Logger.getLogger(DEVKHtmlAdapter.class.getName());
	
	private final Element EMPTY_RESULT;
	
	public ErgoHTMLAdapter() {
		final String EMPTY_RESULT_HTML_STR = "<div></div>";
		EMPTY_RESULT = Jsoup.parse(EMPTY_RESULT_HTML_STR);
	}
	
	@Override
	public Element convert(JSONObject element) {
		Element result = EMPTY_RESULT;
		if(element != null) {
			final String homepageUrl = getHomepageUrl(element); 
			final String imprintUrl = getImprintUrl(element);
			if(homepageUrl != null && imprintUrl != null) {
				try {
					final String homepageResponse = read(homepageUrl);
					final String imprintResponse = read(imprintUrl);
					final Element homepageElement = Jsoup.parse(homepageResponse);
					final Element imprintElement = Jsoup.parse(imprintResponse);
					final Elements hompageBodyChildren = homepageElement.selectFirst("body").children();
					final Elements imprintBodyChildren = imprintElement.selectFirst("body").children();
					String resultStr = "<html><head></head><body>";
					if(hompageBodyChildren.size() > 0) {
						resultStr = resultStr + "<div id=\"homepage\">";
						for(Element hompageBodyChild : hompageBodyChildren) {
							resultStr = resultStr + hompageBodyChild.toString();
						}
						resultStr = resultStr + "</div>";
					}
					if(imprintBodyChildren.size() > 0) {
						resultStr = resultStr + "<div id=\"imprint\">";
						for(Element imprintBodyChild : imprintBodyChildren) {
							resultStr = resultStr + imprintBodyChild.toString();
						}
						if(resultStr.contains("E-Mail:")) {
							int emailBeginIndex = resultStr.indexOf("E-Mail:");
							int emailEndIndex = resultStr.indexOf("@ergo.de");
							if(emailBeginIndex != -1) {
								emailBeginIndex = emailBeginIndex + 7;
								emailEndIndex = emailEndIndex + 8;
								if(emailBeginIndex < emailEndIndex && emailEndIndex < resultStr.length()) {
									String email = resultStr.substring(emailBeginIndex, emailEndIndex);
									resultStr = resultStr + "<div id=\"emails\"><div id=\"email\">";
									resultStr = resultStr +  email + "</div></div>";
								} 
							}
						}
						if(resultStr.contains("unter der Register-Nr.")) {
							int registerNoBeginIndex = resultStr.indexOf("unter der Register-Nr.");
							int registerNoEndIndex = resultStr.indexOf("eingetragen.");
							if(registerNoBeginIndex != -1) {
								registerNoBeginIndex = registerNoBeginIndex + 22;
								if(registerNoBeginIndex < registerNoEndIndex && registerNoEndIndex < resultStr.length()) {
									String registerNo = resultStr.substring(registerNoBeginIndex, registerNoEndIndex);
									resultStr = resultStr + "<div id=\"registerNumbers\"><div id=\"registerNumber\">";
									resultStr = resultStr +  registerNo + "</div></div>";
								} 
							}
						}
						resultStr = resultStr + "</div>";
					}
					result = Jsoup.parse(resultStr);
				}catch(IOException e0) { 
					LOG.info(e0.getMessage());
				}   
			}    
		} 
		return result;  
	}  
	 
	private String getImprintUrl(JSONObject element) {
		String imprintUrl = null;
		final String homepageUrl = getHomepageUrl(element);
		if(homepageUrl != null) {
			imprintUrl = homepageUrl + IMPRINT_SUFFIX; 
		}
		return imprintUrl;
	}
	
	private String getHomepageUrl(JSONObject element) {
		String homepageUrl = null;
		final Object curObj = element.get(JSON_KEY_HOMEPAGE);
		if(curObj instanceof String) {
			homepageUrl = "https://" + ((String) curObj);
		}
		return homepageUrl;
	}
		
	private String read(String urlStr) 
			throws IOException {
		StringBuilder response = new StringBuilder("");
    	try (URLConnection connection =IURLConnectionFactory.getInstance().getURLConnection(urlStr, "ergo"); InputStream in = connection.getInputStream();)
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
   