/**
 *
 */
package ch.epfl.calendar.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.utils.Logger;

/**
 * DAO for {@link Course}.
 *
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

	private static CourseDataSource mCourseDataSource;
	private DBHelper mDBHelper;

	public static CourseDataSource getInstance() {
		if (CourseDataSource.mCourseDataSource == null) {
			CourseDataSource.mCourseDataSource = new CourseDataSource();
		}
		return CourseDataSource.mCourseDataSource;
	}

	public ArrayList<Course> findAll() {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM "
				+ CourseTable.TABLE_NAME_COURSE
				+ " ORDER BY code ASC",
				null);
		ArrayList<Course> courses = new ArrayList<Course>();

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				String name = cursor.getString(
						cursor.getColumnIndex(CourseTable.COLUMN_NAME_NAME));
				// FIXME: find a valid return type for a list of periods
				ArrayList<Period> periods = null;
				String teacher = cursor.getString(
						cursor.getColumnIndex(CourseTable.COLUMN_NAME_TEACHER));
				int credits = cursor.getInt(
						cursor.getColumnIndex(CourseTable.COLUMN_NAME_CREDITS));
				String code = cursor.getString(
						cursor.getColumnIndex(CourseTable.COLUMN_NAME_CODE));
				String description = cursor.getString(
						cursor.getColumnIndex(CourseTable.COLUMN_NAME_DESCRIPTION));

				courses.add(new Course(name, periods, teacher, credits, code, description));
			}
		}

		return courses;
	}

	/**
	 * Create a course.
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
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
	 * Update a course.
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
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
	 * Delete a course,
	 *
	 * @param obj
	 * @throws SQLiteCalendarException
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
	 * Delete all courses.
	 */
	@Override
	public void deleteAll() {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.delete(CourseTable.TABLE_NAME_COURSE, null, null);
	}

}
