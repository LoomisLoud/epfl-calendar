package ch.epfl.calendar.display;

import android.content.Context;
import android.os.AsyncTask;
import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.AppEngineDatabaseInterface;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;

/**
 * Extends {@link AsyncTask} and fetches data on the AppEngine.
 * 
 * @author winni74
 * 
 */
public class AppEngineTask extends AsyncTask<String, Void, Course> {
    private Course mCourse;
    private Context mContext;
    private AppEngineListener mListener = null;
    private AppEngineDatabaseInterface mAppEngineClient;
    private boolean mExceptionOccured = false;

    /**
     * A listener which will be triggered at the end of a download from the App
     * Engine.
     * 
     * @author Maxime
     * 
     */
    public interface AppEngineListener {
        /**
         * The method to call when an error occured during download
         * 
         * @param context
         *            the context of the {@link Activity} using this Task
         * @param msg
         *            the error message
         */
        void onError(Context context, String msg);

        /**
         * The method to call when everything went well
         */
        void onSuccess();
    }

    /**
     * The constructor of this class
     * 
     * @param context
     *            the context of the {@link Activity} using this task
     * @param listener
     *            the {@link AppEngineListener} implementation to use
     */
    public AppEngineTask(Context context, AppEngineListener listener) {
        mContext = context;
        mListener = listener;
    }

    /**
     * 
     * @return the fetched course
     */
    public Course getCourse() {
        return mCourse;
    }

    @Override
    protected Course doInBackground(String... courseName) {
        return retrieveCourse(courseName[0]);
    }

    private void setCourse(Course course) {
        mCourse = course;
    }

    private Course retrieveCourse(String courseName) {
        Course result = new Course(courseName);

        try {
            mAppEngineClient = new AppEngineClient(
                    "http://versatile-hull-742.appspot.com");

            result = getAppEngineClient().getCourseByName(courseName);

        } catch (CalendarClientException e) {
            mExceptionOccured = true;
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Course result) {
        if (mExceptionOccured) {
            mListener.onError(mContext, "Can't retrieve : " + result.getName());
        } else {
            setCourse(result);
            mListener.onSuccess();
        }
    }

    public AppEngineDatabaseInterface getAppEngineClient() {
        return mAppEngineClient;
    }
}
