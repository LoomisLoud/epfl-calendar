/**
 *
 */
package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * Handle migration for the course table.
 *
 * @author lweingart
 *
 */
public class CourseTable {

    /**
     * The name of the table in database.
     */
    public static final String TABLE_NAME_COURSE = "course";
    
    /**
     * The name of the column containing the names of the courses in database.
     */
    public static final String COLUMN_NAME_NAME = "name";
    
    /**
     * The name of the column containing the teachers of the courses in database.
     */
    public static final String COLUMN_NAME_TEACHER = "teacher";
    
    /**
     * The name of the column containing the credits of the courses in database.
     */
    public static final String COLUMN_NAME_CREDITS = "credits";
    
    /**
     * The name of the column containing the codes of the courses in database.
     */
    public static final String COLUMN_NAME_CODE = "code";
    
    /**
     * The name of the column containing the description of the courses in database.
     */
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    /**
     * Creates the Course table in the database.
     * See {@link SQLiteDatabase#onCreate(SQLiteDatabase}
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CourseTable.TABLE_NAME_COURSE + "("
                + CourseTable.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, "
                + CourseTable.COLUMN_NAME_TEACHER + " TEXT, "
                + CourseTable.COLUMN_NAME_CREDITS + " INTEGER, "
                + CourseTable.COLUMN_NAME_CODE + " TEXT, "
                + CourseTable.COLUMN_NAME_DESCRIPTION + " TEXT)");
    }

    /**
     * Drops the table if it exists and recreates it in database.
     * See {@link SQLiteDatabase#onUpgrade(SQLiteDatabase}
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME_COURSE);
        CourseTable.onCreate(db);
    }
}
