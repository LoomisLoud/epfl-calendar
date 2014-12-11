/**
 *
 */
package ch.epfl.calendar.persistence;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ch.epfl.calendar.App;

/**
 * Class for managing the persistent storage in DB.
 * 
 * @author lweingart
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * Construct a new DBPoint entry.
     * 
     * @param context the context of the {@link Activity} actually using the {@link DBHelper}
     * @param databaseName the name of the database to use.
     */
    public DBHelper(Context context, String databaseName) {
        super(context, databaseName, null, App.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PeriodTable.onCreate(db);
        CourseTable.onCreate(db);
        EventTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PeriodTable.onUpgrade(db, oldVersion, newVersion);
        CourseTable.onUpgrade(db, oldVersion, newVersion);
        EventTable.onUpgrade(db, oldVersion, newVersion);
    }
}
