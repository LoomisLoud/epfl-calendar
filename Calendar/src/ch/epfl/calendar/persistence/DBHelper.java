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

	/**
	 * Database file name.
	 */
	public static final String DATABASE_NAME = "Calendar.db";

	/**
	 * Database version. This number must be increased whenever the database
	 * schema is upgraded in order to trigger the
	 * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
	 * method.
	 */
	public static final int DATABASE_VERSION = 1;

	private Context mContext;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
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

	public Context getContext() {
		return this.mContext;
	}
}
