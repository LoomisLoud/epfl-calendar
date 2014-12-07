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
    private static final int MINUTE_CONVERTER = 60;

    /**
     * Construct the event object. date format must be of format dd.mm.yyyy
     * startTime and endTime must be of format hh:mm
     * 
     * @param date
     * @param name
     * @param startTime
     * @param endTime
     * @param type
     * @param linkedCourse
     * @param description
     * @param isBlock
     */
    public Event(String startDate, String endDate, String name,
            String startTime, String endTime, String type, String linkedCourse,
            String description, boolean isBlock) {
        this.mName = name;
        this.mStartDate = App.createCalendar(startDate, startTime);
        this.mEndDate = App.createCalendar(endDate, endTime);
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
     * 
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
        this(startTime.substring(App.ZERO_INDEX, App.END_DATE_INDEX), endTime
                .substring(App.ZERO_INDEX, App.END_DATE_INDEX), name, startTime
                .substring(App.START_TIME_INDEX), endTime
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

        return name.concat(" from ").concat(startDate).concat(" to ")
                .concat(endDate).concat(" : ").concat(description).concat("\n");
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }
    
    public String getTitle() {
        String startHour = App.calendarHourToBasicFormatString(mStartDate);
        String endHour = App.calendarHourToBasicFormatString(mEndDate);
        
        String hour = startHour + " - " + endHour;
        
        return hour + "\n" + getName();
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Method to retrieve the hours of the event in form of a double
     * 
     * @return hours of the event in double
     */
    public double getHours() {
        double hourStart = this.mStartDate.get(Calendar.HOUR_OF_DAY);
        double hourEnd = this.mEndDate.get(Calendar.HOUR_OF_DAY);
        double minuteStart = ((double) this.mStartDate.get(Calendar.MINUTE))
                / MINUTE_CONVERTER;
        double minuteEnd = ((double) this.mEndDate.get(Calendar.MINUTE))
                / MINUTE_CONVERTER;

        return (hourEnd + minuteEnd) - (hourStart + minuteStart);
    }

    public boolean isAutomaticAddedBlock() {
        return mIsAutomaticAddedBlock;
    }

    public void setIsAutomaticAddedBlock(boolean isAutomaticAddedBlock) {
        this.mIsAutomaticAddedBlock = isAutomaticAddedBlock;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int constantValue1 = 1231;
        final int constantValue2 = 1237;
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mDescription == null) ? 0 : mDescription.hashCode());
        result = prime * result
                + ((mEndDate == null) ? 0 : mEndDate.hashCode());
        result = prime * result + mId;
        result = prime * result
                + (mIsAutomaticAddedBlock ? constantValue1 : constantValue2);
        result = prime * result
                + ((mLinkedCourse == null) ? 0 : mLinkedCourse.hashCode());
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        result = prime * result
                + ((mStartDate == null) ? 0 : mStartDate.hashCode());
        result = prime * result + ((mType == null) ? 0 : mType.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        if (mDescription == null) {
            if (other.mDescription != null) {
                return false;
            }
        } else if (!mDescription.equals(other.mDescription)) {
            return false;
        }
        if (mEndDate == null) {
            if (other.mEndDate != null) {
                return false;
            }
        } else if (!mEndDate.equals(other.mEndDate)) {
            return false;
        }
        if (mId != other.mId) {
            return false;
        }
        if (mIsAutomaticAddedBlock != other.mIsAutomaticAddedBlock) {
            return false;
        }
        if (mLinkedCourse == null) {
            if (other.mLinkedCourse != null) {
                return false;
            }
        } else if (!mLinkedCourse.equals(other.mLinkedCourse)) {
            return false;
        }
        if (mName == null) {
            if (other.mName != null) {
                return false;
            }
        } else if (!mName.equals(other.mName)) {
            return false;
        }
        if (mStartDate == null) {
            if (other.mStartDate != null) {
                return false;
            }
        } else if (!mStartDate.equals(other.mStartDate)) {
            return false;
        }
        if (mType == null) {
            if (other.mType != null) {
                return false;
            }
        } else if (!mType.equals(other.mType)) {
            return false;
        }
        return true;
    }
}
