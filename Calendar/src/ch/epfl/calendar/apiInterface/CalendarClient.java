/**
 *
 */
package ch.epfl.calendar.apiInterface;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;
import ch.epfl.calendar.data.Course;
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
    private CalendarClientDownloadInterface mDownloadInterface = null;
    private TequilaAuthenticationTask mTask = null;

    public CalendarClient(Activity activity, CalendarClientDownloadInterface downloadInterface) {
        this.mParentActivity = activity;
        this.mDownloadInterface = downloadInterface;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public void getISAInformations() {
        mTask = new TequilaAuthenticationTask(mParentActivity,
                new TequilaAuthenticationHandler(),
                null,
                null);
        mTask.execute(null, null);
    }

    private void callback(boolean success) throws TequilaAuthenticationException, CalendarClientException {
        List<Course> coursesList = new ArrayList<Course>();
        if (success) {
            try {
                byte[] timeTableBytes = mTask.getResult().getBytes("UTF-8");
                coursesList = new ISAXMLParser().parse(new ByteArrayInputStream(timeTableBytes));
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
        }
        mDownloadInterface.callbackDownload(success, coursesList);
    }
    
    /**
     * A Handler that manages the onError and onSuccess function for Tequila Authentication
     * @author AblionGE
     *
     */
    private class TequilaAuthenticationHandler implements TequilaAuthenticationListener {
        @Override
        public void onError(String msg) {
            boolean exceptionOccured = false;
            String errMessage = "";
            if (msg.equals(mParentActivity.getString(R.string.error_disconnected))) {
                TequilaAuthenticationAPI.getInstance().clearStoredData(mParentActivity);
            }
            Toast.makeText(mParentActivity, msg, Toast.LENGTH_LONG).show();
            try {
                callback(false);
            } catch (TequilaAuthenticationException e) {
                errMessage = e.getMessage();
            } catch (CalendarClientException e) {
                errMessage = e.getMessage();
            }
            if (!exceptionOccured) {
                Log.i("Unexpected error : ", errMessage);
            }
        }
        @Override
        public void onSuccess(String sessionID) {
            // store the sessionID in the preferences
            TequilaAuthenticationAPI.getInstance().setSessionID(mParentActivity, sessionID);
            
            boolean exceptionOccured = false;
            String errMessage = "";
            try {
                callback(true);
            } catch (TequilaAuthenticationException e) {
                errMessage = e.getMessage();
            } catch (CalendarClientException e) {
                errMessage = e.getMessage();
            }
            if (exceptionOccured) {
                Toast.makeText(mParentActivity, errMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}
