/**
 *
 */
package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteException;

/**
 * @author lweingart
 *
 */
public class SQLiteCalendarException extends SQLiteException {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new SQLiteCalendarException.
	 *
	 * @param msg
	 */
	public SQLiteCalendarException(String msg) {
		super(msg);
	}
}
