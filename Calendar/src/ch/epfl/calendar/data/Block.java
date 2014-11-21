/**
 * 
 */
package ch.epfl.calendar.data;

/**
 * @author LoomisLoud
 *
 */
public class Block {
	
	private Course mCourse;
	private int mRemainingCredits;
	
	public Block(Course course, int remainingCredits) {
		this.mCourse = course;
		this.mRemainingCredits = remainingCredits;
	}
	
	/**
	 * @return the mCourse
	 */
	public Course getCourse() {
		return mCourse;
	}
	/**
	 * @param course the mCourse to set
	 */
	public void setCourse(Course course) {
		this.mCourse = course;
	}
	/**
	 * @return the mRemainingCredits
	 */
	public int getRemainingCredits() {
		return mRemainingCredits;
	}
	/**
	 * @param remainingCredits the mRemainingCredits to set
	 */
	public void setRemainingCredits(int remainingCredits) {
		this.mRemainingCredits = remainingCredits;
	}
	
	/**
	 * Returns if we can display the Block or not in a list in order to add it
	 * @return true if the number of remaining credits is different than zero
	 */
	public boolean displayable() {
		return !(mRemainingCredits == 0);
	}
	
	/**
	 *	Placing a number of credits from this block
	 *	@param credits the number of credits placed on the calendar
	 */
	public void placingCredits(int credits) {
		setRemainingCredits(this.mRemainingCredits - credits);
	}

	@Override
	public String toString() {
		return this.mCourse.getName() + "\nRemaining credits: " + this.mRemainingCredits;
	}
	
	public String creditsToString() {
		return "Remaining credits: " + this.mRemainingCredits;
	}
}
