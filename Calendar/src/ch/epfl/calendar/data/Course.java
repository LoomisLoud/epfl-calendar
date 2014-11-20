package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.List;

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

    public Course(String name, String date, String startTime, String endTime,
            String type, List<String> rooms) {
        this.mName = name;
        this.mPeriods = new ArrayList<Period>();
        this.addPeriod(new Period(date, startTime, endTime, type, rooms));
        this.mTeacher = null;
        this.mCredits = 0;
    }

    // FIXME : DELETE !!! ???
    public Course(String name) {
        this.setName(name);
        this.mPeriods = new ArrayList<Period>();
        this.setTeacher(null);
        this.setCredits(0);
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return mName + ", Periods : " + mPeriods + ", Teacher : " + mTeacher
                + ", nb Credits : " + mCredits;
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
        if (!this.getName().equals(otherCourse.getName())) {
            System.out.println("Name aren't equals ");
            return false;
        }
        System.out.println("Names equals");
        if (this.getPeriods().size() == otherCourse.getPeriods().size()) {
            for (int i = 0; i < this.getPeriods().size(); i++) {
                if (!this.getPeriods().get(i).equals(otherCourse.getPeriods().get(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (!this.getTeacher().equals(otherCourse.getTeacher())) {
            return false;
        }
        if (this.getCredits() != otherCourse.getCredits()) {
            return false;
        }
        if (!this.getCode().equals(otherCourse.getCode())) {
            return false;
        }
        if (!this.getDescription().equals(otherCourse.getDescription())) {
            return false;
        }
        //all member are equals
        return true;
    }
    
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

    
    // Parcelable ----------------
    // using setter and getter to check for property in case of memory error or any problem that could happen
    // at execution between writing and reading and corrupt integrity.
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeList(getPeriods());
        parcel.writeString(getTeacher());
        parcel.writeInt(getCredits());
        parcel.writeString(getCode());
        parcel.writeString(getDescription());
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
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
