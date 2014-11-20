/**
 * 
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import ch.epfl.calendar.data.Block;
import ch.epfl.calendar.data.Course;

/**
 * @author LoomisLoud
 *
 */
public class BlockTest extends TestCase {
	private static final int REMAINING_CREDITS = 7;
	private int mRemainingCredits = REMAINING_CREDITS;
	private Course mCourse = new Course("test", "16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());
	private Block mBlock = new Block(mCourse, mRemainingCredits);
	
	public void testSetters() {
		assertEquals(mCourse, mBlock.getCourse());
		mCourse.setName("tempName");
		assertEquals(mCourse, mBlock.getCourse());
		Course tempCourse = new Course("test", "16.06.2014", "16:15", "17:15", "Lecture", new ArrayList<String>());
		mBlock.setCourse(tempCourse);
		assertEquals(mBlock.getCourse(), tempCourse);
		
	}
	
	public void testDisplayable() {
		assertTrue(mBlock.displayable());
		mBlock.setRemainingCredits(0);
		assertFalse(mBlock.displayable());
	}
}
