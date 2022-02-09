package sqlexception;

public class NotAllowedException extends Exception { //raised when the token given by the user isn't the same as the one in the sql database

	public NotAllowedException() {
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
