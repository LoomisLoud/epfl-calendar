package ch.epfl.calendar.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import ch.epfl.calendar.R;

/**
 * @author LoomisLoud
 *
 */
public class AddEventBlockActivity extends Activity {

	private TimePicker mStartBlockEventHour;
	private TimePicker mEndBlockEventHour;
	private Spinner mSpinnerDays;
	private TextView mAskDay;
	private String mCourseName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event_block);
		
		Intent mIntent = getIntent();
		mCourseName = "";
	    if (mIntent.hasExtra("courseName")) {
	    	mCourseName = mIntent.getStringExtra("courseName");
	    }
		
		mAskDay = (TextView) findViewById(R.id.ask_block_day);
		mSpinnerDays = (Spinner) findViewById(R.id.spinner_week_days);
		mStartBlockEventHour = (TimePicker) findViewById(R.id.from_picker_hour);
		mEndBlockEventHour = (TimePicker) findViewById(R.id.to_picker_hour);
		
		mAskDay.setText(getString(R.string.choose_day_block) + " " + mCourseName + ": ");
		
		ArrayAdapter<CharSequence> mSpinnerDaysAdapter = ArrayAdapter.createFromResource(this,
		        R.array.week_days, android.R.layout.simple_spinner_item);
		mSpinnerDaysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDays.setAdapter(mSpinnerDaysAdapter);
	}
	
	public void finishActivity(View v) {
		transferAndStoreData();
		finish();
	}

	private void transferAndStoreData() {
		// FIXME Implement the storing of the data in the DB 
		Intent i = getIntent();
		
		i.putExtra("day", mAskDay.getText());
		i.putExtra("courseName", mCourseName);
		i.putExtra("nameEvent", "Do " + mCourseName + " homework");

		i.putExtra("startHour", mStartBlockEventHour.getCurrentHour());
		i.putExtra("startMinute", mStartBlockEventHour.getCurrentMinute());

		i.putExtra("endHour", mEndBlockEventHour.getCurrentHour());
		i.putExtra("endMinute", mEndBlockEventHour.getCurrentMinute());

		setResult(RESULT_OK, i);
	}
}
