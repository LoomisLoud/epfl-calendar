package ch.epfl.calendar.data;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author Enea Bell
 *
 */
public class Student extends Person implements StudentData {
	
	public static final int MIN_LENGTH_SCIPER = 5;
	public static final int MAX_LENGTH_SCIPER = 8;
	public static final String GET_TIMETABLE_WEEK = "https://isa.epfl.ch/service/secure/student/timetable/week";
	public static final String GPS_EDOC = "https://isa.epfl.ch/services/gps/EDOC";
	//TODO Choose type of arguments
	private int mSciper;
	private List<Course> mCourses;
	
	//constructor needs specific argument to fill the data of the object
	//TODO add arguments needed
	public Student() {
		
	}

	@Override
	public Array getLectureList() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSCIPER() {
		return mSciper;
	}

	public void setSCIPER(int sciper) {
		//check if argument is correct
		//get "length" of int (as number of digit)
		int length = (int) (Math.log10(sciper)+1);
		if (length < MIN_LENGTH_SCIPER || length > MAX_LENGTH_SCIPER || sciper < 0) {
			//TODO could be changed to custom exception to better discribe
			throw new IllegalArgumentException();
		}
		this.mSciper = sciper;
	}

    /**
     * @return the courses
     */
    public List<Course> getCourses() {
        return mCourses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<Course> courses) {
        this.mCourses = courses;
    }
}
