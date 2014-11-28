package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import ch.epfl.calendar.R;


/**
 * @author Maxime
 *
 */
public class EventDetailActivity extends Activity {
    
    private static final float SIZE_OF_TITLE = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        
        Intent startIntent = getIntent();
        
        String name = startIntent.getStringArrayExtra("description")[0];
        String description = startIntent.getStringArrayExtra("description")[1];
        
        TextView textView = (TextView) findViewById(R.id.eventName);
        textView.setText(titleToSpannable(name));
        
        TextView textView1 = (TextView) findViewById(R.id.eventDescription);
        textView1.setText(bodyToSpannableConcatAndBold("Description: ",
                description));
        
        
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
}
