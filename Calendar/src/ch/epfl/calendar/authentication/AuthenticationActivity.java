package ch.epfl.calendar.authentication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ch.epfl.calendar.R;

/**
 * Authentication activity
 * @author lweingart
 *
 */
public class AuthenticationActivity extends Activity {

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button mBtnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

        mTxtUsername = (EditText) this.findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) this.findViewById(R.id.txtPassword);
        mBtnLogin = (Button) this.findViewById(R.id.btnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = AuthenticationActivity.this.mTxtUsername.getText().toString();
                String password = AuthenticationActivity.this.mTxtPassword.getText().toString();
                if ((username == null) || username.trim().isEmpty()
                        || (password == null) || password.trim().isEmpty()) {
                    Toast.makeText(AuthenticationActivity.this.getBaseContext(),
                            AuthenticationActivity.this.getString(R.string.error_empty_credentials),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // start the authorization process

            }
        });
	}

}
