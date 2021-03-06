/**
 *
 */
package ch.epfl.calendar.data;

import java.util.Calendar;

import ch.epfl.calendar.App;

/**
 * An event in the calendar, added by the user (different from {@link Course} and {@link Period})
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
     * @param date the date of the event
     * @param name the title of the event
     * @param startTime the stating time of the event
     * @param endTime the ending time of the event
     * @param type the type of the event
     * @param linkedCourse the {@link Course} to which the event is linked (if it is linked to a {@link Course})
     * @param description the description of the {@link Course}
     * @param isBlock true if the {@link Event} is a block of a course
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
     * @param name the title of the event
     * @param startTime the starting time of the event
     * @param endTime the ending time of the event
     * @param type the type of the event
     * @param linkedCourse the course to which the event is related
     * @param description the description of the event
     * @param isBlock true if the {@link Event} is a block (a credit work time block)
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
        String startDate = App.calendarTo12HoursString(this.mStartDate);
        String endDate = App.calendarTo12HoursString(this.mEndDate);
        String description = this.mDescription;

        return name.concat(" from ").concat(startDate).concat(" to ")
                .concat(endDate).concat(" : ").concat(description).concat("\n");
    }
    
    /**
     * Same as toString but nicely formatted to display of contents from human point of view
     * @return the content of the {@link Event} in a format to be displayed on screen.
     */
    public String toDisplay() {
        String name = this.mName;
        String[] date = App.calendarToBasicFormatStringSameDaySpecialFormat(this.mStartDate, mEndDate);
        String description = this.mDescription;
        
        return date[0] + " : " + name + "\n" + date[1] + "  " + description;
    }
    
    

    /**
     * @return the name of the event
     */
    public String getName() {
        return mName;
    }
    
    /**
     * @return the title of the event
     */
    public String getTitle() {
        String startHour = App.calendarTo12HoursString(mStartDate);
        String endHour = App.calendarTo12HoursString(mEndDate);
        
        String hour = startHour + " - " + endHour;
        
        return hour + "\n" + getName();
    }

    /**
     * Allows to set the name of the event
     * @param name the new name of the event
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @return the starting date of the event
     */
    public Calendar getStartDate() {
        return (Calendar) mStartDate.clone();
    }

    /**
     * Sets the starting date of the event
     * @param mStartDate the new start date of the event
     */
    public void setStartDate(Calendar startDate) {
        this.mStartDate = (Calendar) startDate.clone();
    }

    /**
     * @return the ending date of the event
     */
    public Calendar getEndDate() {
        return (Calendar) mEndDate.clone();
    }

    /**
     * Sets the end date of the event
     * @param mEndDate the new end date of the event
     */
    public void setEndDate(Calendar endDate) {
        this.mEndDate = (Calendar) endDate.clone();
    }

    /**
     * @return the type of the event
     */
    public String getType() {
        return mType;
    }

    /**
     * Sets the type of the event
     * @param type the new type to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @return the name of the {@link Course} to which the event is related (if there is one).
     * Empty String otherwise.
     */
    public String getLinkedCourse() {
        return mLinkedCourse;
    }

    /**
     * Set the name of the {@link Course} to which this event is related
     * @param linkedCourse the name of the new {@link Course} linked to this event.
     */
    public void setLinkedCourse(String linkedCourse) {
        this.mLinkedCourse = linkedCourse;
    }
    
    /**
     * @return the description of the event
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Set the description of the event
     * @param description the new description of the event
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    /**
     * @return the ID (in local database) of the event
     */
    public int getId() {
        return mId;
    }

    /**
     * Set the ID (related to local database) of the event
     * @param id the new ID of the event
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Method to retrieve the hours of the event in the form of a double
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
