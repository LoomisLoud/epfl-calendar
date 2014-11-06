package ch.epfl.calendar.authentication;

/**
 * Exception when Authentication on Tequila fails
 * @author AblionGE
 *
 */
public class TequilaAuthenticationException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public TequilaAuthenticationException(String message) {
        super(message);
    }

}
