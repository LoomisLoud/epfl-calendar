/**
 *
 */
package ch.epfl.calendar;

import ch.epfl.calendar.persistence.DBHelper;
import android.app.Application;
import android.content.Context;

/**
 * @author lweingart
 *
 */
public class App extends Application {

	/**
	 * Database file name.
	 */
	public static final String DATABASE_NAME = "calendar.db";

	/**
	 * Database version. This number must be increased whenever the database
	 * schema is upgraded in order to trigger the
	 * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
	 * method.
	 */
	public static final int DATABASE_VERSION = 1;

	/**
	 * Database helper.
	 */
	private static DBHelper mDBHelper;

	/**
	 * Application context.
	 */
	private static Context mContext;


	@Override
	public void onCreate() {
		super.onCreate();
		App.mContext = this.getApplicationContext();
		App.mDBHelper = new DBHelper(App.mContext);
	}

	public static DBHelper getDBHelper() {
		return App.mDBHelper;
	}

	public static Context getAppContext() {
		return App.mContext;
	}
}
