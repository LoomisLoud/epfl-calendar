/**
 *
 */
package ch.epfl.calendar.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author lweingart
 *
 */
public class CourseTable {

	public static final String TABLE_NAME_COURSE 		= "course";
	public static final String COLUMN_NAME_NAME 		= "name";
	public static final String COLUMN_NAME_PERIODS 		= "periods";
	public static final String COLUMN_NAME_TEACHER 		= "teacher";
	public static final String COLUMN_NAME_CREDITS 		= "credits";
	public static final String COLUMN_NAME_CODE 		= "code";
	public static final String COLUMN_NAME_DESCRIPTION 	= "description";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + CourseTable.TABLE_NAME_COURSE + "("
				+ CourseTable.COLUMN_NAME_NAME + "TEXT PRIMARY KEY, "
				+ CourseTable.COLUMN_NAME_PERIODS + "FOREIGN KEY, "
				+ CourseTable.COLUMN_NAME_TEACHER + "TEXT, "
				+ CourseTable.COLUMN_NAME_CREDITS + "INTEGER, "
				+ CourseTable.COLUMN_NAME_CODE + "TEXT, "
				+ CourseTable.COLUMN_NAME_DESCRIPTION + "TEXT)");
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME_COURSE);
		CourseTable.onCreate(db);
	}
}
