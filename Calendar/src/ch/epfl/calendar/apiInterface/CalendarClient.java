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

    public CalendarClient(Activity activity) {
        this.mParentActivity = activity;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public List<Course> getISAInformations()
    	throws CalendarClientException, TequilaAuthenticationException {

    	List<Course> coursesList = new ArrayList<Course>();
    	List<String> namesOfCourses = new ArrayList<String>();

        try {
            coursesList = ISAXMLParser.
            		parse(new ByteArrayInputStream((getIsaTimetableOnline(mParentActivity)).getBytes("UTF-8")));
        } catch (ParsingException e) {
            throw new CalendarClientException("Parsing Exception");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (Course course : coursesList) {
            namesOfCourses.add(course.getName());
        }
        return coursesList;
    }

	private String getIsaTimetableOnline(Context context) {

        try {
            String result =
                    new TequilaAuthenticationTask(
                            mParentActivity,
                            new TequilaAuthenticationHandler(),
                            null,
                            null)
            			.execute(null, null)
            			.get();
            return result;
        } catch (InterruptedException e) {
            Log.e(TAG, "INTERRUPTED");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e(TAG, "EXECUTION");
            throw new TequilaAuthenticationException(e);
        }
        return null;
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
