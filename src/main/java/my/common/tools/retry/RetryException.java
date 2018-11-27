package my.common.tools.retry;

/**
 * 
 * @author mariogiolo
 *
 */
public class RetryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Failed to recover from errors.";
	
	public RetryException() {
		super(MESSAGE);
	}
	
	public RetryException(final String message, final Throwable cause) {
        super(message, cause, true, true);
    }

    public RetryException(final Throwable cause) {
    	super(MESSAGE, cause, true, true);
    }
    
    public static final RetryException with(final Throwable cause) {
    	return new RetryException(cause);
    }
}
