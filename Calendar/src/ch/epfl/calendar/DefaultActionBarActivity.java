package ch.epfl.calendar;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
 * This class is the parent activity of all the other activities.
 * Contains, for exemple, the number of running {@link AsyncTask} at any point of the application
 * @author fouchepi *
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

    /**
     * The code representing the {@link AuthenticationActivity}.
     */
    public static final int AUTH_ACTIVITY_CODE = 1;

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
            case R.id.action_credits:
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

    /**
     * Switches to {@link AddEventActivity}
     */
    public void switchToAddEventsActivity() {
        Intent addEventsActivityIntent = new Intent(this,
                AddEventActivity.class);
        startActivity(addEventsActivityIntent);
    }

    public void switchToAddBlockActivity() {
        Intent blockActivityIntent = new Intent(this, AddBlocksActivity.class);
        startActivity(blockActivityIntent);
    }

    /**
     * Switch to {@link EventDetailActivity}
     * @param name name of the event
     * @param description description of the event
     */
    public void switchToEventDetail(String name, String description) {
        Intent eventDetailActivityIntent = new Intent(this,
                EventDetailActivity.class);

        eventDetailActivityIntent.putExtra("description", new String[] {name, description});
        startActivity(eventDetailActivityIntent);
    }

    /**
     * Switches to {@link AddEventActivity} (will be an activity to edit event if an ID is passed in intent).
     */
    public void switchToEditActivity(Event event) {
        Intent editActivityIntent = new Intent(this, AddEventActivity.class);
        editActivityIntent.putExtra("Id", event.getId());
        startActivity(editActivityIntent);
    }

    /**
     * Switches to {@link AuthenticationActivity}
     */
    public void switchToAuthenticationActivity() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        Intent displayAuthenticationActivtyIntent = new Intent(mThisActivity,
                AuthenticationActivity.class);
        mThisActivity.startActivityForResult(
                displayAuthenticationActivtyIntent, AUTH_ACTIVITY_CODE);
    }

    /**
     * Gets the courses from ISA and populates the database.
     */
    public void populateCalendarFromISA() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        if (!mAuthUtils.isAuthenticated(getApplicationContext())) {
            switchToAuthenticationActivity();
        } else {
            mDialog = new ProgressDialog(this);
            mDialog.setTitle(this.getString(R.string.be_patient));
            mDialog.setMessage(this.getString(R.string.uploading));
            mDialog.setCancelable(false);
            mDialog.show();
            App.setDBHelper(App.DATABASE_NAME + "_" + App.getCurrentUsername());
            CalendarClientInterface cal = new CalendarClient(mThisActivity,
                    this);
            cal.getISAInformations();
        }
    }

    /**
     * Completes the informations of the courses in coursesList with informations from the AppEngine.
     * @param coursesList a List of courses get from ISA.
     */
    public void completeCalendarFromAppEngine(List<Course> coursesList) {
        ConstructListCourse constructCourse = ConstructListCourse
                .getInstance(this);
        constructCourse.completeCourse(coursesList, this);
    }

    @Override
    public void logout(boolean isLogoutDoneByUser) {
        /*
         * if (isLogoutDoneByUser) { Menu to delete DB createMenuDeleteDB(); }
         * else { App.setDBHelper(App.DATABASE_NAME); DBQuester.close();
         * TequilaAuthenticationAPI.getInstance().clearStoredData(
         * getApplicationContext()); populateCalendarFromISA(); }
         */
        App.setDBHelper(App.DATABASE_NAME);
        mCurrentDBName = "none";
        App.setCurrentUsername("noUser");
        DBQuester.close();
        TequilaAuthenticationAPI.getInstance().clearStoredData(
                getApplicationContext());
        populateCalendarFromISA();

    }

    @Override
    public void callbackISAcademia(boolean success, List<Course> courses) {
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

        getDBQuester().storeCourses(mCourses);
    }

    /**
     *
     * @return the {@link UpdateDataFromDBInterface} of the application.
     */
    public UpdateDataFromDBInterface getUdpateData() {
        return mUdpateData;
    }

    /**
     *
     * @param udpateData sets the {@link UpdateDataFromDBInterface} of this class.
     */
    public void setUdpateData(UpdateDataFromDBInterface udpateData) {
        this.mUdpateData = udpateData;
        App.setActionBar(this);
    }

    /**
     * Adds #value AsyncTasks to the counter.
     * @param value
     */
    public synchronized void addTask(int value) {
        mNbOfAsyncTaskDB = mNbOfAsyncTaskDB + value;
    }

    /**
     * Called when an AsyncTask is finished.
     */
    public synchronized void asyncTaskStoreFinished() {
        mNbOfAsyncTaskDB = mNbOfAsyncTaskDB - 1;
        if (mNbOfAsyncTaskDB <= 0) {
            DBQuester.close();
            mUdpateData.updateFromDatabase();
            if (mDialog != null) {
                mDialog.dismiss();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }

    }

    /**
     *
     * @return the number of AsyncTasks actually running.
     */
    public synchronized int getNbOfAsyncTaskDB() {
        return mNbOfAsyncTaskDB;
    }

    @Override
    public void callbackUploadDatabase() {
        mUdpateData.updateFromDatabase();
    }

    /**
     *
     * @return the {@link AuthenticationUtils} object of this class
     */
    public AuthenticationUtils getAuthUtils() {
        return mAuthUtils;
    }

    /**
     * Set the {@link AuthenticationUtils} of this class.
     * @param authUtils
     */
    public void setAuthUtils(AuthenticationUtils authUtils) {
        this.mAuthUtils = authUtils;
    }

    /**
     *
     * @return a reference to this {@link DefaultActionBarActivity}
     */
    public Activity getThisActivity() {
        return mThisActivity;
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
            populateCalendarFromISA();
        }
    }

    protected void activateRotation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
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

    private void switchToCreditsActivity() {
        Intent i = new Intent(mThisActivity, CreditsActivity.class);
        startActivity(i);
    }

//    private void createMenuDeleteDB() {
//        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(this);
//        choiceDialog
//                .setTitle("Do you want do delete the data for the user "
//                        + TequilaAuthenticationAPI.getInstance().getUsername(
//                                mThisActivity.getApplicationContext()) + " ?");
//        choiceDialog.setItems(R.array.yes_or_no, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        // Yes
//                        getDBQuester().deleteAllTables();
//                        getApplicationContext().deleteDatabase(
//                                App.getDBHelper().getDatabaseName());
//                        App.setDBHelper(App.DATABASE_NAME);
//                        mCurrentDBName = "none";
//                        App.setCurrentUsername("noUser");
//                        DBQuester.close();
//                        dialog.cancel();
//                        TequilaAuthenticationAPI.getInstance().clearStoredData(
//                                getApplicationContext());
//                        populateCalendarFromISA();
//                        break;
//                    case 1:
//                        // No
//                        App.setDBHelper(App.DATABASE_NAME);
//                        App.setCurrentUsername("noUser");
//                        mCurrentDBName = "none";
//                        DBQuester.close();
//                        dialog.cancel();
//                        TequilaAuthenticationAPI.getInstance().clearStoredData(
//                                getApplicationContext());
//                        populateCalendarFromISA();
//                        break;
//                    default:
//                        // Cancel
//                        dialog.cancel();
//                        break;
//                }
//            }
//        });
//        choiceDialog.create();
//        choiceDialog.show();
//    }
}
