/**
 * 
 */
package ch.epfl.calendar.display;

/**
 * @author gilbrechbuhler
 * 
 */
public class ReversedDatesException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Simply builds a {@link ReversedDateException} with a default error
     * message (An error occured, please try again.)
     */
    public ReversedDatesException() {
        super();
    }

    /**
     * Builds a {@link ReversedDateException} and allows to set the message
     * 
     * @param message
     *            the message of the exception
     */
    public ReversedDatesException(String message) {
        super(message);
    }

    /**
     * Builds a {@link ReversedDateException}
     * 
     * @param throwable
     *            a {@link Throwable} object to base this exception on.
     */
    public ReversedDatesException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Combinations of the 2 previous constructors.
     * 
     * @param message
     *            the message of the exception
     * @param throwable
     *            a {@link Throwable} object to base this exception on.
     */
    public ReversedDatesException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
