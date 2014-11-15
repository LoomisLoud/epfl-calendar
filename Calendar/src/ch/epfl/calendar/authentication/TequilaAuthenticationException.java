package ch.epfl.calendar.authentication;


/**
 * Exception when Authentication on Tequila fails
 * @author AblionGE
 *
 */
public class TequilaAuthenticationException extends Exception{

    private static final long serialVersionUID = 1L;

    public TequilaAuthenticationException() {
        super();
    }

    public TequilaAuthenticationException(String message) {
        super(message);
    }

    public TequilaAuthenticationException(Throwable e) {
        super(e);
    }

    public TequilaAuthenticationException(String message, Throwable e) {
    	super(message, e);
    }

}
