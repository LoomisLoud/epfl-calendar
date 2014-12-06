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
    * Builds a {@link NetworkException} and allows to set the message
    * @param message the message of the exception
    */
    public NetworkException(String message) {
        super(message);
    }    
}
