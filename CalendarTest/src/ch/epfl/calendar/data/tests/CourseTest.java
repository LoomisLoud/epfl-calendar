/**
 * 
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

import junit.framework.TestCase;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;

/**
 * Test class for the {@link ch.epfl.calendar.data.Course} class
 * 
 * @author gilbrechbuhler
 * 
 */
public class CourseTest extends TestCase {

	public static final int CREDITS = 5;
	private static final Object NUMBER_OF_PERIODS = 2;
	
	private Course mCourse;
	
	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    mCourse = new Course("test", "16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());
        Period periodNormal = new Period("16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());
        mCourse.addPeriod(periodNormal);
	}

	public void testAddPeriod() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());
		Period periodNormal = new Period("16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());
		Period periodAnything = new Period("10.10.2010", "08:15", "09:15", "Anything", new ArrayList<String>());
		
		course.addPeriod(periodAnything);
		
		List<Period> returnedPeriods = course.getPeriods();

		assertEquals(returnedPeriods.size(), NUMBER_OF_PERIODS);
		
		assertEquals(returnedPeriods.get(0).getStartDate(), periodNormal.getStartDate());
		assertEquals(returnedPeriods.get(0).getEndDate(), periodNormal.getEndDate());
		assertEquals(returnedPeriods.get(0).getType(), PeriodType.EXERCISES);
		assertEquals(returnedPeriods.get(0).getRooms().size(), 0);
		
		assertEquals(returnedPeriods.get(1).getStartDate(), periodAnything.getStartDate());
		assertEquals(returnedPeriods.get(1).getEndDate(), periodAnything.getEndDate());
		assertEquals(returnedPeriods.get(1).getType(), PeriodType.DEFAULT);
		assertEquals(returnedPeriods.get(1).getRooms().size(), 0);
	}

	public void testConstructors() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15",
				"cours", new ArrayList<String>());
		Course courseOnlyName = new Course("testName");
		Course courseWithInfo = new Course("3030", "testName", "Description",
				"Hello", CREDITS);

		assertEquals(course.getName(), "test");
		assertEquals(course.getCredits(), 0);
		assertEquals(course.getTeacher(), null);

		assertEquals(courseOnlyName.getName(), "testName");
		assertEquals(courseOnlyName.getPeriods(), new ArrayList<Period>());
		assertEquals(courseOnlyName.getCredits(), 0);
		assertEquals(courseOnlyName.getTeacher(), null);

		assertEquals(courseWithInfo.getCode(), "3030");
		assertEquals(courseWithInfo.getCredits(), CREDITS);
		assertEquals(courseWithInfo.getDescription(), "Description");
		assertEquals(courseWithInfo.getName(), "testName");
		assertEquals(courseWithInfo.getTeacher(), "Hello");
	}

	public void testSetters() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15",
				"cours", new ArrayList<String>());
		Period period = new Period("16.06.2014", "16:15", "17:15", "Exercices",
				new ArrayList<String>());
		ArrayList<Period> list = new ArrayList<Period>();
		list.add(period);

		course.setCode("3030");
		assertEquals(course.getCode(), "3030");

		course.setCredits(CREDITS);
		assertEquals(course.getCredits(), CREDITS);

		course.setDescription("Des");
		assertEquals(course.getDescription(), "Des");

		course.setName("Name");
		assertEquals(course.getName(), "Name");

		course.setPeriods(list);
		assertEquals(course.getPeriods(), list);

		course.setTeacher("Man");
		assertEquals(course.getTeacher(), "Man");
	}
	
	public void testParcelable() {
	    // Obtain a Parcel object and write the parcelable object to it:
	    Parcel parcel = Parcel.obtain();
	    mCourse.writeToParcel(parcel, 0);
	    
	    // After you're done with writing, you need to reset the parcel for reading:
	    parcel.setDataPosition(0);
	    
	    // Reconstruct object from parcel and asserts:
	    Course createdFromParcel = Course.CREATOR.createFromParcel(parcel);
	    assertEquals(mCourse, createdFromParcel);
	}
}