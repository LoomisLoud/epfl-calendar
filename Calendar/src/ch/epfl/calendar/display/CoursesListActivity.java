package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;

/**
 * @author Maxime
 * 
 */
public class CoursesListActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        mListView = (ListView) findViewById(R.id.coursesListView);

        final ArrayList<Course> coursesList = retrieveCourse();
        final ArrayList<String> coursesNameList = retrieveCourseName(coursesList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, coursesNameList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                openCourseDetails(coursesList.get(position));

            }

        });

    }

    public void openCourseDetails(Course course) {
        Intent courseDetailsActivityIntent = new Intent(this,
                CourseDetailsActivity.class);

        courseDetailsActivityIntent.putExtra("course", course);
        startActivity(courseDetailsActivityIntent);
    }

    public ArrayList<Course> retrieveCourse() {

        CalendarClient calendarClient = new CalendarClient();
        ArrayList<Course> retrieveData = null;

        try {
            retrieveData = new ArrayList<Course>(
                    calendarClient.getISAInformations());
        } catch (CalendarClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return retrieveData;
    }

    public ArrayList<String> retrieveCourseName(List<Course> coursesList) {

        ArrayList<String> coursesName = new ArrayList<String>();

        for (Course cours : coursesList) {
            coursesName.add(cours.getName());
        }
        return coursesName;
    }
}
