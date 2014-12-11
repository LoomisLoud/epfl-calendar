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

    /**
     * The name of the table containing the periods in database.
     */
    public static final String TABLE_NAME_PERIOD = "period";
    
    /**
     * The name of the column containing the IDs of the periods in database.
     */
    public static final String COLUMN_NAME_ID = "id";
    
    /**
     * The name of the column containing the types of the periods in database.
     */
    public static final String COLUMN_NAME_TYPE = "type";
    
    /**
     * The name of the column containing the start dates of the periods in database.
     */
    public static final String COLUMN_NAME_STARTDATE = "startdate";
    
    /**
     * The name of the column containing the end dates of the periods in database.
     */
    public static final String COLUMN_NAME_ENDDATE = "enddate";
    
    /**
     * The name of the column containing the rooms of the periods in database.
     */
    public static final String COLUMN_NAME_ROOMS = "rooms";
    
    /**
     * The name of the column containing the courses related to the periods in database.
     */
    public static final String COLUMN_NAME_COURSE = "course";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";

    /**
     * Creates the table in database
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PeriodTable.TABLE_NAME_PERIOD + "("
                + PeriodTable.COLUMN_NAME_ID + " TEXT PRIMARY KEY, "
                + PeriodTable.COLUMN_NAME_TYPE + " TEXT, "
                + PeriodTable.COLUMN_NAME_STARTDATE + " TEXT, "
                + PeriodTable.COLUMN_NAME_ENDDATE + " TEXT, "
                + PeriodTable.COLUMN_NAME_ROOMS + " TEXT, "
                + PeriodTable.COLUMN_NAME_COURSE + " TEXT,"
                + FOREIGN_KEY + "(" + PeriodTable.COLUMN_NAME_COURSE + ")"
                + REFERENCES + CourseTable.TABLE_NAME_COURSE + "(" + CourseTable.COLUMN_NAME_NAME + ")"
                + ");");
    }
    /**
     * Drops the table if it exists and recreates it.
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PeriodTable.TABLE_NAME_PERIOD);
        PeriodTable.onCreate(db);
    }
}
