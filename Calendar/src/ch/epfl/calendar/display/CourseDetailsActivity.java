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

        String[] course =  startingIntent.getStringArrayExtra("course");
        String courseName = course[0];
        String courseProfessor = course[1];
        String courseCredits = course[2];

        // get the TextView and update it
        TextView textView = (TextView) findViewById(R.id.courseName);
        textView.setText(courseName);

        textView = (TextView) findViewById(R.id.courseProfessor);
        textView.setText(courseProfessor);

        textView = (TextView) findViewById(R.id.courseCredits);
        textView.setText(courseCredits);
    }
}