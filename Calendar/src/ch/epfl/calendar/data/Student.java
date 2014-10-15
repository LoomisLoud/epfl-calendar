package ch.epfl.calendar.data;

import java.lang.reflect.Array;

/**
 * @author Enea Bell & Marc Shär
 *
 */
public class Student extends Person implements StudentData {

	//TODO Choose type of arguments
	private String mStudentTimetable;
	
	//constructor needs specific argument to fill the data of the object
	//TODO add arguments needed
	public Student() {
	}

	@Override
	public Array getLectureList() {
		// TODO Auto-generated method stub
		return null;
	}
}
