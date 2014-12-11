/**
 * 
 */
package ch.epfl.calendar.data;

import java.text.DecimalFormat;

/**
 * A block is an event in the database which corresponds to a Course and its duration corresponds to a certain amount
 * of credits. Exemple : for a course giving 6 credits, there should be 3 Block of 2 hours in a week.
 * @author LoomisLoud
 *
 */
public class Block {
	
	private Course mCourse;
	private double mRemainingCredits;
	
	/**
	 * 
	 * @param course the {@link Course} to which this Block is related
	 * @param remainingCredits the remaining credits of the {@link Course} that you need to place on calendar.
	 */
	public Block(Course course, int remainingCredits) {
		this.mCourse = course;
		this.mRemainingCredits = remainingCredits;
	}
	
	/**
	 * @return the {@link Course} to which this Block is related
	 */
	public Course getCourse() {
		return mCourse;
	}
	/**
	 * Sets the {@link Course} to which the Block is related
	 * @param course the {@link Course} you want the Block to be related to.
	 */
	public void setCourse(Course course) {
		this.mCourse = course;
	}
	/**
	 * @return the number of remaining credits to place
	 */
	public double getRemainingCredits() {
		return mRemainingCredits;
	}
	/**
	 * Sets the number of remaining credits to place
	 * @param remainingCredits the new number of remaining credits.
	 */
	public void setRemainingCredits(double remainingCredits) {
		this.mRemainingCredits = remainingCredits;
	}
	
	/**
	 * Returns if we can display the Block or not in a list in order to add it.
	 * If this returns false, the {@link Course} related to this Block will not appear in the view to add Block.
	 * @return true if the number of remaining credits is different than zero
	 */
	public boolean displayable() {
		return !(mRemainingCredits == 0);
	}
	
	/**
	 * Returns whether this is a block of said name
	 * @param name the name you want to check
	 * @return true if this is a block with the parameter's name, false otherwise.
	 */
	public boolean isBlockOf(String name) {
		return getCourse().getName().equals(name);
	}

	@Override
	public String toString() {
		return this.mCourse.getName() + "\nRemaining credits: " + this.mRemainingCredits;
	}
	
	/**
	 * 
	 * @return the number of remaining credits to place in a {@link String}
	 */
	public String creditsToString() {
		return "Remaining credits: " + new DecimalFormat("#.##").format(this.mRemainingCredits);
	}
}
