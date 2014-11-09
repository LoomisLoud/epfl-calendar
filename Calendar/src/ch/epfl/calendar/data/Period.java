/**
 * 
 */
package ch.epfl.calendar.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A period is a date, a start time and an end time + the type (exercises,
 * lesson) and the rooms of a course
 * 
 * @author AblionGE
 * 
 */
public class Period implements Parcelable {
    private String mDate;
    private String mStartTime;
    private String mEndTime;
    private String mType;
    private List<String> mRooms;

    public Period(String date, String startTime, String endTime, String type,
            List<String> rooms) {
        this.mDate = date;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mType = type;
        this.mRooms = rooms;
    }

    /**
     * @return the mDate
     */
    public String getDate() {
        return mDate;
    }

    /**
     * @param mDate
     *            the mDate to set
     */
    public void setDate(String date) {
        this.mDate = date;
    }

    /**
     * @return the mStartTime
     */
    public String getStartTime() {
        return mStartTime;
    }

    public int getHourStartTime() {
        String[] tab = mStartTime.split(":");
        return Integer.parseInt(tab[0]);
    }

    public int getMinuteStartTime() {
        String[] tab = mStartTime.split(":");
        return Integer.parseInt(tab[1]);
    }

    /**
     * @param mStartTime
     *            the mStartTime to set
     */
    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    /**
     * @return the mEndTime
     */
    public String getEndTime() {
        return mEndTime;
    }

    /**
     * @param mEndTime
     *            the mEndTime to set
     */
    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param mType
     *            the mType to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @return the mRoom
     */
    public List<String> getRooms() {
        return new ArrayList<String>(mRooms);
    }

    /**
     * @param mRoom the mRoom to set
     */
    public void setRooms(List<String> room) {
        this.mRooms = new ArrayList<String>(room);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return mDate + " from " + mStartTime + " to " + mEndTime + " of type "
                + mType + " in rooms : " + mRooms + "\n";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(mDate);
        dest.writeString(mStartTime);
        dest.writeString(mEndTime);
        dest.writeString(mType);
        dest.writeList(mRooms);
    }

    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() {

        @Override
        public Period createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Period(source);
        }

        @Override
        public Period[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Period[size];
        }
    };

    public Period(Parcel in) {

        this.setDate(in.readString());
        this.setStartTime(in.readString());
        this.setEndTime(in.readString());
        this.setType(in.readString());
        mRooms = new ArrayList<String>();
        in.readList(mRooms, String.class.getClassLoader());

    }
    
    public JSONObject getJSONFromPeriod(String courseCode) throws JSONException {
        JSONObject jsonPeriod = new JSONObject();
        
        jsonPeriod.put("code", courseCode);
        jsonPeriod.put("date", this.mDate);
        jsonPeriod.put("startTime", this.mStartTime);
        jsonPeriod.put("endTime", this.mEndTime);
        jsonPeriod.put("type", this.mType);
        
        JSONObject jsonRooms = new JSONObject();
        for (int i = 0; i < this.mRooms.size(); i++) {
            jsonRooms.put(Integer.toString(i), this.mRooms.get(i));
        }
        jsonPeriod.put("rooms", jsonRooms);
        
        return jsonPeriod;
    }

    /**
     * 
     * @param jsonObject
     *            the JSONObject to parse.
     * @return A Period filled with the informations from the JSON
     * @throws JSONException
     */
    public static List<Period> parseFromJSON(JSONObject jsonObject) throws JSONException {
        List<Period> periodsList = new ArrayList<Period>();
        for (int i = 0; i < jsonObject.length(); i++) {
            JSONObject tempJSONObject = jsonObject.getJSONObject(Integer
                    .toString(i));
            String date = tempJSONObject.getString("date");
            String startTime = tempJSONObject.getString("startTime");
            String endTime = tempJSONObject.getString("endTime");
            String periodType = tempJSONObject.getString("periodType");
            List<String> rooms = getStringListFromJSONArray(tempJSONObject.getJSONArray("rooms"));
            Period period = new Period(date, startTime, endTime, periodType,
                    rooms);
            periodsList.add(period);
        }

        return periodsList;
    }

    /**
     * Taken from my homework 2. Returns a String List
     * 
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    private static List<String> getStringListFromJSONArray(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<String>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(jsonArray.getString(i));
        }

        return stringList;
    }
}
