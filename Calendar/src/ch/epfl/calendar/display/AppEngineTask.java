package ch.epfl.calendar.display;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.AppEngineDatabaseInterface;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;

/**
 * @author Maxime
 * 
 */
public class AppEngineTask extends AsyncTask<String, Void, Course> {
    private Course mCourse;
    private Context mContext;
    private AppEngineListener mListener = null;
    private AppEngineDatabaseInterface mAppEngineClient;
    private boolean mExceptionOccured = false;
    
    /**
     * @author Maxime
     * 
     */
    public interface AppEngineListener {
        void onError(Context context, String msg);

        void onSuccess();
    }

    public AppEngineTask(Context context, AppEngineListener listener) {
        mContext = context;
        mListener = listener;
    }

    public Course getCourse() {
        return mCourse;
    }

    @Override
    protected Course doInBackground(String... courseName) {
        return retrieveCourse(courseName[0]);
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
            mCourse = result;
            mListener.onSuccess();
        }
    }

    public AppEngineDatabaseInterface getAppEngineClient() {
        return mAppEngineClient;
    }
}
