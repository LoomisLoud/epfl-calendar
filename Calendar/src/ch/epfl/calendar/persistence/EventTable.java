package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the event table.
 *
 * @author lweingart
 *
 */
public class EventTable {

	public static final String TABLE_NAME_EVENT			= "event";
	public static final String COLUMN_NAME_ID			= "_id";
	public static final String COLUMN_NAME_NAME			= "name";
	public static final String COLUMN_NAME_STARTDATE	= "startdate";
	public static final String COLUMN_NAME_ENDDATE		= "enddate";
	public static final String COLUMN_NAME_TYPE			= "type";
	public static final String COLUMN_NAME_COURSE		= "course";

	/**
	 * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + EventTable.TABLE_NAME_EVENT + "("
				+ EventTable.COLUMN_NAME_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ EventTable.COLUMN_NAME_NAME + "TEXT, "
				+ EventTable.COLUMN_NAME_STARTDATE + "TEXT, "
				+ EventTable.COLUMN_NAME_ENDDATE + "TEXT, "
				+ EventTable.COLUMN_NAME_TYPE + "TEXT, "
				+ EventTable.COLUMN_NAME_COURSE + "TEXT INTEGER FOREIGN KEY)");
	}

	/**
	 * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
	 */
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME_EVENT);
		PeriodTable.onCreate(db);
	}
}