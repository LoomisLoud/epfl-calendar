/**
 *
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;

/**
 * Test class for the {@link ch.epfl.calendar.data.Course} class
 * 
 * @author gilbrechbuhler
 * 
 */
public class CourseTest extends TestCase {

	private static final int FIVE_CREDITS = 5;
	private static final Object NUMBER_OF_PERIODS = 2;
	private static final int FOUR_CREDITS = 4;
	private static final List<Event> EVENT_LIST = new ArrayList<Event>();
	private static final List<Period> PERIODS_LIST = new ArrayList<Period>();
	private Course mCourse;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mCourse = new Course("test", "16.06.2014", "16:15", "17:15",
				"Exercices", new ArrayList<String>(), "idPeriod");
		mCourse.setTeacher("Pr Waffle Blue");
		mCourse.setCredits(FIVE_CREDITS);
		mCourse.setCode("CS-42");
		mCourse.setDescription("Waffle's knowledge  of the world");
		ArrayList<String> rooms = new ArrayList<String>();
		rooms.add("CO2");
		Period periodNormal = new Period("16.06.2014", "16:15", "17:15",
				"Exercices", rooms, "idPeriod");
		Period periodAnything = new Period("10.10.2010", "08:15", "09:15",
				"Anything", new ArrayList<String>(), "idPeriod");
		mCourse.addPeriod(periodNormal);
		PERIODS_LIST.add(periodAnything);
		PERIODS_LIST.add(periodNormal);
		Event eventNormal = new Event("27.11.2014", "27.11.2014", "Sweng project", "08:00", "10:00", 
	            "Project", "Sweng", "description", true);
		Event eventSecond = new Event("Sweng project", "27.11.2014 08:00", "27.11.2014 10:00", 
	            "Project", "Sweng", "description", true, 1);
		EVENT_LIST.add(eventNormal);
		EVENT_LIST.add(eventSecond);
	}

	public void testAddPeriod() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15",
				"Exercices", new ArrayList<String>(), "idPeriod");
		Period periodNormal = new Period("16.06.2014", "16:15", "17:15",
				"Exercices", new ArrayList<String>(), "idPeriod");
		Period periodAnything = new Period("10.10.2010", "08:15", "09:15",
				"Anything", new ArrayList<String>(), "idPeriod");

		course.addPeriod(periodAnything);

		List<Period> returnedPeriods = course.getPeriods();

		assertEquals(returnedPeriods.size(), NUMBER_OF_PERIODS);

		assertEquals(returnedPeriods.get(0).getStartDate(),
				periodNormal.getStartDate());
		assertEquals(returnedPeriods.get(0).getEndDate(),
				periodNormal.getEndDate());
		assertEquals(returnedPeriods.get(0).getType(), PeriodType.EXERCISES);
		assertEquals(returnedPeriods.get(0).getRooms().size(), 0);

		assertEquals(returnedPeriods.get(1).getStartDate(),
				periodAnything.getStartDate());
		assertEquals(returnedPeriods.get(1).getEndDate(),
				periodAnything.getEndDate());
		assertEquals(returnedPeriods.get(1).getType(), PeriodType.DEFAULT);
		assertEquals(returnedPeriods.get(1).getRooms().size(), 0);
	}

	public void testConstructors() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15",
				"cours", new ArrayList<String>(), "idPeriod");
		Course courseOnlyName = new Course("testName");
		Course courseWithInfo = new Course("3030", "testName", "Description",
				"Hello", FIVE_CREDITS);

		Course courseWithLocalDB = new Course("test", PERIODS_LIST, "teacher",
				FIVE_CREDITS, "3030", "description", EVENT_LIST);

		assertEquals(course.getName(), "test");
		assertEquals(course.getCredits(), 0);
		assertEquals(course.getTeacher(), null);

		assertEquals(courseOnlyName.getName(), "testName");
		assertEquals(courseOnlyName.getPeriods(), new ArrayList<Period>());
		assertEquals(courseOnlyName.getCredits(), 0);
		assertEquals(courseOnlyName.getTeacher(), null);

		assertEquals(courseWithInfo.getCode(), "3030");
		assertEquals(courseWithInfo.getCredits(), FIVE_CREDITS);
		assertEquals(courseWithInfo.getDescription(), "Description");
		assertEquals(courseWithInfo.getName(), "testName");
		assertEquals(courseWithInfo.getTeacher(), "Hello");
		
		assertEquals(courseWithLocalDB.getName(), "test");
		assertEquals(courseWithLocalDB.getPeriods(), PERIODS_LIST);
		assertEquals(courseWithLocalDB.getTeacher(), "teacher");
		assertEquals(courseWithLocalDB.getCredits(), FIVE_CREDITS);
		assertEquals(courseWithLocalDB.getCode(), "3030");
		assertEquals(courseWithLocalDB.getDescription(), "description");
		assertEquals(courseWithLocalDB.getEvents(), EVENT_LIST);
	}

	public void testSetters() {
		Course course = new Course("test", "16.06.2014", "16:15", "17:15",
				"cours", new ArrayList<String>(), "idPeriod");
		Period period = new Period("16.06.2014", "16:15", "17:15", "Exercices",
				new ArrayList<String>(), "idPeriod");
		ArrayList<Period> list = new ArrayList<Period>();
		list.add(period);

		course.setCode("3030");
		assertEquals(course.getCode(), "3030");

		course.setCredits(FIVE_CREDITS);
		assertEquals(course.getCredits(), FIVE_CREDITS);

		course.setDescription("Des");
		assertEquals(course.getDescription(), "Des");

		course.setName("Name");
		assertEquals(course.getName(), "Name");

		course.setPeriods(list);
		assertEquals(course.getPeriods(), list);

		course.setTeacher("Man");
		assertEquals(course.getTeacher(), "Man");
	}

	public void testParseFromJSON() {
		Course course = new Course("temp");
		JSONObject json = new JSONObject();

		try {
			json = new JSONObject(
					"{\"code\":\"BIO-341\",\"professorName\":\"Naef\","
							+ "\"name\":\"Mod\u00e9lisation math\u00e9matique et computationnelle en biologie\","
							+ "\"numberOfCredits\":4,\"description\":\"This course introduces"
							+ "dynamical systems theory for "
							+ "modeling simple biological networks. Qualitative analysis"
							+ "of non-linear dynamical models will "
							+ "be developed in conjunction with simulations. The focus is on applications to "
							+ "biological networks.\"}");
		} catch (JSONException e) {
			// doNothing
		}

		// Exception tests
		try {
			course = Course
					.parseFromJSON(new JSONObject(
							"{\"code\":,\"professorName\":\"Naef\","
									+ "\"name\":\"Mod\u00e9lisation math\u00e9matique et"
									+ "computationnelle en biologie\","
									+ "\"numberOfCredits\":4,\"description\":\"This"
									+ "course introduces dynamical systems theory for "
									+ "modeling simple biological networks. Qualitative"
									+ "analysis of non-linear dynamical models will "
									+ "be developed in conjunction with simulations. The focus is on applications to "
									+ "biological networks.\"}"));
			fail("Should have thrown JSONException");
		} catch (JSONException e) {
			// success
		}

		try {
			course = Course
					.parseFromJSON(new JSONObject(
							"{\"code\":\"BIO-341\",\"professorName\":,"
									+ "\"name\":\"Mod\u00e9lisation math\u00e9matique et"
									+ "computationnelle en biologie\","
									+ "\"numberOfCredits\":4,\"description\":\"This"
									+ "course introduces dynamical systems theory "
									+ "for modeling simple biological networks."
									+ "Qualitative analysis of non-linear dynamical models "
									+ "will be developed in conjunction with simulations."
									+ "The focus is on applications to "
									+ "biological networks.\"}"));
			fail("Should have thrown JSONException");
		} catch (JSONException e) {
			// success
		}

		try {
			course = Course
					.parseFromJSON(new JSONObject(
							"{\"code\":\"BIO-341\",\"professorName\":\"Naef\","
									+ "\"name\":,\"numberOfCredits\":4,\"description\":\"This"
									+ "course introduces dynamical systems "
									+ "theory for modeling simple biological networks."
									+ "Qualitative analysis of non-linear dynamical "
									+ "models will be developed in conjunction with simulations."
									+ "The focus is on applications to "
									+ "biological networks.\"}"));
			fail("Should have thrown JSONException");
		} catch (JSONException e) {
			// success
		}

		try {
			course = Course
					.parseFromJSON(new JSONObject(
							"{\"code\":\"BIO-341\",\"professorName\":\"Naef\","
									+ "\"name\":\"Mod\u00e9lisation math\u00e9matique et"
									+ "computationnelle en biologie\","
									+ "\"numberOfCredits\":,\"description\":\"This course"
									+ "introduces dynamical systems theory for "
									+ "modeling simple biological networks."
									+ "Qualitative analysis of non-linear dynamical models will "
									+ "be developed in conjunction with simulations. The focus is on applications to "
									+ "biological networks.\"}"));
			fail("Should have thrown JSONException");
		} catch (JSONException e) {
			// success
		}

		try {
			course = Course
					.parseFromJSON(new JSONObject(
							"{\"code\":\"BIO-341\",\"professorName\":\"Naef\","
									+ "\"name\":\"Mod\u00e9lisation math\u00e9matique et"
									+ "computationnelle en biologie\","
									+ "\"numberOfCredits\":4,\"description\":}"));
			fail("Should have thrown JSONException");
		} catch (JSONException e) {
			// success
		}

		// Testing arguments
		try {
			course = Course.parseFromJSON(json);
		} catch (JSONException e) {
			fail("Should not have thrown an exception.");
		}

		assertEquals(course.getCode(), "BIO-341");
		assertEquals(course.getCredits(), FOUR_CREDITS);
		assertEquals(
				course.getDescription(),
				"This course introducesdynamical systems theory for modeling simple "
						+ "biological networks. Qualitative analysisof non-linear "
						+ "dynamical models will be developed in "
						+ "conjunction with simulations. The focus is on applications to biological networks.");
		assertEquals(course.getName(),
				"Mod\u00e9lisation math\u00e9matique et computationnelle en biologie");
		assertEquals(course.getTeacher(), "Naef");

	}

	public void testEqualsSameObject() {
		assertEquals(mCourse, mCourse);
	}

	public void testEqualsDifferentReference() {
		Course course2 = new Course("test", "16.06.2014", "16:15", "17:15",
				"Exercices", new ArrayList<String>(), "idPeriod");
		course2.setTeacher("Pr Waffle Blue");
		course2.setCredits(FIVE_CREDITS);
		course2.setCode("CS-42");
		course2.setDescription("Waffle's knowledge  of the world");
		ArrayList<String> rooms = new ArrayList<String>();
		rooms.add("CO2");
		Period periodNormal = new Period("16.06.2014", "16:15", "17:15",
				"Exercices", rooms, "idPeriod");
		course2.addPeriod(periodNormal);
		assertEquals(mCourse, course2);
	}

	public void testHashSameObject() {
		assertEquals(mCourse.hashCode(), mCourse.hashCode());
	}

	public void testHashDifferentReference() {
		Course course2 = new Course("test", "16.06.2014", "16:15", "17:15",
				"Exercices", new ArrayList<String>(), "idPeriod");
		course2.setTeacher("Pr Waffle Blue");
		course2.setCredits(FIVE_CREDITS);
		course2.setCode("CS-42");
		course2.setDescription("Waffle's knowledge  of the world");
		ArrayList<String> rooms = new ArrayList<String>();
		rooms.add("CO2");
		Period periodNormal = new Period("16.06.2014", "16:15", "17:15",
				"Exercices", rooms, "idPeriod");
		course2.addPeriod(periodNormal);
		assertEquals(mCourse.hashCode(), course2.hashCode());
	}

	public void testParcelable() {
		// Obtain a Parcel object and write the parcelable object to it:
		Parcel parcel = Parcel.obtain();
		mCourse.writeToParcel(parcel, 0);

		// After you're done with writing, you need to reset the parcel for
		// reading:
		parcel.setDataPosition(0);

		// Reconstruct object from parcel and asserts:
		Course createdFromParcel = Course.CREATOR.createFromParcel(parcel);

		parcel.recycle();
		assertEquals(mCourse, createdFromParcel);
	}
	
	public void testDescribeContents() {
		assertEquals(mCourse.describeContents(), 0);
	}
}
