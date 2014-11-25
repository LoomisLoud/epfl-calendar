package ch.epfl.calendar.display;


import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import ch.epfl.calendar.DefaultActionBarActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.UpdateDataFromDBInterface;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.persistence.DBQuester;


/**
 * @author LoomisLoud
 * 
 */

public class CourseDetailsActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    private static final float SIZE_OF_TITLE = 1.5f;

    private String mCourseName;
    private Course mCourse;
    private DBQuester mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courseDetailsActionBar();

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

        mDB = new DBQuester();

        mCourseName = startingIntent.getStringExtra("course");

        updateData();
    }

    private void courseDetailsActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Course Details");
    }

    private void setTextViewsFromCourse() {
        String courseProfessor = mCourse.getTeacher();
        String courseCredits = Integer.toString(mCourse.getCredits());
        String courseDescription = mCourse.getDescription();
        String linkedEventsToString = "";
        List<Event> linkedEvents = mCourse.getEvents();
        
        

        // get the TextView and update it
        TextView textView = (TextView) findViewById(R.id.courseName);
        textView.setText(titleToSpannable(mCourse.getName()));

        textView = (TextView) findViewById(R.id.courseProfessor);
        textView.setText(bodyToSpannableConcatAndBold("Professor: ",
                courseProfessor));

        textView = (TextView) findViewById(R.id.courseCredits);
        textView.setText(bodyToSpannableConcatAndBold("Crédits: ",
                courseCredits));

        textView = (TextView) findViewById(R.id.courseDescription);
        textView.setText(bodyToSpannableConcatAndBold("Description: ",
                courseDescription));
        textView.setMovementMethod(new ScrollingMovementMethod());
        
        for (Event event: linkedEvents) {
            linkedEventsToString += event.toString();
        }
        
        
        
        TextView textView2 = (TextView) findViewById(R.id.linkedEvents);
        textView2.setText(linkedEventsToString);
        
    }

    private SpannableString titleToSpannable(String title) {
        SpannableString spannable = new SpannableString(title);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 0, title.length(), 0);
        spannable.setSpan(new RelativeSizeSpan(SIZE_OF_TITLE), 0,
                title.length(), 0);

        return spannable;
    }

    private SpannableStringBuilder bodyToSpannableConcatAndBold(
            String bodyBold, String body) {
        SpannableStringBuilder sb = new SpannableStringBuilder(bodyBold + body);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span
                                                                       // to
                                                                       // make
                                                                       // text
                                                                       // bold
        sb.setSpan(bss, 0, bodyBold.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first characters
                                                     // Bold
        return sb;
    }

    // WARNING decomment test in testSuite if you use this method again
    /*
     * private SpannableString bodyToSpannable(String body) { SpannableString
     * spannable = new SpannableString(body); StyleSpan boldSpan = new
     * StyleSpan(Typeface.BOLD); spannable.setSpan(boldSpan, 0, body.length(),
     * 0); return spannable; }
     */

    @Override
    public void updateData() {
        mCourse = mDB.getCourse(mCourseName);
        if (mCourse == null) {
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(mCourseName + " not found in data base.");
        } else {
            setTextViewsFromCourse();
        }
    }

}
