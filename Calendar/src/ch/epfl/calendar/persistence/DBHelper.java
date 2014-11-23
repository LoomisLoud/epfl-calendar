/**
 *
 */
package ch.epfl.calendar.persistence;

import ch.epfl.calendar.App;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
     * @param context
     */
    public DBHelper(Context context) {
        super(context, App.DATABASE_NAME, null, App.DATABASE_VERSION);
        Log.i("JE RENTRE DANS", "DBHELPER");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("JDKSAJDSAKDSAJDSAKSA","JKDLSAJDLKLJAJLAS");
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
