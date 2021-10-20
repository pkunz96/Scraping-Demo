package de.kunz.scraping.api.rest;


import java.io.*;

public class SecurityClient {

	public enum Operation {
		EXECUTE_QUERY, GET_RESULT;
	}
	
	public boolean authorize(String accessToken, Operation operation) 
			throws IOException {
		return true;
	}
	
}
 