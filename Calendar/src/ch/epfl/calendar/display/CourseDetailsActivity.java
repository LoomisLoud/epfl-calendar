package ch.epfl.calendar.display;

import java.util.ArrayList;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
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
        super.setUdpateData(this);
        setContentView(R.layout.activity_course_details);

        courseDetailsActionBar();

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

        mDB = new DBQuester();

        mCourseName = startingIntent.getStringExtra("course");

        updateData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void courseDetailsActionBar() {
        ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Course Details");
    }
    
    public void switchToEditActivity(Event event) {
        Intent editActivityIntent = new Intent(this, AddEventActivity.class);
        editActivityIntent.putExtra("Id", event.getId());
        startActivity(editActivityIntent);
    }

    private void setTextViewsFromCourse() {
        String courseProfessor = mCourse.getTeacher();
        String courseCredits = Integer.toString(mCourse.getCredits());
        String courseDescription = mCourse.getDescription();
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

        if (!courseDescription.equals("")) {
            textView = (TextView) findViewById(R.id.courseDescription);
            textView.setText(bodyToSpannableConcatAndBold("Description: ",
                    courseDescription));
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
        if (!linkedEvents.isEmpty()) {
            TextView textView2 = (TextView) findViewById(R.id.linkedEvents);
            textView2.setText(bodyToSpannableConcatAndBold("Event related:", ""));
            // find the layout of activity to add view at the end
            RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.courseDetailsLayout);
            ArrayList<TextView> myTextViews = new ArrayList<TextView>();
            //ArrayList mIDEvents = new ArrayList<Integer>();
            int precedantEvents = 0;
            for (int i=0; i<linkedEvents.size(); i++) {
                
                Event event = linkedEvents.get(i);
                
                TextView eventTextView = new TextView(this);

                // set some properties
                eventTextView.setText(event.toDisplay());
                eventTextView.setId(event.getId());
                OnClickListener onClickListenerEvent = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Event event = mDB.getEvent(v.getId());
                        switchToEditActivity(event);
                    }
                    
                };
                eventTextView.setOnClickListener(onClickListenerEvent);

                

                if (i==0) {
                  // create layout rule to set textview below event title
                    RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lparams.addRule(RelativeLayout.BELOW, R.id.linkedEvents);
                    eventTextView.setLayoutParams(lparams);
                } else {
                    RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //below precedent event view
                    newParams.addRule(RelativeLayout.BELOW, precedantEvents);
                    newParams.setMargins(0, (int) this.getResources().getDimension(R.dimen.activity_top_margin_small),
                            0, 0);
                    eventTextView.setLayoutParams(newParams);
                }

                // add to layout
                relativeLayout.addView(eventTextView);
                //record id of precedant event for relative layout
                precedantEvents = event.getId();
                // save view in array
                myTextViews.add(eventTextView);
            }

            
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
