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

    /**
     * Constructor used for courses get from ISA
     *
     * @param name
     *            the name of the course
     * @param date
     *            the date of a new period of the course
     * @param startTime
     *            the starting time of the new period of the course
     * @param endTime
     *            the ending time of the new period of the course
     * @param type
     *            the type (LECTURE, EXERCISE, ...)
     * @param rooms
     *            the rooms in which the period takes place
     * @param idPeriod
     *            the id of the period in the ISA database
     */
    public Course(String name, String date, String startTime, String endTime,
            String type, List<String> rooms, String idPeriod) {
        this.mName = name;
        this.mPeriods = new ArrayList<Period>();
        this.addPeriod(new Period(date, startTime, endTime, type, rooms,
                idPeriod));
        this.mTeacher = null;
        this.mCredits = 0;
        this.mEvents = new ArrayList<Event>();
    }

    /**
     * Constructor used for courses get from the local DB
     *
     * @param name
     *            the name of the course
     * @param periods
     *            the list of the periods of the course
     * @param teacher
     *            the name of the teacher of this course
     * @param credits
     *            the number of credits of the course
     * @param code
     *            the code of the course (CS-XXX, GC-XXX, ...)
     * @param description
     *            the description of the course
     * @param events
     *            the list of the events related to the course
     */
    public Course(String name, List<Period> periods, String teacher,
            int credits, String code, String description, List<Event> events) {
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

    /**
     * Constructs a course with the given name, no period and events (empty
     * lists), no teacher (null) and no credits (0).
     *
     * @param name
     *            the name of the course
     */
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
     *            the period to add to the list of {@link Period}
     */
    public void addPeriod(Period period) {
        this.mPeriods.add(period);
    }

    /**
     * @return the name of the course
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the name of the course
     *
     * @param mName
     *            the new name of the course
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @return the {@link List} of {@link Period} of this {@link Course}
     */
    public List<Period> getPeriods() {
        return new ArrayList<Period>(mPeriods);
    }

    /**
     * Sets the {@link List} of {@link Period} of this {@link Course}
     *
     * @param mPeriods
     *            the new {@link List} of {@link Period}
     */
    public void setPeriods(List<Period> periods) {
        this.mPeriods = new ArrayList<Period>(periods);
    }

    /**
     * @return the number of credits of this course
     */
    public int getCredits() {
        return mCredits;
    }

    /**
     * Sets the number of credits of a course
     *
     * @param mCredits
     *            the new number of credits
     */
    public void setCredits(int credits) {
        this.mCredits = credits;
    }

    /**
     * @return the teacher of the course
     */
    public String getTeacher() {
        return mTeacher;
    }

    /**
     * Sets the teacher name of the course
     *
     * @param mTeacher
     *            the new teacher name
     */
    public void setTeacher(String teacher) {
        this.mTeacher = teacher;
    }

    /**
     * @return the code of the course
     */
    public String getCode() {
        return mCode;
    }

    /**
     * Sets the code of the course
     *
     * @param mCode
     *            the new code of the course.
     */
    public void setCode(String code) {
        mCode = code;
    }

    /**
     * @return the description of the course
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Sets the description of the course
     *
     * @param mDescription
     *            the new description of the course
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * @return the {@link List} of {@link Event} related to this {@link Course}
     */
    public List<Event> getEvents() {
        return new ArrayList<Event>(mEvents);
    }

    /**
     * Sets a new {@link List} of {@link Event} related to this course
     *
     * @param mEvents
     *            the new {@link List} of {@link Event} related to this course
     */
    public void setEvents(List<Event> events) {
        this.mEvents = new ArrayList<Event>(events);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (mEvents == null) {
            return mName + ", Periods : " + mPeriods + ", Teacher : "
                    + mTeacher + ", nb Credits : " + mCredits;
        } else {
            return mName + ", Periods : " + mPeriods + ", Teacher : "
                    + mTeacher + ", nb Credits : " + mCredits + ", Events : "
                    + mEvents;
        }
    }

    /**
     * Returns a {@link Course} parsed from a {@link JSONObject} representing
     * it?
     *
     * @param jsonObject
     *            the JSONObject to parse
     * @return A Course filled with the informations from the JSON
     * @throws JSONException
     *             if the JSON is badly formated
     */
    public static Course parseFromJSON(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        String professorName = jsonObject.getString("professorName");
        int numberOfCredits = jsonObject.getInt("numberOfCredits");

        return new Course(code, name, description, professorName,
                numberOfCredits);
    }

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
        // test each member
        if (!this.getName().equals(otherCourse.getName())
                || !this.getPeriods().equals(otherCourse.getPeriods())
                || !this.getTeacher().equals(otherCourse.getTeacher())
                || this.getCredits() != otherCourse.getCredits()
                || !this.getCode().equals(otherCourse.getCode())
                || !this.getDescription().equals(otherCourse.getDescription())) {
            return false;
        }
        // all member are equals
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += mName.hashCode() + mTeacher.hashCode() + mCredits
                + mCode.hashCode() + mDescription.hashCode();
        for (Period p : mPeriods) {
            result += p.hashCode();
        }
        return result;
    }

    /**
     *
     * @return the periods of the course nicely compacted and formated for
     *         {@link CourseDetailsActivity}
     */
    public String toDisplayPeriod() {
        // Transform year's periods to week's period by removing duplicate
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

    /*
     * Parcelable ---------------- using setter and getter to check for property
     * in case of memory error or any problem that could happen at execution
     * between writing and reading and corrupt integrity.
     */
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

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * A {@link Parcelable.Creator} for the {@link Course} class.
     */
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
}
