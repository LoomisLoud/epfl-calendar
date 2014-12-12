/**
 * 
 */
package ch.epfl.calendar.persistence;

/**
 * This interface provides to an activity to implement a callback function
 * called after creating/updating data to DB
 * @author AblionGE
 *
 */
public interface DatabaseUploadInterface {

    /**
     * The callback method to be called after creating/updating data in the database.
     */
    void callbackUploadDatabase(); 
}
