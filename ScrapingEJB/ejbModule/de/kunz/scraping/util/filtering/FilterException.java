package de.kunz.scraping.util.filtering;

public class FilterException extends Exception {

	private static final long serialVersionUID = 8905848828268725533L;

	public FilterException() {
		super();
	}

	public FilterException(String message) {
		super(message);
	}

	public FilterException(Throwable cause) {
		super(cause);
	}

	public FilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
