package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * @author Maxime
 * 
 */
public class CoursesListActivity extends Activity implements
        CalendarClientDownloadInterface, AppEngineDownloadInterface {

    public static final int AUTH_ACTIVITY_CODE = 1;
    private ListView mListView;
    private List<Course> mCourses = new ArrayList<Course>();
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        mListView = (ListView) findViewById(R.id.coursesListView);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // System.out.println("Loading courses in savedInstanceState");
            mCourses = savedInstanceState.getParcelableArrayList("coursesList");
            callbackAppEngine(mCourses);
        } else {
            // Retrieve course for first time
            // System.out.println("Retrieving courses for first time");
            retrieveCourse();
        }

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

    /**
     * Launches the CourseDetailsActivity of a specific courseName
     * 
     * @param courseName
     *            the name of the course from which we want the details
     */
    private void openCourseDetails(String courseName) {

        Intent courseDetailsActivityIntent = new Intent(this,
                CourseDetailsActivity.class);

        courseDetailsActivityIntent.putExtra("course", courseName);
        startActivity(courseDetailsActivityIntent);
    }

    private void retrieveCourse() {
        CalendarClientInterface calendarClient = new CalendarClient(this, this);
        calendarClient.getISAInformations();
    }

    private void retrieveCourseInfo(List<Course> coursesList) {

        ConstructListCourse constructCourse = ConstructListCourse
                .getInstance(this);
        constructCourse.completeCourse(coursesList, this);

    }

    public void callbackAppEngine(List<Course> coursesList) {

        ArrayList<Map<String, String>> coursesName = new ArrayList<Map<String, String>>();

        for (Course cours : coursesList) {
            Map<String, String> courseMap = new HashMap<String, String>();
            int creditImage = getCreditImage(cours);

            Set<String> periods = new HashSet<String>();
            for (Period period : cours.getPeriods()) {
                periods.add(period.toString());
            }

            courseMap.put("credit_image", Integer.toString(creditImage));
            courseMap.put("course", cours.getName());
            courseMap.put("info", "Professor : " + cours.getTeacher()
                    + ", Credits : " + cours.getCredits() + "\n" + periods);

            coursesName.add(courseMap);
        }

        final List<Map<String, String>> courseInfoList = coursesName;

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, courseInfoList,
                R.layout.listview_images,
                new String[] {"credit_image", "course", "info" },
                new int[] {R.id.credit_image, R.id.course, R.id.info });

        mListView.setAdapter(simpleAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                openCourseDetails(courseInfoList.get(position).get("course"));

            }

        });
    }

    /**
     * FIXME : NOT CLEAN TO DO THIS WAY
     */
    private void switchToAuthenticationActivity() {
        Intent displayAuthenticationActivtyIntent = new Intent(this,
                AuthenticationActivity.class);
        this.startActivityForResult(displayAuthenticationActivtyIntent,
                AUTH_ACTIVITY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY_CODE && resultCode == RESULT_OK) {
            this.mCourses = new ArrayList<Course>();
            retrieveCourse();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void callbackDownload(boolean success, List<Course> courses) {
        if (success) {
            this.mCourses = courses;
            retrieveCourseInfo(mCourses);
        } else {
            switchToAuthenticationActivity();
        }

    }

    private int getCreditImage(Course cours) {
        switch (cours.getCredits()) {
            case ONE:
                return R.drawable.un;
            case TWO:
                return R.drawable.deux;
            case THREE:
                return R.drawable.trois;
            case FOUR:
                return R.drawable.quatre;
            case FIVE:
                return R.drawable.cinq;
            case SIX:
                return R.drawable.six;
            case SEVEN:
                return R.drawable.sept;
            case EIGHT:
                return R.drawable.huit;
            case NINE:
                return R.drawable.neuf;
            case TEN:
                return R.drawable.dix;
            case ELEVEN:
                return R.drawable.onze;
            case TWELVE:
                return R.drawable.douze;

            default:
                return R.drawable.vingt;

        }
    }
}
