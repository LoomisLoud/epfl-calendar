/**
 * 
 */
package ch.epfl.utils.isaparser;

/**
 * This class represents the Exception when a Parsing fails.
 * @author AblionGE
 *
 */
public class ParsingException extends RuntimeException{
    private static final long serialVersionUID = -4295502112884769605L;
    
    public ParsingException(String message) {
        super(message);
    }
}
