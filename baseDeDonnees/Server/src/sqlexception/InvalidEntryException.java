package sqlexception;

public class InvalidEntryException extends Exception {	//When format is bad or there is conflict with existing entries

	public InvalidEntryException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidEntryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidEntryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidEntryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidEntryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
