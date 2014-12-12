/**
 * 
 */
package ch.epfl.calendar.test.data;

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
	private static final String CREDITS_TO_STRING = "Remaining credits: 3.76";
	private static final String BLOCK_TO_STRING = "test\nRemaining credits: 7.0";
	private static final double DOUBLE_CREDITS = 3.756;
	private int mRemainingCredits = REMAINING_CREDITS;
	private Course mCourse = new Course("test", "16.06.2014", "16:15", "17:15",
			"Exercices", new ArrayList<String>(), "");
	private Block mBlock = new Block(mCourse, mRemainingCredits);
	
	public void testConstructor() {
	    Block testBlock = new Block(mCourse, REMAINING_CREDITS);
	    assertEquals(mCourse, testBlock.getCourse());
	    assertEquals((double) REMAINING_CREDITS, testBlock.getRemainingCredits());
	}
	
	public void testCreditsToString() {
	    mBlock.setRemainingCredits(DOUBLE_CREDITS);
	    assertEquals(CREDITS_TO_STRING, mBlock.creditsToString());
	}
	
	public void testToString() {
	    mBlock.setRemainingCredits(REMAINING_CREDITS);
	    assertEquals(BLOCK_TO_STRING, mBlock.toString());
	}

	public void testSetters() {
		assertEquals(mCourse, mBlock.getCourse());
		mCourse.setName("tempName");
		assertEquals(mCourse, mBlock.getCourse());
		Course tempCourse = new Course("test", "16.06.2014", "16:15", "17:15",
				"Lecture", new ArrayList<String>(), "");
		mBlock.setCourse(tempCourse);
		assertEquals(mBlock.getCourse(), tempCourse);

	}

	public void testDisplayable() {
		assertTrue(mBlock.displayable());
		mBlock.setRemainingCredits(0);
		assertFalse(mBlock.displayable());
	}
	
	public void testIsBlockOf() {
		assertTrue(mBlock.isBlockOf("test"));
		assertFalse(mBlock.isBlockOf("wrongTest"));
	}
}
