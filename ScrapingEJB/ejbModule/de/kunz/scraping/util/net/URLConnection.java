package de.kunz.scraping.util.net;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

public class URLConnection extends java.net.URLConnection implements AutoCloseable {

	private java.net.URLConnection connection;

	private final Semaphore semaphore; 
	
	private boolean initialized;
	
	URLConnection(URL url, Proxy proxy) 
			throws IOException {
		super(url);
		if(url == null) {
			throw new NullPointerException();
		}
		else {
			try {
				this.connection = url.openConnection(proxy);
				this.semaphore = null;
				this.initialized = true;
			} catch(IOException e0) {
				this.initialized = false;
				this.connection = null;
				throw e0;
			}
		}
	}
	
	URLConnection(URL url, Proxy proxy, Semaphore semaphore) 
			throws IOException {
		super(url);
		if(url == null) {
			throw new NullPointerException();
		}
		else {
			try {
				this.connection = url.openConnection(proxy);
				this.semaphore = semaphore;
				this.initialized = true;
			}catch(IOException e0) {
				this.initialized = false;
				this.connection = null;
				if(semaphore != null) {
					semaphore.release();
				}
				throw e0;
			}
		}
	}
	
	@Override
	public int hashCode() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.equals(obj);
	}

	@Override
	public void connect() throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.connect();
	}

	@Override
	public void setConnectTimeout(int timeout) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setConnectTimeout(timeout);
	}

	@Override
	public int getConnectTimeout() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getConnectTimeout();
	}

	@Override
	public void setReadTimeout(int timeout) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setReadTimeout(timeout);
	}
	
	@Override
	public int getReadTimeout() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getReadTimeout();
	}

	@Override
	public URL getURL() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getURL();
	}

	@Override
	public int getContentLength() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContentLength();
	}
	
	@Override
	public long getContentLengthLong() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContentType();
	}

	@Override
	public String getContentEncoding() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContentEncoding();
	}

	@Override
	public long getExpiration() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getExpiration();
	}

	@Override
	public long getDate() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getDate();
	}

	@Override
	public long getLastModified() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getLastModified();
	}

	@Override
	public String getHeaderField(String name) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderField(name);
	}

	@Override
	public Map<String, List<String>> getHeaderFields() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderFields();
	}

	@Override
	public int getHeaderFieldInt(String name, int Default) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderFieldInt(name, Default);
	}

	@Override
	public long getHeaderFieldLong(String name, long Default) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderFieldLong(name, Default);
	}

	@Override
	public long getHeaderFieldDate(String name, long Default) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderFieldDate(name, Default);
	}

	@Override
	public String getHeaderFieldKey(int n) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderFieldKey(n);
	}

	@Override
	public String getHeaderField(int n) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getHeaderField(n);
	}

	@Override
	public Object getContent() throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContent();
	}

	@Override
	public Object getContent(Class<?>[] classes) throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getContent(classes);
	}

	@Override
	public Permission getPermission() throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getPermission();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getOutputStream();
	}

	@Override
	public String toString() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.toString();
	}

	@Override
	public void setDoInput(boolean doinput) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setDoInput(doinput);
	}

	@Override
	public boolean getDoInput() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getDoInput();
	}

	@Override
	public void setDoOutput(boolean dooutput) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setDoOutput(dooutput);
	}

	@Override
	public boolean getDoOutput() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getDoOutput();
	}

	@Override
	public void setAllowUserInteraction(boolean allowuserinteraction) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setAllowUserInteraction(allowuserinteraction);
	}

	@Override
	public boolean getAllowUserInteraction() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getAllowUserInteraction();
	}

	@Override
	public void setUseCaches(boolean usecaches) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setUseCaches(usecaches);
	}

	@Override
	public boolean getUseCaches() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getUseCaches();
	}

	@Override
	public void setIfModifiedSince(long ifmodifiedsince) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setIfModifiedSince(ifmodifiedsince);
	}

	@Override
	public long getIfModifiedSince() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getIfModifiedSince();
	}

	@Override
	public boolean getDefaultUseCaches() {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getDefaultUseCaches();
	}

	@Override
	public void setDefaultUseCaches(boolean defaultusecaches) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setDefaultUseCaches(defaultusecaches);
	}

	@Override
	public void setRequestProperty(String key, String value) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.setRequestProperty(key, value);
	}

	@Override
	public void addRequestProperty(String key, String value) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		connection.addRequestProperty(key, value);
	}

	@Override
	public String getRequestProperty(String key) {
		if(!initialized) {
			throw new IllegalStateException();
		}
		return connection.getRequestProperty(key);
	}

	@Override
	public Map<String, List<String>> getRequestProperties() {
		if(!initialized) {
			throw new IllegalStateException();
		} 
		return connection.getRequestProperties();
	}

	@Override
	public synchronized void close() {
		if(!this.initialized) {
			throw new IllegalStateException();
		}
		else {
	
			//final InputStream in = this.getInputStream();
			//final OutputStream out = this.getOutputStream();
			//if(in != null) {
			//	in.close();
			//} 
			//if(out != null) {
			//	out.close();
			//}
			if(this.semaphore != null) {
				this.semaphore.release();
			}
			this.initialized = false;
			this.connection = null;
		}
	}
}
