/**
 *
 */
package ch.epfl.calendar.persistence;

/**
 * A Data Access Object.
 *
 * @author lweingart
 *
 */
public interface DAO {

    /**
     * Insert a new object.
     *
     * @param obj
     */
    long create(Object obj, String key);

    /**
     * Update an existing object.
     *
     * @param obj
     */
    long update(Object obj, String key);

    /**
     * Delete an existing object.
     *
     * @param obj
     */
    void delete(Object obj, String key);

    /**
     * Delete all element from table.
     */
    void deleteAll();
}
