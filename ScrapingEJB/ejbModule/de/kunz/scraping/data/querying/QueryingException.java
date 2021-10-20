package de.kunz.scraping.data.querying;


public class QueryingException extends Exception {

	private static final long serialVersionUID = -6072417374845417589L;

	public QueryingException() {
		super();
	}

	public QueryingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryingException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryingException(String message) {
		super(message);
	}

	public QueryingException(Throwable cause) {
		super(cause);
	}
}
