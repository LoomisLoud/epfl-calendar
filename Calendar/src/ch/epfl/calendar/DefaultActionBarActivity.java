package ch.epfl.calendar;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.display.AddBlocksActivity;
import ch.epfl.calendar.display.AddEventActivity;
import ch.epfl.calendar.display.AppEngineDownloadInterface;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.DatabaseUploadInterface;
import ch.epfl.calendar.utils.AuthenticationUtils;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * @author fouchepi
 * 
 */
public abstract class DefaultActionBarActivity extends Activity implements
        CalendarClientDownloadInterface, AppEngineDownloadInterface,
        DatabaseUploadInterface {

    private Activity mThisActivity;
    private DBQuester mDB;
    private UpdateDataFromDBInterface mUdpateData;
    private AuthenticationUtils mAuthUtils;
    private int mNbOfAsyncTaskDB = 0;
    private ProgressDialog mDialog;
    public static final int AUTH_ACTIVITY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_action_bar);
        mThisActivity = this;
        mDB = new DBQuester();
        mAuthUtils = new AuthenticationUtils();
        App.setActionBar(this);
        defaultActionBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
            populateCalendarFromISA();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_courses_list:
                switchToCoursesList();
                return true;
            case R.id.add_event_block:
                switchToAddBlockActivity();
                return true;
            case R.id.action_settings:
                switchToCreditsActivity();
                return true;
            case R.id.add_event:
                switchToAddEventsActivity();
                return true;
            case R.id.action_update_activity:
                populateCalendarFromISA();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void defaultActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
    }

    private void switchToCoursesList() {
        Intent coursesListActivityIntent = new Intent(this,
                CoursesListActivity.class);
        startActivity(coursesListActivityIntent);
    }

    private void switchToAddBlockActivity() {
        Intent blockActivityIntent = new Intent(this, AddBlocksActivity.class);
        startActivity(blockActivityIntent);
    }

    private void switchToAddEventsActivity() {
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    public void switchToEditActivity(Event event) {
        Intent editActivityIntent = new Intent(this, AddEventActivity.class);
        editActivityIntent.putExtra("Id", event.getId());
        startActivity(editActivityIntent);
    }

    private void switchToCreditsActivity() {
        Intent i = new Intent(mThisActivity, CreditsActivity.class);
        startActivity(i);
    }

    public void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivtyIntent = new Intent(mThisActivity,
                AuthenticationActivity.class);
        mThisActivity.startActivityForResult(
                displayAuthenticationActivtyIntent, AUTH_ACTIVITY_CODE);
    }

    public void populateCalendarFromISA() {
        if (!mAuthUtils.isAuthenticated(mThisActivity)) {
            switchToAuthenticationActivity();
        } else {
            CalendarClientInterface cal = new CalendarClient(mThisActivity,
                    this);
            cal.getISAInformations();
        }
    }

    public void completeCalendarFromAppEngine(List<Course> coursesList) {
        ConstructListCourse constructCourse = ConstructListCourse
                .getInstance(this);
        constructCourse.completeCourse(coursesList, this);
    }

    public void logout() {
        TequilaAuthenticationAPI.getInstance().clearStoredData(mThisActivity);
        switchToAuthenticationActivity();
    }

    @Override
    public void callbackDownload(boolean success, List<Course> courses) {
        if (success) {
            completeCalendarFromAppEngine(courses);
        } else {
            this.logout();
        }
    }

    @Override
    public void callbackAppEngine(List<Course> mCourses) {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(this.getString(R.string.be_patient));
        mDialog.setMessage(this.getString(R.string.saving_db));
        mDialog.setCancelable(false);
        mDialog.show();

        mDB.storeCourses(mCourses);
    }

    public UpdateDataFromDBInterface getUdpateData() {
        return mUdpateData;
    }

    public void setUdpateData(UpdateDataFromDBInterface udpateData) {
        this.mUdpateData = udpateData;
        App.setActionBar(this);
    }
    
    public synchronized void addTask() {
        mNbOfAsyncTaskDB = mNbOfAsyncTaskDB + 1;
    }

    public synchronized void asyncTaskStoreFinished() {
        mNbOfAsyncTaskDB = mNbOfAsyncTaskDB - 1;
        if (mNbOfAsyncTaskDB <= 0) {
            DBQuester.close();
            mUdpateData.updateData();
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

    }

    @Override
    public void callbackDBUpload() {
        mUdpateData.updateData();
    }

}
