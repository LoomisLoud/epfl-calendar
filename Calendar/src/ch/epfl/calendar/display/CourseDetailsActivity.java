package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.display.AppEngineTask.AppEngineListener;
import ch.epfl.calendar.utils.HttpUtils;

/**
 * @author LoomisLoud
 * 
 */

public class CourseDetailsActivity extends Activity {

    private static final float SIZE_OF_TITLE = 1.5f;

    private final Activity mThisActivity = this;
    private AppEngineTask mTask;
    private String mCourseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

        mCourseName = startingIntent.getStringExtra("course");

        // Course course = null;

        if (HttpUtils.isNetworkWorking(this.mThisActivity)) {
            // course = new DownloadCourseTask().execute(courseName).get();
            mTask = new AppEngineTask(this, new AppEngineHandler());
            mTask.execute(mCourseName);
        }
    }

    private void callback() {
        Course course = mTask.getCourse();
        if (course == null) {
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(mCourseName + " not found in data base.");
        } else {

            String courseProfessor = course.getTeacher();
            String courseCredits = Integer.toString(course.getCredits());
            String courseDescription = course.getDescription();

            // get the TextView and update it
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(titleToSpannable(course.getName()));

            textView = (TextView) findViewById(R.id.courseProfessor);
            textView.setText(bodyToSpannable("Professor: " + courseProfessor));

            textView = (TextView) findViewById(R.id.courseCredits);
            textView.setText(bodyToSpannable(courseCredits + " crédits"));

            textView = (TextView) findViewById(R.id.courseDescription);
            textView.setText(bodyToSpannable("Description: "
                    + courseDescription));
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
     * 
     * @author Maxime
     * 
     */
    private class AppEngineHandler implements AppEngineListener {

        @Override
        public void onError(Context context, String msg) {

            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess() {
            callback();
        }

    }

}