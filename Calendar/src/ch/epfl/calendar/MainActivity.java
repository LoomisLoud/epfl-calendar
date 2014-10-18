package ch.epfl.calendar;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import ch.epfl.calendar.data.Course;
import ch.epfl.utils.xmlparser.ISAXMLParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author lameAppInc
 *
 */
public class MainActivity extends Activity {
    private static final int READ_TIMEOUT = 7000;
    private static final int CONNECT_TIMEOUT = 8000;
    private static final String TAG = "MainActivity";
    private static final int BUFFER_LENGTH = 100000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		new FetchInformations().execute();
      
    }

    public String doInternetStuff() {
        
        /*****************************TEST XML PARSER*************************/
        String contentAsString = "<data status=\"Termine\" date=\"20141017 16:08:36\" key=\"1864682915\" dateFin=\"19.10.2014\" dateDebut=\"13.10.2014\"><study-period><id>1808047617</id><date>13.10.2014</date><duration>105</duration><day>1</day><startTime>14:15</startTime><endTime>16:00</endTime><type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type><course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course><room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text></name></room></study-period><study-period><id>1808048631</id><date>13.10.2014</date><duration>105</duration><day>1</day><startTime>16:15</startTime><endTime>18:00</endTime><type><text lang=\"en\">Exercises</text><text lang=\"fr\">Exercices</text></type><course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course><room><id>2189182</id><code>GCB331</code><name><text lang=\"fr\">GC B3 31</text></name></room><room><id>2189101</id><code>GCA331</code><name><text lang=\"fr\">GC A3 31</text></name></room><room><id>1614950371</id><code>GCD0386</code><name><text lang=\"fr\">GC D0 386</text></name></room><room><id>2189114</id><code>GCB330</code><name><text lang=\"fr\">GC B3 30</text></name></room></study-period><study-period><id>1808331964</id><date>14.10.2014</date><duration>105</duration><day>2</day><startTime>08:15</startTime><endTime>10:00</endTime><type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type><course><id>24092923</id><name><text lang=\"fr\">Software engineering</text></name></course><room><id>4255362</id><code>BC02</code><name><text lang=\"fr\">BC 02</text></name></room><room><id>4255327</id><code>BC01</code><name><text lang=\"fr\">BC 01</text></name></room><room><id>4255386</id><code>BC03</code><name><text lang=\"fr\">BC 03</text></name></room><room><id>4255408</id><code>BC04</code><name><text lang=\"fr\">BC 04</text></name></room></study-period></data>";
        Log.d(TAG, contentAsString);

        try {
            List<Course> courses = new ISAXMLParser().parse(new ByteArrayInputStream(contentAsString.getBytes("UTF-8")));
            System.out.println("Size list : " + courses.size());
            for (Course course: courses) {
                System.out.println(course.toString());
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return contentAsString;
        /*********************************************************************/
    }
    
    /**
     * Read an InputStream and returns the content as a String
     * @param inputStream
     * @param bufferLen
     * @return
     * @author AblionGE
     * @throws IOException 
     */
    private String readInput(InputStream inputStream, int bufferLen) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(inputStream, "UTF-8");
        char[] buffer = new char[bufferLen];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 
     * @author AblionGE
     *
     */
    private class FetchInformations extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return doInternetStuff();
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i("result request", result);
        }
    }
}
