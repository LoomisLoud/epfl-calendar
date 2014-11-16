/**
 *
 */
package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the period table.
 *
 * @author lweingart
 *
 */
public class PeriodTable {

	public static final String TABLE_NAME_PERIOD		= "period";
	public static final String COLUMN_NAME_ID			= "_id";
	public static final String COLUMN_NAME_TYPE			= "type";
	public static final String COLUMN_NAME_STARTDATE	= "startdate";
	public static final String COLUMN_NAME_ENDDATE		= "enddate";
	public static final String COLUMN_NAME_ROOMS		= "rooms";
	public static final String COLUMN_NAME_COURSE_ID	= "course_id";

	/**
	 * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + PeriodTable.TABLE_NAME_PERIOD + "("
				+ PeriodTable.COLUMN_NAME_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PeriodTable.COLUMN_NAME_TYPE + "TEXT, "
				+ PeriodTable.COLUMN_NAME_STARTDATE + "TEXT, "
				+ PeriodTable.COLUMN_NAME_ENDDATE + "TEXT, "
				+ PeriodTable.COLUMN_NAME_ROOMS + "TEXT, "
				+ PeriodTable.COLUMN_NAME_COURSE_ID + "INTEGER FOREIGN KEY)");
	}

	/**
	 * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
	 */
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PeriodTable.TABLE_NAME_PERIOD);
		PeriodTable.onCreate(db);
	}
}
