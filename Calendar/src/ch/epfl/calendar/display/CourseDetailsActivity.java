package ch.epfl.calendar.display;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.DatabaseInterface;
import ch.epfl.calendar.data.Course;

/**
 * @author LoomisLoud
 * 
 */

public class CourseDetailsActivity extends Activity {

    private static final float SIZE_OF_TITLE = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

        String courseName = startingIntent.getStringExtra("course");

        Course course = null;
        try {
            course = new DownloadCourseTask().execute(courseName).get();
        } catch (InterruptedException e) {
            Toast.makeText(CourseDetailsActivity.this, 
            		"Could not retrieve data from the course", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(CourseDetailsActivity.this, 
            		"Could not retrieve data, check your internet connection", Toast.LENGTH_LONG).show();
        }

        if (course == null) {
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(courseName + " not found in data base.");
        } else {

            String courseProfessor = course.getTeacher();
            String courseCredits = Integer.toString(course.getCredits());
            String courseDescription = course.getDescription();

            // get the TextView and update it
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(titleToSpannable(courseName));

            textView = (TextView) findViewById(R.id.courseProfessor);
            textView.setText(bodyToSpannable("Professor: " + courseProfessor));

            textView = (TextView) findViewById(R.id.courseCredits);
            textView.setText(bodyToSpannable(courseCredits + " crédits"));
            
            textView = (TextView) findViewById(R.id.courseDescription);
            textView.setText(bodyToSpannable("Description: " + courseDescription));
            textView.setMovementMethod(new ScrollingMovementMethod());

        }
    }

    private SpannableString titleToSpannable(String title) {
        SpannableString spannable = new SpannableString(title);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 0, title.length(), 0);
        spannable.setSpan(new RelativeSizeSpan(SIZE_OF_TITLE), 0,
                title.length(), 0);

        return spannable;
    }

    private SpannableString bodyToSpannable(String body) {
        SpannableString spannable = new SpannableString(body);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 0, body.length(), 0);

        return spannable;
    }

    /**
     * @author Maxime
     * 
     */
    private class DownloadCourseTask extends AsyncTask<String, Void, Course> {

        @Override
        protected Course doInBackground(String... courseName) {
            return retrieveCourse(courseName[0]);
        }

        private Course retrieveCourse(String courseName) {
            DatabaseInterface appEngineClient;
            Course course = null;
            try {
                appEngineClient = new AppEngineClient(
                        "http://versatile-hull-742.appspot.com");
                course = appEngineClient.getCourseByName(courseName);

            } catch (CalendarClientException e) {
            	Toast.makeText(CourseDetailsActivity.this, 
                		"Could not retrieve data, check your internet connection", Toast.LENGTH_LONG).show();
            }
            return course;
        }
    }

}