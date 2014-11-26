/**
 *
 */
package ch.epfl.calendar.data;

import java.util.Calendar;

import ch.epfl.calendar.App;

/**
 * @author gilbrechbuhler
 * 
 */
public class Event {

    private int mId;
    private String mName;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mType;
    private String mLinkedCourse;
    private String mDescription;
    private boolean mIsAutomaticAddedBlock;

    /**
     * Construct the event object. date format must be of format dd.mm.yyyy
     * startTime and endTime must be of format hh:mm
     * @param date
     * @param name
     * @param startTime
     * @param endTime
     * @param type
     * @param linkedCourse
     * @param description
     * @param isBlock
     */
    public Event(String date, String name, String startTime, String endTime,
            String type, String linkedCourse, String description, boolean isBlock) {
        this.mName = name;
        this.mStartDate = App.createCalendar(date, startTime);
        this.mEndDate = App.createCalendar(date, endTime);
        if (this.mStartDate != null && this.mEndDate != null) {
            if (mStartDate.after(mEndDate)) {
                Calendar temp = mStartDate;
                mStartDate = mEndDate;
                mEndDate = temp;
            }
        }
        if (type != null) {
            setType(type);
        }
        this.mLinkedCourse = linkedCourse;
        this.mDescription = description;
        this.mIsAutomaticAddedBlock = isBlock;
    }

    /**
     * Construct the event object. startTime and endTime must be of format
     * 'dd.mm.yyy hh:mm'
     * @param name
     * @param startTime
     * @param endTime
     * @param type
     * @param linkedCourse
     * @param description
     * @param isBlock
     * @param id
     */
    public Event(String name, String startTime, String endTime, String type,
            String linkedCourse, String description, boolean isBlock, int id) {
        this(startTime.substring(App.ZERO_INDEX, App.END_DATE_INDEX), name,
                startTime.substring(App.START_TIME_INDEX), endTime
                        .substring(App.START_TIME_INDEX), type, linkedCourse,
                description, isBlock);
        this.mId = id;
    }

    @Override
    public String toString() {
        String name = this.mName;
        String startDate = App.calendarToBasicFormatString(this.mStartDate);
        String endDate = App.calendarToBasicFormatString(this.mEndDate);
        String description = this.mDescription;

        return name.concat(" from ").concat(startDate).concat(" to ").concat(endDate)
                .concat(" : ").concat(description);
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name
     *            the mName to set
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
     * @param mStartDate
     *            the mStartDate to set
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
     * @param mEndDate
     *            the mEndDate to set
     */
    public void setEndDate(Calendar endDate) {
        this.mEndDate = (Calendar) endDate.clone();
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param type
     *            the mType to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @return the mLinkedCourse
     */
    public String getLinkedCourse() {
        return mLinkedCourse;
    }

    /**
     * @param linkedCourse
     *            the mLinkedCourse to set
     */
    public void setLinkedCourse(String linkedCourse) {
        this.mLinkedCourse = linkedCourse;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String description) {
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean isAutomaticAddedBlock() {
        return mIsAutomaticAddedBlock;
    }

    public void setIsAutomaticAddedBlock(boolean isAutomaticAddedBlock) {
        this.mIsAutomaticAddedBlock = isAutomaticAddedBlock;
    }
}
