package ch.epfl.calendar.apiInterface;

/**
 * This interface is used to update data in an activity after
 * storing informations online
 * A class storing or updating the database must implement this interface.
 * @author AblionGE
 *
 */
public interface UpdateDataFromDBInterface {
    
    /**
     * Method to call when all the storage or update of local database has finished.
     */
    void updateFromDatabase();
}
