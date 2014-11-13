package ch.epfl.calendar.utils;

/**
* Thrown to indicate a problem encountered by the connection to the network
*
* @author AblionGE
*
*/
public class NetworkException extends Exception {

    private static final long serialVersionUID = 1L;

   /**
    * Simply builds a {@link CalendarClientException} with a default error message (An error 
    * occured, please try again.)
    */
    public NetworkException() {
        super();
    }

       /**
    * Builds a {@link CalendarClientException} and allows to set the message
    * @param message the message of the exception
    */
    public NetworkException(String message) {
        super(message);
    }

       /**
    * Builds a {@link CalendarClientException}
    * @param throwable a {@link Throwable} object to base this exception on.
    */
    public NetworkException(Throwable throwable) {
        super(throwable);
    }

    /**
    * Combinations of the 2 previous constructors.
    * @param message the message of the exception
    * @param throwable a {@link Throwable} object to base this exception on.
    */
    public NetworkException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
