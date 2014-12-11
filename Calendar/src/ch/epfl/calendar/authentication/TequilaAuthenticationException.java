package ch.epfl.calendar.authentication;


/**
 * Exception when Authentication on Tequila fails
 * @author AblionGE
 *
 */
public class TequilaAuthenticationException extends Exception{

    private static final long serialVersionUID = 1L;

    /**
     * Builds a {@link TequilaAuthenticationException} and allows to set the message
     * @param message the message of the exception
     */
    public TequilaAuthenticationException(String message) {
        super(message);
    }
}
