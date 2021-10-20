package de.kunz.scraping.util.net;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import de.kunz.scraping.conf.*;

final class URLConnectionFactoryImpl implements IURLConnectionFactory {
	
	private static class URLConnectionSemaphore extends Semaphore {
		
		private static final long serialVersionUID = 495989249237502814L;
		
		private final int initialPermits;
		
		public URLConnectionSemaphore(int permits) {
			super(permits, true);
			this.initialPermits = permits;
		}
		
		public int initialPermits() {
			return initialPermits;
		}
	}
	
	private static class ConnectionLimitSemaphoreMapMaintanance extends Thread {
		
		private final Map<String, URLConnectionSemaphore> connectionLimitSemaphoreMap;
		
		public ConnectionLimitSemaphoreMapMaintanance(Map<String, URLConnectionSemaphore> connectionLimitSemaphoreMap) {
			if(connectionLimitSemaphoreMap == null) {
				throw new NullPointerException();
			}
			else {
				this.connectionLimitSemaphoreMap = connectionLimitSemaphoreMap;
			}
		}
		
		@Override
		public void run() {
			while(true) {
				synchronized (connectionLimitSemaphoreMap) {
					for(String profileName : this.connectionLimitSemaphoreMap.keySet()) {
						final URLConnectionSemaphore semaphore = this.connectionLimitSemaphoreMap.get(profileName);
						if((semaphore != null) && (semaphore.availablePermits() == semaphore.initialPermits())) {
							this.connectionLimitSemaphoreMap.remove(profileName);
						}
					}
				}
				try {
					final int ONE_SECOND = 1000;
					Thread.sleep(ONE_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} 
			}
		}
	}
		
	private static final Map<String, URLConnectionSemaphore> connectionLimitSemaphoreMap;
	
	static {
		connectionLimitSemaphoreMap = new ConcurrentHashMap<>();
		new ConnectionLimitSemaphoreMapMaintanance(connectionLimitSemaphoreMap).start();
	}
	
	private final IScrapingConfiguration scrapingConfiguration;
	private final ISourcingConfiguration sourcingConfiguration;
	
	public URLConnectionFactoryImpl() {
		this.scrapingConfiguration = IScrapingConfiguration.getInstance();
		this.sourcingConfiguration = this.scrapingConfiguration.getSourcingConfiguration();
	}
	
	@Override
	public URLConnection getURLConnection(String urlStr) 
			throws IOException {
		final URLConnection connection;
		if(urlStr == null) {
			throw new NullPointerException();
		}
		else {
			try {
				connection = new URLConnection(new URL(urlStr), getProxy());
			}catch(MalformedURLException e0) {
				throw new IllegalArgumentException(e0);
			}
		}
		return connection;
	}

	@Override
	public URLConnection getURLConnection(String urlStr, String providerProfileName)
			throws IOException, InterruptedException {
		final URLConnection connection;
		if(urlStr == null) {
			throw new NullPointerException();
		}
		else {
			synchronized(connectionLimitSemaphoreMap) {
				try {
					final Semaphore semaphore = getSemaphore(providerProfileName);
					if(semaphore != null) {
						semaphore.acquire();
					}
					connection = new URLConnection(new URL(urlStr), getProxy(providerProfileName), semaphore);
				}catch(MalformedURLException e0) {
					throw new IllegalArgumentException(e0);
				}
			}
		}
		return connection;
	}  
 
	private Proxy getProxy() {
		final Proxy proxy;
		final IProxyConfiguration proxyConfiguration = this.scrapingConfiguration.getProxyConfiguration();
		if(proxyConfiguration.isEnabled()) {
			SocketAddress address = new InetSocketAddress(proxyConfiguration.getHostname(), proxyConfiguration.getPort());
			proxy = new Proxy(proxyConfiguration.getType(), address);
		}
		else {
			proxy = null;
		}
		return proxy; 
	}
	
	private Proxy getProxy(String profileName) {
		Proxy proxy = null;
		if(profileName == null) {
			throw new NullPointerException();
		}
		else {
			final IProviderConfiguration providerConfiguration = this.sourcingConfiguration.getProviderConfiguration(profileName);
			if(providerConfiguration != null) {
				final IProxyConfiguration proxyConfiguration = providerConfiguration.getProxyConfiguration();
				if(proxyConfiguration.isEnabled()) {
					SocketAddress address = new InetSocketAddress(proxyConfiguration.getHostname(), proxyConfiguration.getPort());
					proxy = new Proxy(proxyConfiguration.getType(), address);
				}
			}
			if(proxy == null) {
				proxy = getProxy();
			}
		}  
		return proxy;
	}
	
	private Semaphore getSemaphore(String profileName) {
		URLConnectionSemaphore sempahore = null;
		if(profileName == null) {
			throw new NullPointerException();
		}
		else {
			sempahore = connectionLimitSemaphoreMap.get(profileName);
			if(sempahore == null) {
				final IProviderConfiguration providerConf = this.scrapingConfiguration.getSourcingConfiguration().getProviderConfiguration(profileName);
				if(providerConf == null) {
					throw new IllegalArgumentException();
				}
				else {
					int connectionLimit = providerConf.getConnectionLimit();
					if(connectionLimit >= 0) {
						sempahore = new URLConnectionSemaphore(connectionLimit);
						connectionLimitSemaphoreMap.put(profileName, sempahore);
					}
				}
			}
		}
		return sempahore;
	}
}
 