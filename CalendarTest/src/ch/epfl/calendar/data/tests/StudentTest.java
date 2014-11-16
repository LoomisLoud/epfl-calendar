/**
 * 
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Student;

/**
 * @author LoomisLoud
 *
 */
public class StudentTest extends TestCase {
	public static final int HIGH_SCIPER = 999999999;
	public static final int SCIPER = 203010;

	public void testSettersAndGetters() {
		Student student = new Student();
		ArrayList<Course> testArray = null;
		assertEquals(student.getLectureList(), null);
		
		try {
			student.setSCIPER((Integer) null);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			//success
		}
		
		try {
			student.setSCIPER(HIGH_SCIPER);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}
		
		try {
			student.setSCIPER(-1);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}
		
		student.setSCIPER(SCIPER);
		assertEquals(student.getSCIPER(), SCIPER);
		
		try {
			student.setCourses(testArray);
			fail("Should have thrown IllegalArgumentException");
		} catch (NullPointerException e) {
			//success
		}
		
		testArray = new ArrayList<Course>();
		testArray.add(new Course("TempCourse"));
		student.setCourses(testArray);
		assertEquals(student.getCourses(), testArray);
	}
}
