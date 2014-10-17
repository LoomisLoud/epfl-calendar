package ch.epfl.calendar.authentication;

import ch.epfl.calendar.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * Authentication activity
 * @author lweingart
 *
 */
public class AuthenticationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
	}

}
