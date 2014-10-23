package ch.epfl.calendar;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.utils.GlobalPreferences;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.CoursesListActivity;


/**
 *
 * @author lweingart
 *
 */
public class MainActivity extends Activity {


	private static final int REQUEST_CODE = 1;

	private Button btnLogin;
	private Activity mThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThisActivity = this;
        this.btnLogin = (Button) this.findViewById(R.id.btnLogin);

        this.switchLoginLogout();
        
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
     * Handle button activation based on whether the user is authenticated or not.
     */
    private void switchLoginLogout() {
        Boolean isAuthenticated = GlobalPreferences.isAuthenticated(this);
        if (isAuthenticated) {
            btnLogin.setText(R.string.logout);
            btnLogin.setOnClickListener(new LogoutListener());
        } else {
            btnLogin.setText(R.string.login);
            btnLogin.setOnClickListener(new LoginListener());
        }
    }

    /**
     *
     * @author lweingart
     *
     */
    private class LogoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TequilaAuthenticationAPI.getInstance().clearSessionID(MainActivity.this.mThisActivity);

            MainActivity.this.switchLoginLogout();
        }
    }

    /**
     *
     * @author lweingart
     *
     */
    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity.this.startAuthenticationActivity();
        }
    }

    private void startAuthenticationActivity() {
        Intent displayAuthenticationActivityIntent = new Intent(this,
                AuthenticationActivity.class);
        this.startActivityForResult(displayAuthenticationActivityIntent,
                MainActivity.REQUEST_CODE);
    }

    public void switchToCoursesList(View view) {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
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
