package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A course of EPFL with its informations : - Name - Date - Period classes
 *
 * @author AblionGE
 *
 */
public class Course implements Parcelable {
    private String mName;
    private List<Period> mPeriods;
    private String mTeacher;
    private int mCredits;
    private String mCode;
    private String mDescription;
    private List<Event> mEvents;

    //Constructor for courses get for ISA
    public Course(String name, String date, String startTime, String endTime,
            String type, List<String> rooms, String idPeriod) {
        this.mName = name;
        this.mPeriods = new ArrayList<Period>();
        this.addPeriod(new Period(date, startTime, endTime, type, rooms, idPeriod));
        this.mTeacher = null;
        this.mCredits = 0;
        this.mEvents = new ArrayList<Event>();
    }

    //Constructor for courses get from local DB
    public Course(String name, List<Period> periods, String teacher, int credits,
    		String code, String description, List<Event> events) {
    	this.mName = name;
    	this.mPeriods = periods;
    	this.mTeacher = teacher;
    	this.mCredits = credits;
    	this.mCode = code;
    	this.mDescription = description;
    	if (events == null) {
            this.mEvents = new ArrayList<Event>();
        } else {
            this.mEvents = events;
        }
    }

    public Course(String name) {
        this.setName(name);
        this.mPeriods = new ArrayList<Period>();
        this.setTeacher(null);
        this.setCredits(0);
        this.mEvents = new ArrayList<Event>();
    }

    /**
     * Builds a full course. Used in
     * {@link ch.epfl.calendar.data.Course#parseFromJSON(JSONObject)}
     *
     * @param code
     *            the code of the course
     * @param name
     *            the name of the course
     * @param description
     *            the description of the course
     * @param professorName
     *            the name of the Professor teaching the course
     * @param numberOfCredits
     *            the number of credits for the course
     */
    public Course(String code, String name, String description,
            String professorName, int numberOfCredits) {
        mCode = code;
        mName = name;
        mDescription = description;
        mTeacher = professorName;
        mCredits = numberOfCredits;
    }

    /**
     * Add a period to the current list of periods
     *
     * @param period
     */
    public void addPeriod(Period period) {
        this.mPeriods.add(period);
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param mName
     *            the mName to set
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @return the mPeriods
     */
    public List<Period> getPeriods() {
        return new ArrayList<Period>(mPeriods);
    }

    /**
     * @param mPeriods
     *            the mPeriods to set
     */
    public void setPeriods(List<Period> periods) {
        this.mPeriods = new ArrayList<Period>(periods);
    }

    /**
     * @return the mCredits
     */
    public int getCredits() {
        return mCredits;
    }

    /**
     * @param mCredits
     *            the mCredits to set
     */
    public void setCredits(int credits) {
        this.mCredits = credits;
    }

    /**
     * @return the mTeacher
     */
    public String getTeacher() {
        return mTeacher;
    }

    /**
     * @param mTeacher
     *            the mTeacher to set
     */
    public void setTeacher(String teacher) {
        this.mTeacher = teacher;
    }

    /**
     * @return the mCode
     */
    public String getCode() {
        return mCode;
    }

    /**
     * @param mCode
     *            the mCode to set
     */
    public void setCode(String code) {
        mCode = code;
    }

    /**
     * @return the mDescription
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @param mDescription
     *            the mDescription to set
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * @return the mEvents
     */
    public List<Event> getEvents() {
    	return new ArrayList<Event>(mEvents);
    }

    /**
     * @param mEvents
     * 			 the mEvents to set
     */
    public void setEvents(List<Event> events) {
    	this.mEvents = new ArrayList<Event>(events);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if (mEvents == null) {
    		return mName + ", Periods : " + mPeriods + ", Teacher : " + mTeacher
    				+ ", nb Credits : " + mCredits;
		} else {
    		return mName + ", Periods : " + mPeriods + ", Teacher : " + mTeacher
    				+ ", nb Credits : " + mCredits + ", Events : " + mEvents;
		}
    }

    /**
     *
     * @param jsonObject
     *            the JSONObject to parse.
     * @return A Course filled with the informations from the JSON
     * @throws JSONException
     */
    public static Course parseFromJSON(JSONObject jsonObject)
        throws JSONException {

        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        String professorName = jsonObject.getString("professorName");
        int numberOfCredits = jsonObject.getInt("numberOfCredits");

        return new Course(code, name, description, professorName,
                numberOfCredits);
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
        if (!(other instanceof Course)) {
            return false;
        }
        Course otherCourse = (Course) other;
        //test each member
        if (!this.getName().equals(otherCourse.getName())
                || !this.getPeriods().equals(otherCourse.getPeriods())
                || !this.getTeacher().equals(otherCourse.getTeacher())
                || this.getCredits() != otherCourse.getCredits()
                || !this.getCode().equals(otherCourse.getCode())
                || !this.getDescription().equals(otherCourse.getDescription())) {
            return false;
        }
        //all member are equals
        return true;
    }
    /**
     * Respect the contract of equals methods
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public int hashCode() {
        int result = 0;
        result += mName.hashCode() + mTeacher.hashCode() + mCredits + mCode.hashCode()
                 + mDescription.hashCode();
        for (Period p : mPeriods) {
            result += p.hashCode();
        }
        return result;
    }
    
    public String toDisplayPeriod() {
        //Transform year's periods to week's period by removing duplicate
        Set<String> periods = new HashSet<String>();  
        for (Period period : mPeriods) {
            periods.add(period.toDisplayInCourseDetail());
        }
        String concatPeriod = "";
        for (String period : periods) {
            concatPeriod += period;
        }
        
        return concatPeriod;
    }

    // Parcelable ----------------
    // using setter and getter to check for property in case of memory error or any problem that could happen
    // at execution between writing and reading and corrupt integrity.
    @Override
	public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeList(getPeriods());
        parcel.writeString(getTeacher());
        parcel.writeInt(getCredits());
        parcel.writeString(getCode());
        parcel.writeString(getDescription());
        parcel.writeList(getEvents());
    }

    private Course(Parcel in) {
        setName(in.readString());
        ArrayList<Period> periodList = new ArrayList<Period>();
        in.readList(periodList, Period.class.getClassLoader());
        setPeriods(periodList);
        setTeacher(in.readString());
        setCredits(in.readInt());
        setCode(in.readString());
        setDescription(in.readString());
        ArrayList<Event> eventList = new ArrayList<Event>();
        in.readList(eventList, Event.class.getClassLoader());
        setEvents(eventList);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
		public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
		public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
