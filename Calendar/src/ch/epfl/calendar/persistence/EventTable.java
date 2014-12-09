package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the event table.
 *
 * @author lweingart
 *
 */
public class EventTable {

    public static final String TABLE_NAME_EVENT = "event";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_STARTDATE = "startdate";
    public static final String COLUMN_NAME_ENDDATE = "enddate";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_COURSE = "course";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_IS_BLOCK = "isBlock";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";

    /**
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
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME_EVENT);
        EventTable.onCreate(db);
    }
}
