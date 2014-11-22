package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import ch.epfl.calendar.R;

/**
 * @author LoomisLoud
 *
 */
public class AddEventBlockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event_block);
		
		Intent mIntent = getIntent();
		String mCourseName = "";
	    if (mIntent.hasExtra("courseName")) {
	    	mCourseName = mIntent.getStringExtra("courseName");
	    }
		
		TextView mAskDay = (TextView) findViewById(R.id.ask_block_day);
		mAskDay.setText(getString(R.string.choose_day_block) + " " + mCourseName + ": ");
		
		Spinner mSpinner = (Spinner) findViewById(R.id.spinner_week_days);
		ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.week_days, android.R.layout.simple_spinner_item);
		mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mSpinnerAdapter);
	}
	
	public void finishActivity(View v) {
		transferAndStoreData();
		finish();
	}
}
