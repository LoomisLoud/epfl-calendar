/**
 * 
 */
package ch.epfl.calendar.display;

/**
 * An exception thrown when an event is created with reversed start and end dates
 * @author gilbrechbuhler
 */
public class ReversedDatesException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Simply builds a {@link ReversedDateException} with a default error
     * message (An error occured, please try again.)
     */
    public ReversedDatesException() {
        super();
    }
}
