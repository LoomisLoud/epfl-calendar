package ch.epfl.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
        
        // get the input string
        String courseName =
                startingIntent.getStringExtra(CoursesListActivity.class.getName());
        
        // get the TextView and update it
        TextView textView = (TextView) findViewById(R.id.courseName);
        textView.setText(courseName);
	}
}