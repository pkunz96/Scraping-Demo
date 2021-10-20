package de.kunz.scraping.conf;

import java.net.Proxy.Type;

public interface IProxyConfiguration {
	
	Type getType();
	
	void setType(Type type);
	
	String getHostname();
	
	void setHostname(String hostname);
	
	int getPort();
	
	void setPort(int port);
	
	boolean isEnabled();
	
	void enable();
	
	void disable();
		
}
   