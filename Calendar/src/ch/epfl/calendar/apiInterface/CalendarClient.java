/**
 *
 */
package ch.epfl.calendar.apiInterface;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.isaparser.ISAXMLParser;

/**
 * For now uses a pre-built xml string and parses it.
 *
 * @author gilbrechbuhler
 *
 */
public class CalendarClient implements CalendarClientInterface {

	public static final String TAG = "CalendarClient Class::";

    private Activity mParentActivity = null;

    public CalendarClient(Activity activity) {
        this.mParentActivity = activity;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public List<Course> getISAInformations() throws CalendarClientException {

    	List<Course> coursesList = new ArrayList<Course>();
    	List<String> namesOfCourses = new ArrayList<String>();

        try {
        	byte[] timeTableBytes = getIsaTimetableOnline(mParentActivity).getBytes("UTF-8");
            coursesList = ISAXMLParser.parse(new ByteArrayInputStream(timeTableBytes));
        } catch (UnsupportedEncodingException e) {
        	throw new CalendarClientException("Parsing Exception", e);
        }

        for (Course course : coursesList) {
            namesOfCourses.add(course.getName());
        }
        return coursesList;
    }

	private String getIsaTimetableOnline(Context context) {
		boolean exceptionOccured = false;
		String errMessage = "";
		Exception ex = new Exception();
		String result = null;
        try {
            result = new TequilaAuthenticationTask(mParentActivity,
                              new TequilaAuthenticationHandler(),
                              null,
                              null)
                .execute(null, null)
                .get();
        } catch (InterruptedException e) {
        	exceptionOccured = true;
        	errMessage = "INTERRUPTED";
        	ex = e;
        } catch (ExecutionException e) {
        	exceptionOccured = true;
        	errMessage = "EXECUTION";
        	ex = e;
        } finally {
        	if (exceptionOccured) {
				throw new TequilaAuthenticationException(errMessage, ex);
			}
        }
        return result;
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
        }
    }
}
