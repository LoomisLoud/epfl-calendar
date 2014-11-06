/**
 * 
 */
package ch.epfl.calendar.apiInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ch.epfl.calendar.authentication.HttpClientFactory;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
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
    
    private Context mContext = null;
    
    public CalendarClient(Context context) {
        this.mContext = context;
    }
    
    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.CalendarClientInterface#getCoursesFromStudent(
     * ch.epfl.calendar.mock.MockStudent)
     */
    @Override
    public List<Course> getISAInformations() throws CalendarClientException, TequilaAuthenticationException {
        /*****************************TEST XML PARSER*************************/
        String contentAsString = "<data status=\"Termine\" date=\"20141017 16:08:36\" "
                + "key=\"1864682915\" dateFin=\"19.10.2014\" dateDebut=\"13.10.2014\">"
                + "<study-period><id>1808047617</id><date>13.10.2014</date><duration>105</duration>"
                + "<day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
                + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type>"
                + "<course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
                + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
                + "</name></room></study-period><study-period><id>1808048631</id><date>13.10.2014</date>"
                + "<duration>105</duration><day>1</day><startTime>16:15</startTime><endTime>18:00</endTime>"
                + "<type><text lang=\"en\">Exercises</text><text lang=\"fr\">Exercices</text></type><course>"
                + "<id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course><room><id>2189182</id>"
                + "<code>GCB331</code><name><text lang=\"fr\">GC B3 31</text></name></room><room><id>2189101</id>"
                + "<code>GCA331</code><name><text lang=\"fr\">GC A3 31</text></name></room><room><id>1614950371</id>"
                + "<code>GCD0386</code><name><text lang=\"fr\">GC D0 386</text></name></room><room><id>2189114</id>"
                + "<code>GCB330</code><name><text lang=\"fr\">GC B3 30</text></name></room></study-period>"
                + "<study-period><id>1808331964</id><date>14.10.2014</date><duration>105</duration><day>2</day>"
                + "<startTime>08:15</startTime><endTime>10:00</endTime><type><text lang=\"en\">Lecture</text>"
                + "<text lang=\"fr\">Cours</text></type><course><id>24092923</id><name>"
                + "<text lang=\"fr\">Software engineering</text></name></course><room><id>4255362</id>"
                + "<code>BC02</code><name><text lang=\"fr\">BC 02</text></name></room><room><id>4255327</id>"
                + "<code>BC01</code><name><text lang=\"fr\">BC 01</text></name></room><room><id>4255386</id>"
                + "<code>BC03</code><name><text lang=\"fr\">BC 03</text></name></room><room><id>4255408</id>"
                + "<code>BC04</code><name><text lang=\"fr\">BC 04</text></name></room></study-period></data>";
        
        List<Course> coursesList = new ArrayList<Course>();
        List<String> namesOfCourses = new ArrayList<String>();

        try {
            //coursesList = ISAXMLParser.parse(new ByteArrayInputStream(contentAsString.getBytes("UTF-8")));
            coursesList = ISAXMLParser.parse(getIsaTimetableOnline(this.mContext));
        } catch (ParsingException e) {
            throw new CalendarClientException();
        } catch (TequilaAuthenticationException e) {
            throw new TequilaAuthenticationException();
        }
        
        for (Course course : coursesList) {
            namesOfCourses.add(course.getName());
        }
            
        return coursesList;
    }
    
    private InputStream getIsaTimetableOnline(Context context) {
        
        try {
            InputStream result = new DownloadHttpPage().execute(context).get();
            return result;
            //FIXME : Manage exceptions
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTED");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("EXECUTION");
            throw new TequilaAuthenticationException();
        }
        return null;
    }
    
    /**
     * This class is used to load an http content
     * @author AblionGE
     *
     */
    private class DownloadHttpPage extends AsyncTask<Context, Void, InputStream> {
        @Override
        protected InputStream doInBackground(Context... params) {
            String sessionID = null;
            System.out.println("AUTHENTICATED : "+GlobalPreferences.isAuthenticated(params[0]));
            if (GlobalPreferences.isAuthenticated(params[0])) {
                sessionID = TequilaAuthenticationAPI.getInstance().getSessionID(params[0]);
                if (sessionID.equals("")) {
                    throw new TequilaAuthenticationException("Need to be authenticated");
                }
                return downloadUrl(TequilaAuthenticationAPI.getInstance().getIsAcademiaLoginURL(),
                        params[0], sessionID);
            } else {
                throw new TequilaAuthenticationException("Need to be authenticated");
            }
        }
        
        
        /**
         * Download the data from the given URL and return a QuizQuestion Object
         * @param url
         * @return QuizQuestion object
         * 
         * @author AblionGE
         */
        private InputStream downloadUrl(String url, Context context, String sessionID) {
            AbstractHttpClient client = HttpClientFactory.getInstance();
            TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();
            HttpResponse mRespGetTimetable = null;
            try {
                Log.i("INFO : ", "Try getting access to ISA Services");
                Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL());
                
                
                System.out.println("SESSION ID : " + sessionID);
                
                
                HttpGet sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
                sessionReq.addHeader("Set-Cookie", "JSESSIONID="+sessionID);
                client.getCookieStore().addCookie(new BasicClientCookie("JSESSIONID", sessionID));
                mRespGetTimetable = client
                        .execute(sessionReq, new BasicHttpContext());
                Log.i("INFO : ", "Http code received when trying access to ISA Service : "
                        + mRespGetTimetable.getStatusLine().getStatusCode());
                
                return mRespGetTimetable.getEntity().getContent();
                //FIXME : MANAGE exceptions
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
}
