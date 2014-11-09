package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.utils.GlobalPreferences;


/**
 *
 * @author lweingart
 *
 */
public class MainActivity extends Activity {

	private static final int REQUEST_CODE = 1;

//	private Button btnLogin;
	private Activity mThisActivity;

    private CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThisActivity = this;
//        this.btnLogin = (Button) this.findViewById(R.id.btnLogin);

        //this.switchLoginLogout();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        initializeCalendar();

        //FIXME : At the beginning of the application, we "logout" the user
        TequilaAuthenticationAPI.getInstance().clearSessionID(mThisActivity);

        populateCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_courses_list:
                switchToCoursesList();
                return true;
            case R.id.action_add_events:
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_draft_activity:
                switchToDraftActivity();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_update_activity:
                populateCalendar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handle button activation based on whether the user is authenticated or not.
     */
    /*private void switchLoginLogout() {
        Boolean isAuthenticated = GlobalPreferences.isAuthenticated(this);
        if (isAuthenticated) {
            btnLogin.setText(R.string.logout);
            btnLogin.setOnClickListener(new LogoutListener());
        } else {
            btnLogin.setText(R.string.login);
            btnLogin.setOnClickListener(new LoginListener());
        }
    }*/

    /**
     *
     * @author lweingart
     *
     */
    /*private class LogoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TequilaAuthenticationAPI.getInstance().clearSessionID(MainActivity.this.mThisActivity);

            MainActivity.this.switchLoginLogout();
        }
    }*/

    /**
     *
     * @author lweingart
     *
     */
    /*private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity.this.startAuthenticationActivity();
        }
    }*/

    public void switchToCoursesList() {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
    }

    public void switchToDraftActivity() {
        Intent draftActivityIntent = new Intent(this,
                DraftActivity.class);
        startActivity(draftActivityIntent);
    }

    private void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivityIntent = new Intent(this,
                AuthenticationActivity.class);
        this.startActivityForResult(displayAuthenticationActivityIntent,
                MainActivity.REQUEST_CODE);
    }

    public void initializeCalendar() {
        mCalendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        mCalendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        mCalendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        mCalendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        mCalendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        mCalendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        mCalendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        mCalendar.setOnDateChangeListener(new OnDateChangeListener() {
                       //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<Course> populateCalendar() {
        CalendarClient cal = new CalendarClient(MainActivity.this.mThisActivity);
        List<Course> courses = new ArrayList<Course>();
        if (!GlobalPreferences.isAuthenticated(this)) {
            switchToAuthenticationActivity();
        } else {
            try {
                courses = cal.getISAInformations();
            } catch (CalendarClientException e) {
                //We have a problem...
            	e.printStackTrace();
            }
        }

        //To see if it works
        for (Course c : courses) {
            System.out.println(c.toString());
        }
        return courses;
    }

}
