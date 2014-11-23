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

    private static final int DOUBLE_DIGIT = 10;

    private static final String TYPE_EXERCISES_FR = "Exercices";
    private static final String TYPE_EXERCISES_EN = "Exercises";
    private static final String TYPE_PROJECT_FR = "Projet";
    private static final String TYPE_PROJECT_EN = "Project";
    private static final String TYPE_LECTURE_FR = "Cours";
    private static final String TYPE_LECTURE_EN = "Lecture";

    /**
     * Construct the period object.
     * date format must be of format dd.mm.yyyy
     * startTime and endTime must be of format hh:mm
     * @param date
     * @param startTime
     * @param endTime
     * @param type
     * @param rooms
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
     * Construct the period object.
     * startDate and endDate must be of format 'dd.mm.yyyy hh:mm'
     * @param type
     * @param startDate
     * @param endDate
     * @param rooms
     */
    public Period(String type, String startDate, String endDate, List<String> rooms, String id) {
    	this(startDate.substring(App.ZERO_INDEX, App.END_DATE_INDEX),
    		 startDate.substring(App.START_TIME_INDEX),
    		 endDate.substring(App.START_TIME_INDEX),
    		 type,
    		 rooms,
    		 id);
    }

    /**
     * @return the mType
     */
    public PeriodType getType() {
        return mType;
    }

    /**
     * @param type
     *            the mType to set
     */
    public void setType(PeriodType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.mType = type;
    }

    /**
     * @param type
     * 			  the mType to set
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
     * @return the mRoom
     */
    public List<String> getRooms() {
        return new ArrayList<String>(mRooms);
    }

    /**
     * @param mRoom
     *            the mRoom to set
     */
    public void setRooms(List<String> room) {
        if (room == null) {
            throw new NullPointerException();
        }
        this.mRooms = new ArrayList<String>(room);
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        if (startDate == null) {
            throw new NullPointerException();
        }
        this.mStartDate = startDate;
    }

    public Calendar getEndDate() {
        return mEndDate;

    }

    public void setEndDate(Calendar endDate) {
        if (endDate == null) {
            throw new NullPointerException();
        }
        this.mEndDate = endDate;
    }

    public String getId() {
    	return mId;
    }

    public void setId(String id) {
    	this.mId = id;
    }

    /*
     * (non-Javadoc)
     *
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
        int startHour = mStartDate.get(Calendar.HOUR_OF_DAY);
        int startMinute = mStartDate.get(Calendar.MINUTE);
        int endHour = mEndDate.get(Calendar.HOUR_OF_DAY);
        int endMinute = mEndDate.get(Calendar.MINUTE);
        String startHourToString = Integer.toString(startHour);
        String startMinuteToString = Integer.toString(startMinute);
        String endHourToString = Integer.toString(endHour);
        String endMinuteToString = Integer.toString(endMinute);
        if (isSingleDigit(startHour)) {
            startHourToString = "0" + startHour;
        }
        if (isSingleDigit(startMinute)) {
            startMinuteToString = "0" + startMinute;
        }
        if (isSingleDigit(endHour)) {
            endHourToString = "0" + endHour;
        }
        if (isSingleDigit(endMinute)) {
            endMinuteToString = "0" + endMinute;
        }

        return periodType + " : " + day + " " + startHourToString + ":"
                + startMinuteToString + "-" + endHourToString + ":"
                + endMinuteToString;
    }

    private boolean isSingleDigit(int number) {
        return number < DOUBLE_DIGIT;
    }
    /**
     * Return if classes are equals. Either object can't be null to return true.
     * if they are the same object (==), return true.
     * if they don't have the same reference, the method test each member of the class and check if they are all equals.
     */
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
        //test each member
        if (!this.getType().equals(otherPeriod.getType())) {
            return false;
        }
        if (!this.mStartDate.equals(otherPeriod.getStartDate())) {
            return false;
        }
        if (!this.mEndDate.equals(otherPeriod.getEndDate())) {
            return false;
        }
        if (this.getRooms().size() == otherPeriod.getRooms().size()) {
            for (int i = 0; i < this.getRooms().size(); i++) {
                if (!this.getRooms().get(i).equals(otherPeriod.getRooms().get(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (!this.mId.equals(otherPeriod.getId())) {
        	return false;
		}
        return true;
    }

    /**
     * Respect the contract of equals methods
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public int hashCode() {
        int result = 0;
        result += mType.hashCode() + mStartDate.hashCode() + mEndDate.hashCode();
        for (String str : getRooms()) {
            result += str.hashCode();
        }
        return result;
    }

    // Parcelable ----------------
    // using setter and getter to check for property in case of memory error or any problem that could happen
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

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

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
}
