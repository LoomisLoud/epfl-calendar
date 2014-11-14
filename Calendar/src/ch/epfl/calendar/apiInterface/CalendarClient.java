/**
 *
 */
package ch.epfl.calendar.apiInterface;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.NetworkException;
import ch.epfl.calendar.utils.isaparser.ISAXMLParser;
import ch.epfl.calendar.utils.isaparser.ParsingException;

/**
 * For now uses a pre-built xml string and parses it.
 *
 * @author gilbrechbuhler
 *
 */
public class CalendarClient implements CalendarClientInterface {

	public static final String TAG = "CalendarClient Class::";

    private Activity mParentActivity = null;
    private CalendarClientDownloadInterface mObjectActivity = null;
    private TequilaAuthenticationTask task = null;
    private String mResult = null;

    public CalendarClient(Activity activity, CalendarClientDownloadInterface objectActivity) {
        this.mParentActivity = activity;
        this.mObjectActivity = objectActivity;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public void getISAInformations() throws CalendarClientException, TequilaAuthenticationException {

        List<Course> coursesList = new ArrayList<Course>();
        List<String> namesOfCourses = new ArrayList<String>();

        try {
            getIsaTimetableOnline(mParentActivity);
//          byte[] timeTableBytes = getIsaTimetableOnline(mParentActivity).getBytes("UTF-8");
//            coursesList = new ISAXMLParser().parse(new ByteArrayInputStream(timeTableBytes));
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG + "UnsupportedEncodingException", e.getMessage());
//            throw new CalendarClientException(e);
//        } catch (TequilaAuthenticationException e) {
//            Log.e(TAG + "TequilaAuthenticationException", e.getMessage());
//            throw new TequilaAuthenticationException(e);
//        } catch (ParsingException e) {
//            Log.e(TAG + "ParsingException", e.getMessage());
//            //We don't want that the user sees this exception
//        } catch (NullPointerException e) {
//            Log.e(TAG + "NullPointerException", e.getMessage());
//            //We don't want that the user sees this exception
        } catch (NetworkException e) {
            Log.e(TAG + "NetworkException", e.getMessage());
            //This Exception is already shown to the user
        }
//
//        for (Course course : coursesList) {
//            namesOfCourses.add(course.getName());
//        }
    }
    

    private void getIsaTimetableOnline(Context context) throws TequilaAuthenticationException, NetworkException {
        boolean exceptionOccured = false;
        String errMessage = "";
        Exception ex = new Exception();
        String result = null;
//        try {
        task = new TequilaAuthenticationTask(mParentActivity,
                          new TequilaAuthenticationHandler(),
                          null,
                          null);
        task.execute(null, null);
    }
//                .get();
            
//            if (result.equals(mParentActivity.getString(R.string.network_unreachable))) {
//                throw new NetworkException(
//                        "Getting timetable : " + mParentActivity.getString(R.string.network_unreachable));
//            } else if (result.equals(mParentActivity.getString(R.string.error_wrong_credentials))) {
//                TequilaAuthenticationAPI.getInstance().clearStoredData(mParentActivity);
//                throw new TequilaAuthenticationException(mParentActivity.getString(R.string.error_disconnected));
//            }
//        } catch (InterruptedException e) {
//            exceptionOccured = true;
//            errMessage = "Getting timetable : " + mParentActivity.getString(R.string.error_interruption);
//            ex = e;
//        } catch (ExecutionException e) {
//            exceptionOccured = true;
//            errMessage = "Getting timetable : " + mParentActivity.getString(R.string.error_execution);
//            ex = e;
//        } catch (CancellationException e) {
//            exceptionOccured = true;
//            errMessage = "Getting timetable : " + mParentActivity.getString(R.string.error_authentication_cancel);
//            ex = e;
//        } finally {
//            if (exceptionOccured) {
//                throw new TequilaAuthenticationException(errMessage, ex);
//            }
//        }
//        return result;
//    }

    private void callback() throws TequilaAuthenticationException, CalendarClientException {
        List<Course> coursesList = new ArrayList<Course>();
        try {
            byte[] timeTableBytes = task.getResult().getBytes("UTF-8");
            System.out.println("COUCOU1");
            coursesList = new ISAXMLParser().parse(new ByteArrayInputStream(timeTableBytes));
            System.out.println("COUCOU2");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG + "UnsupportedEncodingException", e.getMessage());
            throw new CalendarClientException(e);
        } catch (ParsingException e) {
            Log.e(TAG + "ParsingException", e.getMessage());
            //We don't want that the user sees this exception
        } catch (NullPointerException e) {
            Log.e(TAG + "NullPointerException", e.getMessage());
            //We don't want that the user sees this exception
        }
        mObjectActivity.callbackDownload(coursesList);
    }
    
    /**
     * A Handler that manage the onError and onSuccess function for Tequila Authentication
     * @author AblionGE
     *
     */
    private class TequilaAuthenticationHandler implements TequilaAuthenticationListener {
        @Override
        public void onError(String msg) {
            Toast.makeText(mParentActivity, msg, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSuccess(String sessionID) {
            // store the sessionID in the preferences
            TequilaAuthenticationAPI.getInstance().setSessionID(mParentActivity, sessionID);
            Toast.makeText(mParentActivity, R.string.updated, Toast.LENGTH_SHORT).show();
            try {
                callback();
            } catch (TequilaAuthenticationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CalendarClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
