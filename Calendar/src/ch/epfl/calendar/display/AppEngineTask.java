package ch.epfl.calendar.display;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ch.epfl.calendar.R;
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
    private ProgressDialog mDialog;
    private Context mContext;
    private AppEngineListener mListener = null;
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
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setTitle(mContext.getString(R.string.be_patient));
        mDialog.setMessage(mContext.getString(R.string.loading_app_engine));
        mDialog.show();
    }

    @Override
    protected Course doInBackground(String... courseName) {
        return retrieveCourse(courseName[0]);
    }

    private Course retrieveCourse(String courseName) {
        AppEngineDatabaseInterface appEngineClient;
        Course result = null;

        try {
            appEngineClient = new AppEngineClient(
                    "http://versatile-hull-742.appspot.com");

            result = appEngineClient.getCourseByName(courseName);

        } catch (CalendarClientException e) {
            // TODO Auto-generated catch block
            mExceptionOccured = true;
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Course result) {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (mExceptionOccured) {
            mListener.onError(mContext, "Can't retrieve : " + result.getName());
        } else {
            mCourse = result;
            mListener.onSuccess();
        }
    }

}
