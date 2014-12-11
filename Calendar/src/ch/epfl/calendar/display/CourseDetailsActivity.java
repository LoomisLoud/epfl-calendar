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

/**
 * @author LoomisLoud
 * 
 */

public class CourseDetailsActivity extends DefaultActionBarActivity implements
        UpdateDataFromDBInterface {

    private static final float SIZE_OF_TITLE = 1.5f;

    private String mCourseName;
    private Course mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setUdpateData(this);
        setContentView(R.layout.activity_course_details);

        courseDetailsActionBar();

        // get the intent that started the Activity
        Intent startingIntent = getIntent();

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
        actionBar.setTitle("Course Details");
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
            // textView.setMovementMethod(new ScrollingMovementMethod());
        }

        textView = (TextView) findViewById(R.id.coursePeriod);
        textView.setText(mCourse.toDisplayPeriod());
        if (!linkedEvents.isEmpty()) {
            System.out.println("Linked event num : "
                    + String.valueOf(linkedEvents.size()));
            TextView textView2 = (TextView) findViewById(R.id.linkedEvents);
            textView2
                    .setText(bodyToSpannableConcatAndBold("Event related:", ""));
            // set view as visible
            textView2.setVisibility(View.VISIBLE);
            // find the layout of activity to add view at the end
            RelativeLayout relativeLayout = (RelativeLayout) this
                    .findViewById(R.id.courseDetailsLayout);
            ArrayList<TextView> myTextViews = new ArrayList<TextView>();
            int precedantEvents = 0;
            for (int i = 0; i < linkedEvents.size(); i++) {
                Event event = linkedEvents.get(i);

                // check if event already exist
                TextView eventTextView;
                View view = findViewById(event.getId());
                if (view == null) {
                    eventTextView = new TextView(this);
                } else {
                    // text view already exist, update data and break
                    eventTextView = (TextView) view;
                    eventTextView.setText(titleBoldEventToDisplay(event
                            .toDisplay()));
                    break;
                }

                // set some properties
                eventTextView
                        .setText(titleBoldEventToDisplay(event.toDisplay()));
                eventTextView.setId(event.getId());
                OnClickListener onClickListenerEvent = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Event event = getDBQuester().getEvent(v.getId());
                        switchToEditActivity(event);
                    }

                };
                eventTextView.setOnClickListener(onClickListenerEvent);

                if (i == 0) {
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
                    // below precedent event view
                    newParams.addRule(RelativeLayout.BELOW, precedantEvents);
                    newParams.setMargins(0, (int) this.getResources()
                            .getDimension(R.dimen.activity_top_margin_small),
                            0, 0);
                    eventTextView.setLayoutParams(newParams);
                }

                // add to layout
                relativeLayout.addView(eventTextView);
                // record id of precedant event for relative layout
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
        // span to make string bold
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        // make first characters bold
        sb.setSpan(bss, 0, bodyBold.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    private Spannable titleBoldEventToDisplay(String displayString) {
        String[] splitNameDescrpt = displayString.split(": ");
        String[] splittedName = splitNameDescrpt[1].split("\n");
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableStringBuilder sb = new SpannableStringBuilder(displayString);
        sb.setSpan(bss, splitNameDescrpt[0].length() + 1,
                splitNameDescrpt[0].length() + 2 + splittedName[0].length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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
        mCourse = getDBQuester().getCourse(mCourseName);
        if (mCourse == null) {
            TextView textView = (TextView) findViewById(R.id.courseName);
            textView.setText(mCourseName + " not found in data base.");
        } else {
            setTextViewsFromCourse();
        }
    }

}
