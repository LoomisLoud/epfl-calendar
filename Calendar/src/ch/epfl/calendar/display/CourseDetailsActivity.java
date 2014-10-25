package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;

/**
 * @author LoomisLoud
 * 
 */
public class CourseDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

        Course course =  startingIntent.getParcelableExtra("course");
        String courseName = course.getName();
        String courseProfessor = course.getTeacher();
        String courseCredits = Integer.toString(course.getCredits());

        // get the TextView and update it
        TextView textView = (TextView) findViewById(R.id.courseName);
        textView.setText(courseName);

        textView = (TextView) findViewById(R.id.courseProfessor);
        textView.setText(courseProfessor);

        textView = (TextView) findViewById(R.id.courseCredits);
        textView.setText(courseCredits);
    }
}