/**
 *
 */
package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import ch.epfl.calendar.App;

/**
 * A period is a date, a start time and an end time + the type (exercises,
 * lesson) and the rooms of a course
 * 
 * @author AblionGE
 * 
 */
public class Period implements Parcelable {

    private PeriodType mType;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private List<String> mRooms;
    private String mId;

    private static final String TYPE_EXERCISES_FR = "Exercices";
    private static final String TYPE_EXERCISES_EN = "Exercises";
    private static final String TYPE_PROJECT_FR = "Projet";
    private static final String TYPE_PROJECT_EN = "Project";
    private static final String TYPE_LECTURE_FR = "Cours";
    private static final String TYPE_LECTURE_EN = "Lecture";

    /**
     * Construct the period object. date format must be of format dd.mm.yyyy
     * startTime and endTime must be of format hh:mm
     * 
     * @param date the date of the period
     * @param startTime the start time  of the period
     * @param endTime the end time of the period
     * @param type the type of the period (LECTURE, EXERCISE, PROJECT)
     * @param rooms the rooms in which the period takes place
     */
    public Period(String date, String startTime, String endTime, String type,
            List<String> rooms, String id) {
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
        this.mRooms = rooms;
        this.mId = id;
    }

    /**
     * Construct the period object. startDate and endDate must be of format
     * 'dd.mm.yyyy hh:mm'
     * 
     * @param type the type of the period (LECTURE, EXERCISE, PROJECT)
     * @param startDate the start time of the period
     * @param endDate the end time of the period
     * @param rooms the rooms in which the period takes place
     */
    public Period(String type, String startDate, String endDate,
            List<String> rooms, String id) {
        this(startDate.substring(App.ZERO_INDEX, App.END_DATE_INDEX), startDate
                .substring(App.START_TIME_INDEX), endDate
                .substring(App.START_TIME_INDEX), type, rooms, id);
    }

    /**
     * @return the type of the period
     */
    public PeriodType getType() {
        return mType;
    }

    /**
     * Set the type of the period
     * @param type the type to set
     */
    public void setType(PeriodType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.mType = type;
    }

    /**
     * Set the type of the period from a String
     * @param type the type to set
     */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException();
        }
        PeriodType periodType = null;
        if (type.equalsIgnoreCase(TYPE_EXERCISES_EN)
                || type.equalsIgnoreCase(TYPE_EXERCISES_FR)) {
            periodType = PeriodType.EXERCISES;
        } else if (type.equalsIgnoreCase(TYPE_PROJECT_EN)
                || type.equalsIgnoreCase(TYPE_PROJECT_FR)) {
            periodType = PeriodType.PROJECT;
        } else if (type.equalsIgnoreCase(TYPE_LECTURE_EN)
                || type.equalsIgnoreCase(TYPE_LECTURE_FR)) {
            periodType = PeriodType.LECTURE;
        } else {
            periodType = PeriodType.DEFAULT;
        }
        this.mType = periodType;
    }

    /**
     * @return the rooms of the period
     */
    public List<String> getRooms() {
        return new ArrayList<String>(mRooms);
    }

    /**
     * Set the rooms of the period
     * @param mRoom the room to set
     */
    public void setRooms(List<String> room) {
        if (room == null) {
            throw new NullPointerException();
        }
        this.mRooms = new ArrayList<String>(room);
    }

    /**
     * 
     * @return the start date of the period
     */
    public Calendar getStartDate() {
        return mStartDate;
    }

    /**
     * Set the start date of the period
     * @param startDate the sart date to set
     */
    public void setStartDate(Calendar startDate) {
        if (startDate == null) {
            throw new NullPointerException();
        }
        this.mStartDate = startDate;
    }

    /**
     * 
     * @return the end date of the period
     */
    public Calendar getEndDate() {
        return mEndDate;
    }

    /**
     * Set the end date of the period
     * @param endDate the end date to set
     */
    public void setEndDate(Calendar endDate) {
        if (endDate == null) {
            throw new NullPointerException();
        }
        this.mEndDate = endDate;
    }

    /**
     * 
     * @return the ID of the period in local database
     */
    public String getId() {
        return mId;
    }

    /**
     * Set the ID of the period
     * @param id the ID to set
     */
    public void setId(String id) {
        this.mId = id;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String periodType;
        switch (mType) {
            case LECTURE:
                periodType = "L";
                break;
            case PROJECT:
                periodType = "P";
                break;
            case EXERCISES:
                periodType = "E";
                break;
            case DEFAULT:
                periodType = "D";
                break;
            default:
                periodType = "D";
                break;
        }
        
        String day = mStartDate.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        String startTime = App.calendarTo12HoursString(mStartDate);
        String endTime = App.calendarTo12HoursString(mEndDate);
        
        return periodType + " : " + day + " " + startTime + " - " + endTime;
    }
    
    /**
     * 
     * @return the same as toString but with the hours formated in 12-hour format (AM/PM)
     */
    public String to12HourString() {
        String periodType;
        switch (mType) {
            case LECTURE:
                periodType = "L";
                break;
            case PROJECT:
                periodType = "P";
                break;
            case EXERCISES:
                periodType = "E";
                break;
            case DEFAULT:
                periodType = "D";
                break;
            default:
                periodType = "D";
                break;
        }
        
        String day = mStartDate.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        String startTime = App.calendarTo12HoursString(mStartDate);
        String endTime = App.calendarTo12HoursString(mEndDate);
        
        return periodType + " : " + day + " " + startTime + "-" + endTime;
    }
    
    /**
     * 
     * @return the period in String format to be displayed in {@link CourseDetailsActivity}
     */
    public String toDisplayInCourseDetail() {
        String periodType;
        switch (mType) {
            case LECTURE:
                periodType = "Lecture";
                break;
            case PROJECT:
                periodType = "Project";
                break;
            case EXERCISES:
                periodType = "Exercises";
                break;
            default:
                periodType = "Default";
                break;
        }

        String day = mStartDate.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.SHORT, Locale.ENGLISH);
        
        String startHour = App.calendarTo12HoursString(mStartDate);  
        String endHour = App.calendarTo12HoursString(mEndDate);
        
        return periodType + " : " + day + " " + startHour + "-" + endHour + "\n";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Period)) {
            return false;
        }
        Period otherPeriod = (Period) other;
        // test each member
        if (!this.getType().equals(otherPeriod.getType())
                || !this.mStartDate.equals(otherPeriod.getStartDate())
                || !this.mEndDate.equals(otherPeriod.getEndDate())
                || !this.getRooms().equals(otherPeriod.getRooms())) {
            return false;
        }
        if (!this.mId.equals(otherPeriod.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += mType.hashCode() + mStartDate.hashCode()
                + mEndDate.hashCode();
        for (String str : getRooms()) {
            result += str.hashCode();
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * The {@link Parcelable.Creator} of this class
     */
    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };
    
 // Parcelable ----------------
    // using setter and getter to check for property in case of memory error or
    // any problem that could happen
    // at execution between writing and reading and corrupt integrity.
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getType());
        parcel.writeSerializable(getStartDate());
        parcel.writeSerializable(getEndDate());
        parcel.writeList(getRooms());
        parcel.writeSerializable(getId());
    }
    
    private Period(Parcel in) {
        this.setType((PeriodType) in.readSerializable());
        this.setStartDate((Calendar) in.readSerializable());
        this.setEndDate((Calendar) in.readSerializable());
        ArrayList<String> rooms = new ArrayList<String>();
        in.readList(rooms, String.class.getClassLoader());
        this.setRooms(rooms);
        this.setId((String) in.readSerializable());
    }
}
