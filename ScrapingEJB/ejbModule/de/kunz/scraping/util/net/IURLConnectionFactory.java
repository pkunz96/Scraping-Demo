package de.kunz.scraping.util.net;

import java.io.*;

public interface IURLConnectionFactory {
	
	public static IURLConnectionFactory getInstance() {
		return new URLConnectionFactoryImpl();
	}
	
	URLConnection getURLConnection(String url) 
			throws IOException;
	
	URLConnection getURLConnection(String url, String providerProfileName)
			throws IOException, InterruptedException;
}
      