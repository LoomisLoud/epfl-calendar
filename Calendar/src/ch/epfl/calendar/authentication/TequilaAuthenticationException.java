package ch.epfl.calendar.authentication;


/**
 * Exception when Authentication on Tequila fails
 * @author AblionGE
 *
 */
public class TequilaAuthenticationException extends Exception{

    private static final long serialVersionUID = 1L;

    public TequilaAuthenticationException(String message) {
        super(message);
    }
}
