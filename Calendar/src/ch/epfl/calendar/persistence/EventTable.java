package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the event table.
 *
 * @author lweingart
 *
 */
public class EventTable {

    /**
     * The name of the table for {@link Event} in database
     */
    public static final String TABLE_NAME_EVENT = "event";
    
    /**
     * The name of the column containing the IDs of the events
     */
    public static final String COLUMN_NAME_ID = "_id";
    
    /**
     * The name of the column containing the names of the events
     */
    public static final String COLUMN_NAME_NAME = "name";
    
    /**
     * The name of the column containing the start dates of the events
     */
    public static final String COLUMN_NAME_STARTDATE = "startdate";
    
    /**
     * The name of the column containing the end dates of the events
     */
    public static final String COLUMN_NAME_ENDDATE = "enddate";
    
    /**
     * The name of the column containing the type of the events
     */
    public static final String COLUMN_NAME_TYPE = "type";
    
    /**
     * The name of the column containing the key of the course linked to the events
     */
    public static final String COLUMN_NAME_COURSE = "course";
    
    /**
     * The name of the column containing the description of the events
     */
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    
    /**
     * The name of the column containing the status of the isBlock of the events
     */
    public static final String COLUMN_NAME_IS_BLOCK = "isBlock";
    
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";

    /**
     * Creates an event in database.
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EventTable.TABLE_NAME_EVENT + "("
                + EventTable.COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventTable.COLUMN_NAME_NAME + " TEXT, "
                + EventTable.COLUMN_NAME_STARTDATE + " TEXT, "
                + EventTable.COLUMN_NAME_ENDDATE + " TEXT, "
                + EventTable.COLUMN_NAME_TYPE + " TEXT, "
                + EventTable.COLUMN_NAME_COURSE + " TEXT, "
                + EventTable.COLUMN_NAME_DESCRIPTION + " TEXT, "
                + EventTable.COLUMN_NAME_IS_BLOCK + " TEXT, "
                + FOREIGN_KEY + "(" + EventTable.COLUMN_NAME_COURSE + ")"
                + REFERENCES + CourseTable.TABLE_NAME_COURSE + "(" + CourseTable.COLUMN_NAME_NAME + ")"
                + ");");
    }

    /**
     * Upgrades an Event in database.
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME_EVENT);
        EventTable.onCreate(db);
    }
}
