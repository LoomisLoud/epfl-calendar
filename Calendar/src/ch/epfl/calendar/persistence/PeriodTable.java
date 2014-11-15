/**
 *
 */
package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author lweingart
 *
 */
public class PeriodTable {

	public static final String TABLE_NAME_PERIOD 		= "period";
	public static final String COLUMN_NAME_TYPE 		= "type";
	public static final String COLUMN_NAME_STARTDATE 	= "startdate";
	public static final String COLUMN_NAME_ENDDATE 		= "enddate";
	public static final String COLUMN_NAME_ROOMS 		= "rooms";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + PeriodTable.TABLE_NAME_PERIOD + "("
				+ PeriodTable.COLUMN_NAME_TYPE + "TEXT PRIMARY KEY, "
				+ PeriodTable.COLUMN_NAME_STARTDATE + "DATE, "
				+ PeriodTable.COLUMN_NAME_ENDDATE + "DATE, "
				+ PeriodTable.COLUMN_NAME_ROOMS + "TEXT)");
	}

	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + PeriodTable.TABLE_NAME_PERIOD);
		PeriodTable.onCreate(db);
	}
}
