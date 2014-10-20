package ch.epfl.calendar;


import java.util.List;

import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.data.Course;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author lweingart
 *
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		new FetchInformations().execute();
      
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
     * Fetch the informations of a student
     * !!!!!!!!!!Works as a Mock for the moment!!!!!!!!!!!!!
     * -> no connection, just an XML string
     * @author AblionGE
     *
     */
    private class FetchInformations extends AsyncTask<String, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(String... params) {
            CalendarClientInterface fetcher = new CalendarClient();
            try {
                return fetcher.getISAInformations();
            } catch (CalendarClientException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Course> result) {
            //Log.i("result request", result);
            for (Course course: result) {
                System.out.println(course.toString());
            }
        }
    }
}
