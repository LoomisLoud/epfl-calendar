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
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.GlobalPreferences;
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

    private Activity mContext = null;

    public CalendarClient(Activity activity) {
        this.mContext = activity;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public List<Course> getISAInformations()
    	throws CalendarClientException, TequilaAuthenticationException {

        /*****************************TEST XML PARSER*************************/
//        String contentAsString = "<data status=\"Termine\" date=\"20141017 16:08:36\" "
//                + "key=\"1864682915\" dateFin=\"19.10.2014\" dateDebut=\"13.10.2014\">"
//                + "<study-period><id>1808047617</id><date>13.10.2014</date><duration>105</duration>"
//                + "<day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
//                + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type>"
//                + "<course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
//                + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
//                + "</name></room></study-period><study-period><id>1808048631</id><date>13.10.2014</date>"
//                + "<duration>105</duration><day>1</day><startTime>16:15</startTime><endTime>18:00</endTime>"
//                + "<type><text lang=\"en\">Exercises</text><text lang=\"fr\">Exercices</text></type><course>"
//                + "<id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course><room><id>2189182</id>"
//                + "<code>GCB331</code><name><text lang=\"fr\">GC B3 31</text></name></room><room><id>2189101</id>"
//                + "<code>GCA331</code><name><text lang=\"fr\">GC A3 31</text></name></room><room><id>1614950371</id>"
//                + "<code>GCD0386</code><name><text lang=\"fr\">GC D0 386</text></name></room><room><id>2189114</id>"
//                + "<code>GCB330</code><name><text lang=\"fr\">GC B3 30</text></name></room></study-period>"
//                + "<study-period><id>1808331964</id><date>14.10.2014</date><duration>105</duration><day>2</day>"
//                + "<startTime>08:15</startTime><endTime>10:00</endTime><type><text lang=\"en\">Lecture</text>"
//                + "<text lang=\"fr\">Cours</text></type><course><id>24092923</id><name>"
//                + "<text lang=\"fr\">Software engineering</text></name></course><room><id>4255362</id>"
//                + "<code>BC02</code><name><text lang=\"fr\">BC 02</text></name></room><room><id>4255327</id>"
//                + "<code>BC01</code><name><text lang=\"fr\">BC 01</text></name></room><room><id>4255386</id>"
//                + "<code>BC03</code><name><text lang=\"fr\">BC 03</text></name></room><room><id>4255408</id>"
//                + "<code>BC04</code><name><text lang=\"fr\">BC 04</text></name></room></study-period></data>";


    	List<Course> coursesList = new ArrayList<Course>();
    	List<String> namesOfCourses = new ArrayList<String>();

    	if (!GlobalPreferences.isAuthenticated(mContext)) {
			switchToAuthenticationActivity();
		} else {
	        try {
	            //coursesList = ISAXMLParser.parse(new ByteArrayInputStream(contentAsString.getBytes("UTF-8")));
	            coursesList = ISAXMLParser.parse(new ByteArrayInputStream(
	                    (getIsaTimetableOnline(this.mContext)).getBytes("UTF-8")));
	        } catch (ParsingException e) {
	            throw new CalendarClientException("Parsing Exception");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }

	        for (Course course : coursesList) {
	            namesOfCourses.add(course.getName());
            }
        }
        return coursesList;
    }

	private void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivityIntent = new Intent(mContext, AuthenticationActivity.class);
        mContext.startActivityForResult(displayAuthenticationActivityIntent, MainActivity.AUTH_ACTIVITY_CODE);
    }

	private String getIsaTimetableOnline(Context context) {

        try {
            String result =
                    new TequilaAuthenticationTask(
                            mContext,
                            new TequilaAuthenticationHandler(),
                            null,
                            null).execute(null, null).get();
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
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSuccess(String sessionID) {
            // store the sessionID in the preferences
            TequilaAuthenticationAPI.getInstance().setSessionID(mContext, sessionID);
            Toast.makeText(mContext, R.string.updated, Toast.LENGTH_SHORT).show();
        }
    }
}
