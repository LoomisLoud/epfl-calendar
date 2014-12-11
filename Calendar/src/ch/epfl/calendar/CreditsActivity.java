package ch.epfl.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity show the credits of the APP (who are its developpers).
 * @author lweingart
 *
 */
public class CreditsActivity extends Activity implements OnClickListener {

	public static final String AUTHORS = "Authors:\nRomain Choukroun\n"
										+"Pierre Fouche\n"
										+"Maxime Coriou\n"
										+"Matthias Leroy\n"
										+"Enea Bell\n"
										+"Marc Schär\n"
										+"Gil Brechbühler\n"
										+"Laurent Weingart\n";

	private Button mOkBtn;
	private TextView mText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);

		mOkBtn = (Button) findViewById(R.id.ok_btn);
		mText = (TextView) findViewById(R.id.credit_txtView_id);

		mOkBtn.setOnClickListener(this);
		mText.setText(AUTHORS);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ok_btn:
				this.finish();
				break;
			default:
				break;
		}
	}

}