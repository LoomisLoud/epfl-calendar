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

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CalendarClientException() {
        super();
    }
    
    public CalendarClientException(String message) {
        super(message);
    }
    
    public CalendarClientException(Throwable throwable) {
        super(throwable);
    }
}