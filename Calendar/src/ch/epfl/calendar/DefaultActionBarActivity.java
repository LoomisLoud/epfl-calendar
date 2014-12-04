package ch.epfl.calendar;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
import ch.epfl.calendar.display.EventDetailActivity;
import ch.epfl.calendar.display.EventListActivity;
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
    private String mCurrentDBName;
    public static final int AUTH_ACTIVITY_CODE = 1;

    protected DBQuester getDBQuester() {
        if (mDB == null) {
            mDB = new DBQuester();
        } else if (!App.getDBHelper().getDatabaseName().equals(mCurrentDBName)) {
            DBQuester.close();
            mDB = new DBQuester();
            mCurrentDBName = App.getDBHelper().getDatabaseName(); 
        }
        return mDB;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_action_bar);
        mThisActivity = this;
        mAuthUtils = new AuthenticationUtils();
        App.setActionBar(this);
        defaultActionBar();
        mCurrentDBName = App.getDBHelper().getDatabaseName();
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
            case R.id.action_event_list:
                switchToListEvent();
                return true;
            case R.id.action_logout:
                logout(true);
                return true;
            case R.id.action_calendar:
                switchToCalendar();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void defaultActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
    }

    private void switchToListEvent() {
        Intent i = new Intent(this, EventListActivity.class);
        startActivity(i);
    }

    private void switchToCalendar() {
        Intent goToCalendarIntent = new Intent(this, MainActivity.class);
        startActivity(goToCalendarIntent);
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

    public void switchToAddEventsActivity() {
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    public void switchToEventDetail(String name, String description) {
        Intent eventDetailActivityIntent = new Intent(this,
                EventDetailActivity.class);

        eventDetailActivityIntent.putExtra("description", new String[] {name,
                                                                        description });
        startActivity(eventDetailActivityIntent);
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
            mDialog = new ProgressDialog(this);
            mDialog.setTitle(this.getString(R.string.be_patient));
            mDialog.setMessage(this.getString(R.string.uploading));
            mDialog.setCancelable(false);
            mDialog.show();
            App.setDBHelper(App.DATABASE_NAME + "_" + App.getCurrentUsername());
            System.err.println("IN DEFAULTACTIONBAR, DB NAME : " + App.getDBHelper().getDatabaseName());
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

    public void logout(boolean isLogoutDoneByUser) {
        if (isLogoutDoneByUser) {
            //Activity to delete DB
            createMenuDeleteDB();
        }
    }
    
    private void createMenuDeleteDB() {
        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(this);
        choiceDialog.setTitle("Do you want do delete the database for the user "
                + TequilaAuthenticationAPI.getInstance()
                .getUsername(mThisActivity) + " ?");
        choiceDialog.setItems(R.array.yes_or_no,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Yes
                                getDBQuester().deleteAllTables();
                                mThisActivity.deleteDatabase(App.getDBHelper().getDatabaseName());
                                App.setDBHelper(App.DATABASE_NAME);
                                dialog.cancel();
                                TequilaAuthenticationAPI.getInstance().clearStoredData(mThisActivity);
                                populateCalendarFromISA();
                                break;
                            case 1:
                                // No
                                dialog.cancel();
                                TequilaAuthenticationAPI.getInstance().clearStoredData(mThisActivity);
                                populateCalendarFromISA();
                                break;
                            default:
                                // Cancel
                                dialog.cancel();
                                break;
                        }
                    }
                });
        choiceDialog.create();
        choiceDialog.show();
    }

    @Override
    public void callbackDownload(boolean success, List<Course> courses) {
        if (success) {
            completeCalendarFromAppEngine(courses);
        } else {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            this.logout(false);
        }
    }

    @Override
    public void callbackAppEngine(List<Course> mCourses) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(this.getString(R.string.be_patient));
        mDialog.setMessage(this.getString(R.string.saving_db));
        mDialog.setCancelable(false);
        mDialog.show();
        
        for (Course course : mCourses) {
            //FIXME : TO DELETE
            System.err.println("NOMBRE DE PERIODES: " + course.getPeriods().size());
        }
        
        getDBQuester().storeCourses(mCourses);
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

    public synchronized int getNbOfAsyncTaskDB() {
        return mNbOfAsyncTaskDB;
    }

    @Override
    public void callbackDBUpload() {
        mUdpateData.updateData();
    }

    public AuthenticationUtils getAuthUtils() {
        return mAuthUtils;
    }

    public void setAuthUtils(AuthenticationUtils authUtils) {
        this.mAuthUtils = authUtils;
    }

    public Activity getThisActivity() {
        return mThisActivity;
    }

}
