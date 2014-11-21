/**
 *
 */
package ch.epfl.calendar.data;

import java.util.Calendar;

/**
 * @author gilbrechbuhler
 *
 */
public class Event {
    private String mName;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mType; // Maybe Enum, we have to look
    // Next is optional (like there is nothing in
    private String mLinkedCourse; // See if you want a string with the name of the course or the course itself.

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }
    /**
     * @param name the mName to set
     */
    public void setName(String name) {
        this.mName = name;
    }
    /**
     * @return the mStartDate
     */
    public Calendar getStartDate() {
        return (Calendar) mStartDate.clone();
    }
    /**
     * @param mStartDate the mStartDate to set
     */
    public void setStartDate(Calendar startDate) {
        this.mStartDate = (Calendar) startDate.clone();
    }
    /**
     * @return the mEndDate
     */
    public Calendar getEndDate() {
        return (Calendar) mEndDate.clone();
    }
    /**
     * @param mEndDate the mEndDate to set
     */
    public void setEndDate(Calendar endDate) {
        this.mEndDate = (Calendar) endDate.clone();
    }
    /**
     * @return the mType
     */
    public String getmType() {
        return mType;
    }
    /**
     * @param type the mType to set
     */
    public void setmType(String type) {
        this.mType = type;
    }
    /**
     * @return the mLinkedCourse
     */
    public String getLinkedCourse() {
        return mLinkedCourse;
    }
    /**
     * @param linkedCourse the mLinkedCourse to set
     */
    public void setLinkedCourse(String linkedCourse) {
        this.mLinkedCourse = linkedCourse;
    }
}
