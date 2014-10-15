package ch.epfl.calendar.data;

import java.lang.reflect.Array;

/**
 * @author Enea Bell & Marc Shär
 *
 */
public class Student extends Person implements StudentData {
	
	public static final int MIN_LENGTH_SCIPER = 5;
	public static final int MAX_LENGTH_SCIPER = 8;
	//TODO Choose type of arguments
	private String mStudentTimetable;
	private int mSciper;
	
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
}
