package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.data.Course;

/**
 * @author LoomisLoud
 * 
 */
public class AddEventActivity extends Activity implements
        CalendarClientDownloadInterface {

    private EditText mNameEvent;
    private DatePicker mStartEventDate;

    private TimePicker mStartEventHour;

    private DatePicker mEndEventDate;
    private TimePicker mEndEventHour;

    public static final int AUTH_ACTIVITY_CODE = 1;
    private Spinner mSpinnerCourses;
    private List<Course> mCourses = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mNameEvent = (EditText) findViewById(R.id.name_event_text);
        //
        mStartEventDate = (DatePicker) findViewById(R.id.start_event_picker_date);
        mStartEventHour = (TimePicker) findViewById(R.id.start_event_picker_hour);
        //
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

    private void transferData() {
        Intent i = getIntent();
        i.putExtra("nameInfo", mNameEvent.getText().toString());

        i.putExtra("startYear", mStartEventDate.getYear());
        i.putExtra("startMonth", mStartEventDate.getMonth());
        i.putExtra("startDay", mStartEventDate.getDayOfMonth());
        i.putExtra("startHour", mStartEventHour.getCurrentHour());
        i.putExtra("startMinutes", mStartEventHour.getCurrentMinute());

        i.putExtra("endYear", mEndEventDate.getYear());
        i.putExtra("endMonth", mEndEventDate.getMonth());
        i.putExtra("endDay", mEndEventDate.getDayOfMonth());
        i.putExtra("endHour", mEndEventHour.getCurrentHour());
        i.putExtra("endMinutes", mEndEventHour.getCurrentMinute());

        setResult(RESULT_OK, i);
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

    private void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivtyIntent = new Intent(this,
                AuthenticationActivity.class);
        this.startActivityForResult(displayAuthenticationActivtyIntent,
                AUTH_ACTIVITY_CODE);
    }

    @Override
    public void callbackDownload(boolean success, List<Course> courses) {
        if (success) {
            this.mCourses = courses;

            ArrayList<String> coursesName = new ArrayList<String>();

            coursesName.add("No connection with courses");

            for (Course course : courses) {
                coursesName.add(course.getName());
            }

            ArrayAdapter<String> coursesNameAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, coursesName);

            coursesNameAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerCourses.setAdapter(coursesNameAdapter);
        } else {
            switchToAuthenticationActivity();
        }

    }
}