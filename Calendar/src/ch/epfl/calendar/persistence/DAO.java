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
	void create(Object obj);

	/**
	 * Update an existing object.
	 *
	 * @param obj
	 */
	void update(Object obj);

	/**
	 * Delete an existing object.
	 *
	 * @param obj
	 */
	void delete(Object obj);

	/**
	 * Delete all element from table.
	 */
	void deleteAll();
}
