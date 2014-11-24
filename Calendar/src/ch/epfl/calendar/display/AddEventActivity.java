package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import ch.epfl.calendar.App;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventActivity extends DefaultActionBarActivity {

    private EditText mNameEvent;
    private EditText mDescriptionEvent;
    private DatePicker mStartEventDate;

    private TimePicker mStartEventHour;

    private DatePicker mEndEventDate;
    private TimePicker mEndEventHour;

    private String mLinkedCourse = App.NO_COURSE;
    public static final int AUTH_ACTIVITY_CODE = 1;
    private Spinner mSpinnerCourses;
    private List<Course> mCourses = new ArrayList<Course>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        addEventActionBar();

        mNameEvent = (EditText) findViewById(R.id.name_event_text);
        mDescriptionEvent = (EditText) findViewById(R.id.description_event_text);

        mStartEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        mStartEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);

        mEndEventDate = (DatePicker) findViewById(R.id.end_event_picker_date);
        mEndEventHour = (TimePicker) findViewById(R.id.end_event_picker_hour);

        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mCourses = savedInstanceState.getParcelableArrayList("coursesList");
            callbackDownload(true, mCourses);
        } else {
            // Retrieve course for first time
            retrieveCourse();
        }

    }

    private void addEventActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("New Event");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retour = super.onCreateOptionsMenu(menu);
        MenuItem addEventItem = (MenuItem) menu.findItem(R.id.add_event);
        addEventItem.setVisible(false);
        this.invalidateOptionsMenu();
        return retour;
    }

    public Calendar createCalendar(int year, int month, int day, int hour,
            int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar;
    }

    private void transferData() {
        Calendar start = createCalendar(mStartEventDate.getYear(),
                mStartEventDate.getMonth(), mStartEventDate.getDayOfMonth(),
                mStartEventHour.getCurrentHour(),
                mStartEventHour.getCurrentMinute());
        Calendar end = createCalendar(mEndEventDate.getYear(),
                mEndEventDate.getMonth(), mEndEventDate.getDayOfMonth(),
                mEndEventHour.getCurrentHour(),
                mEndEventHour.getCurrentMinute());
        Event e = new Event(mNameEvent.getText().toString(),
                App.calendarToBasicFormatString(start),
                App.calendarToBasicFormatString(end),
                PeriodType.DEFAULT.toString(), mLinkedCourse, mDescriptionEvent
                        .getText().toString(), DBQuester.NO_ID);
        DBQuester dbQuester = new DBQuester();
        dbQuester.storeEvent(e);
    }

    public void finishActivity(View v) {
        transferData();
        finish();
    }

    private void retrieveCourse() {
        CalendarClientInterface calendarClient = new CalendarClient(this, this);
        calendarClient.getISAInformations();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the activity state
        savedInstanceState.putParcelableArrayList("coursesList",
                new ArrayList<Course>(mCourses));
        // System.out.println("Saving state");

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void callbackDownload(boolean success, List<Course> courses) {
        if (success) {
            this.mCourses = courses;

            final ArrayList<String> coursesName = new ArrayList<String>();

            coursesName.add("No connection with courses");

            for (Course course : courses) {
                coursesName.add(course.getName());
            }

            ArrayAdapter<String> coursesNameAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, coursesName);

            coursesNameAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerCourses.setAdapter(coursesNameAdapter);
            mSpinnerCourses
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                View view, int position, long id) {
                            if (position != 0) {
                                mLinkedCourse = coursesName.get(position);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            mLinkedCourse = App.NO_COURSE;
                        }

                    });
        } else {
            switchToAuthenticationActivity();
        }

    }
}