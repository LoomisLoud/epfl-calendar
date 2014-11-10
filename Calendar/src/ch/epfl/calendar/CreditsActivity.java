package ch.epfl.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreditsActivity extends Activity implements OnClickListener {

	private Button ok_btn;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);

		ok_btn = (Button) findViewById(R.id.ok_btn);
		text = (TextView) findViewById(R.id.credit_txtView_id);

		ok_btn.setOnClickListener(this);
		text.setText("Authors:\nRomain Choukroun\n"
					+"Pierre Fouche\n"
					+"Maxime Coriou\n"
					+"Matthias Leroy\n"
					+"Enea Bell\n"
					+"Marc Schär\n"
					+"Gil Brechbühler\n"
					+"Laurent Weingart\n");
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
