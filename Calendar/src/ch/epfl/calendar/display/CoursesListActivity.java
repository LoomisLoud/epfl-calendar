package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.ConstructListCourse;

/**
 * @author Maxime
 * 
 */
public class CoursesListActivity extends Activity implements
        CalendarClientDownloadInterface, AppEngineDownloadInterface {

    private ListView mListView;
    private List<Course> mCourses = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        mListView = (ListView) findViewById(R.id.coursesListView);

        retrieveCourse();

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
            courseMap.put("Course name", cours.getName());
            courseMap.put("Course information",
                    "Professor : " + cours.getTeacher() + ", Credits : "
                            + cours.getCredits());

            coursesName.add(courseMap);
        }

        final List<Map<String, String>> courseInfoList = coursesName;

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, courseInfoList,
                android.R.layout.simple_list_item_2,
                new String[] {"Course name", "Course information" },
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

    @Override
    public void callbackDownload(List<Course> courses) {
        this.mCourses = courses;
        retrieveCourseInfo(mCourses);

    }
}
