package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.ConstructCourse;

/**
 * @author Maxime
 * 
 */
public class CoursesListActivity extends Activity {
    private ProgressDialog mDialog;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        mListView = (ListView) findViewById(R.id.coursesListView);

        final ArrayList<Course> coursesList = retrieveCourse();

        final List<Map<String, String>> courseInfoList = retrieveCourseInfo(coursesList);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, courseInfoList,
                android.R.layout.simple_list_item_2,
                new String[] {"Course name", "Professor and Credits" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        mListView.setAdapter(simpleAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                openCourseDetails(courseInfoList.get(position).get(
                        "Course name"));

            }

        });

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

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Charging course details");
        mDialog.show();

    }

    private ArrayList<Course> retrieveCourse() {

        CalendarClient calendarClient = new CalendarClient(this);
        ArrayList<Course> retrieveData = null;

        try {
            retrieveData = new ArrayList<Course>(
                    calendarClient.getISAInformations());
        } catch (CalendarClientException e) {
            e.printStackTrace();
        }
        return retrieveData;
    }

    private ArrayList<Map<String, String>> retrieveCourseInfo(
            List<Course> coursesList) {

        ArrayList<Map<String, String>> coursesName = new ArrayList<Map<String, String>>();

        for (Course cours : coursesList) {
            ConstructCourse constructCourse = ConstructCourse.getInstance();
            constructCourse.completeCourse(cours);
            
            Map<String, String> courseMap = new HashMap<String, String>();
            courseMap.put("Course name", cours.getName());
            courseMap.put("Professor and Credits",
                    "Professor : " + cours.getTeacher() + ", Credits : "
                            + cours.getCredits());
            
            coursesName.add(courseMap);
        }
        return coursesName;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
