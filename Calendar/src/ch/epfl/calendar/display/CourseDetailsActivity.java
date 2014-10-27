package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import ch.epfl.calendar.R;
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

        Course course =  startingIntent.getParcelableExtra("course");
        String courseName = course.getName();
        String courseProfessor = course.getTeacher();
        String courseCredits = Integer.toString(course.getCredits());

        // get the TextView and update it
        TextView textView = (TextView) findViewById(R.id.courseName);
        textView.setText(titleToSpannable(courseName));

        textView = (TextView) findViewById(R.id.courseProfessor);
        textView.setText(bodyToSpannable("Professor: " + courseProfessor));

        textView = (TextView) findViewById(R.id.courseCredits);
        textView.setText(bodyToSpannable(courseCredits + " crédits"));
    }


    private SpannableString titleToSpannable(String title) {
        SpannableString spannable = new SpannableString(title);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 0, title.length(), 0);
        spannable.setSpan(new RelativeSizeSpan(SIZE_OF_TITLE), 0, title.length(), 0);

        return spannable;
    }

    private SpannableString bodyToSpannable(String body) {
        SpannableString spannable = new SpannableString(body);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 0, body.length(), 0);

        return spannable;
    }
}