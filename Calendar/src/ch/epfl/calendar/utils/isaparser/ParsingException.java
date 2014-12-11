/**
 * 
 */
package ch.epfl.calendar.utils.isaparser;

/**
 * This class represents the Exception when a Parsing fails.
 * @author AblionGE
 *
 */
public class ParsingException extends RuntimeException{
    private static final long serialVersionUID = -4295502112884769605L;
    
    /**
     * Creates a new {@link ParsingException} with a message
     * @param message the message of the {@link ParsingException}
     */
    public ParsingException(String message) {
        super(message);
    }
}
