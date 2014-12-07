/**
 * 
 */
package ch.epfl.calendar.data;

import java.text.DecimalFormat;

/**
 * @author LoomisLoud
 *
 */
public class Block {
	
	private Course mCourse;
	private double mRemainingCredits;
	
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
	public double getRemainingCredits() {
		return mRemainingCredits;
	}
	/**
	 * @param remainingCredits the mRemainingCredits to set
	 */
	public void setRemainingCredits(double remainingCredits) {
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
	 * Returns whether this is a block of said name
	 * @param name 
	 * @return true if this is a block with the parameter's name
	 */
	public boolean isBlockOf(String name) {
		return getCourse().getName().equals(name);
	}

	@Override
	public String toString() {
		return this.mCourse.getName() + "\nRemaining credits: " + this.mRemainingCredits;
	}
	
	public String creditsToString() {
		return "Remaining credits: " + new DecimalFormat("#.##").format(this.mRemainingCredits);
	}
}
