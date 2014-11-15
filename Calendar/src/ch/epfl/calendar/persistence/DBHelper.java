/**
 *
 */
package ch.epfl.calendar.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author lweingart
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Calendar.db";
	public static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		PeriodTable.onCreate(db);
		CourseTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		PeriodTable.onUpgrade(db, oldVersion, newVersion);
		CourseTable.onUpgrade(db, oldVersion, newVersion);
	}

}
