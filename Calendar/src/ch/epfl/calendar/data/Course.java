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
        //TODO implement teacher and credits
        this.mTeacher = "John Doe";
        this.mCredits = 24;
    }

    // FIXME : DELETE !!! ???
    public Course(String name) {
        this.setName(name);
        this.mPeriods = new ArrayList<Period>();
        this.setTeacher(null);
        this.setCredits(0);
    }
    
    /**
     * Builds a course from a parcel.
     * @param in a Parcel ?
     */
    public Course(Parcel in) {

        this.setName(in.readString());
        mPeriods = new ArrayList<Period>();
        in.readList(mPeriods, Period.class.getClassLoader());
        this.setTeacher(in.readString());
        this.setCredits(in.readInt());
    }
    
    /**
     * Builds a full course. Used in {@link ch.epfl.calendar.data.Course#parseFromJSON(JSONObject)}
     * @param code the code of the course
     * @param name the name of the course
     * @param description the description of the course
     * @param professorName the name of the Professor teahcing the course
     * @param numberOfCredits the number of credits for the course
     */
    public Course(String code, String name, String description, String professorName, int numberOfCredits) {
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
        return mPeriods;
    }

    /**
     * @param mPeriods
     *            the mPeriods to set
     */
    public void setPeriods(List<Period> periods) {
        this.mPeriods = periods;
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
     * @param mCode the mCode to set
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
     * @param mDescription the mDescription to set
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

    /*
     * Implementation of Parcelable method
     */
    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mName);
        dest.writeList(mPeriods);
        dest.writeString(mTeacher);
        dest.writeInt(mCredits);

    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {

        @Override
        public Course createFromParcel(Parcel source) {

            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {

            return new Course[size];
        }

    };
    
    /**
     * 
     * @param jsonObject the JSONObject to parse.
     * @return A Course filled with the informations from the JSON
     * @throws JSONException
     */
    public static Course parseFromJSON(JSONObject jsonObject) throws JSONException {
        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        String professorName = jsonObject.getString("professorName");
        int numberOfCredits = jsonObject.getInt("numberOfCredits");
                
        return new Course(code, name, description, professorName, numberOfCredits);
    }
}
