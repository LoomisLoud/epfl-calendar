/**
 *
 */
package ch.epfl.calendar.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.Logger;

/**
 * @author lweingart
 *
 */
public class CourseDataSource implements DAO {

	private static final String ERROR_CREATE 	= "Unable to create a new course!";
	private static final String ERROR_DELETE 	= "Unable to delete a course!";
	private static final String ERROR_UPDATE 	= "Unable to update a course!";

	private static final String SUCCESS_CREATE 	= "Course successfully created!";
	private static final String SUCCESS_DELETE 	= "Course successfully deleted";
	private static final String SUCCESS_UPDATE 	= "Course successfully updated";

	private DBHelper mDBHelper;

	/**
	 * @see ch.epfl.calendar.persistence.DAO#create(java.lang.Object)
	 */
	@Override
	public void create(Object obj) throws SQLiteCalendarException {
		Course course = (Course) obj;
		assert course!= null;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CourseTable.COLUMN_NAME_NAME, course.getName());
		// FIXME: look for the reason of this error
//		values.put(CourseTable.COLUMN_NAME_PERIODS, course.getPeriods());
		values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
		values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
		values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
		values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

		long rowId = db.insert(CourseTable.TABLE_NAME_COURSE, null, values);
		if (rowId == -1) {
			Log.e(Logger.CALENDAR_SQL_ERROR, CourseDataSource.ERROR_CREATE);
			throw new SQLiteCalendarException(CourseDataSource.ERROR_CREATE);
		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, CourseDataSource.SUCCESS_CREATE);
	}

	/**
	 * @see ch.epfl.calendar.persistence.DAO#update(java.lang.Object)
	 */
	@Override
	public void update(Object obj) throws SQLiteCalendarException {
		Course course = (Course) obj;
		assert course != null;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		// FIXME: look for the reason of this error
//		values.put(CourseTable.COLUMN_NAME_PERIODS, course.getPeriods());
		values.put(CourseTable.COLUMN_NAME_TEACHER, course.getTeacher());
		values.put(CourseTable.COLUMN_NAME_CREDITS, course.getCredits());
		values.put(CourseTable.COLUMN_NAME_CODE, course.getCode());
		values.put(CourseTable.COLUMN_NAME_DESCRIPTION, course.getDescription());

		long rowId = db.update(
				CourseTable.TABLE_NAME_COURSE,
				values,
				CourseTable.COLUMN_NAME_NAME + " = ?",
				new String[] {course.getName()});
		if (rowId == -1) {
			Log.e(Logger.CALENDAR_SQL_ERROR, CourseDataSource.ERROR_UPDATE);
			throw new SQLiteCalendarException(CourseDataSource.ERROR_UPDATE);
		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, CourseDataSource.SUCCESS_UPDATE);
	}

	/**
	 * @see ch.epfl.calendar.persistence.DAO#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object obj) throws SQLiteCalendarException {
		Course course = (Course) obj;
		assert course != null;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		long rowId = db.delete(
				CourseTable.TABLE_NAME_COURSE,
				CourseTable.COLUMN_NAME_NAME + " = '" + course.getName() + "'",
				null);
		if (rowId == -1) {
			Log.e(Logger.CALENDAR_SQL_ERROR, CourseDataSource.ERROR_DELETE);
			throw new SQLiteCalendarException(CourseDataSource.ERROR_DELETE);
		}

		Log.i(Logger.CALENDAR_SQL_SUCCES, CourseDataSource.SUCCESS_DELETE);
	}

	/**
	 * @see ch.epfl.calendar.persistence.DAO#deleteAll()
	 */
	@Override
	public void deleteAll() {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.delete(CourseTable.TABLE_NAME_COURSE, null, null);
	}

}
