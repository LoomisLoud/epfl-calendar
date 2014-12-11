/**
 *
 */
package ch.epfl.calendar.apiInterface;

/**
 * Thrown to indicate a problem encountered by a {@link CalendarClientInterface} implementation
 * when fetching data.
 *
 * @author gilbrechbuhler
 *
 */
public class CalendarClientException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Simply builds a {@link CalendarClientException} with a default error message (An error 
     * occured, please try again.)
     */
    public CalendarClientException() {
        super();
    }

    /**
     * Builds a {@link CalendarClientException} and allows to set the message
     * @param message the message of the exception
     */
    public CalendarClientException(String message) {
        super(message);
    }

    /**
     * Builds a {@link CalendarClientException}
     * @param throwable a {@link Throwable} object to base this exception on.
     */
    public CalendarClientException(Throwable throwable) {
        super(throwable);
    }
}
