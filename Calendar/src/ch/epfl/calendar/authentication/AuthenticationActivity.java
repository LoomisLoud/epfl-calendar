package ch.epfl.calendar.authentication;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;

/**
 * Authentication activity
 * 
 * @author lweingart
 * 
 */
public class AuthenticationActivity extends Activity {

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button mBtnLogin;

    private final Activity mThisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_authentication);

        mTxtUsername = (EditText) this.findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) this.findViewById(R.id.txtPassword);
        mBtnLogin = (Button) this.findViewById(R.id.btnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = AuthenticationActivity.this.mTxtUsername
                        .getText().toString();
                String password = AuthenticationActivity.this.mTxtPassword
                        .getText().toString();
                if ((username == null) || username.trim().isEmpty()
                        || (password == null) || password.trim().isEmpty()) {
                    Toast.makeText(
                            AuthenticationActivity.this.getBaseContext(),
                            AuthenticationActivity.this
                                    .getString(R.string.error_empty_credentials),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // start the authorization process
                new TequilaAuthenticationTask(
                        AuthenticationActivity.this.mThisActivity,
                        new TequilaAuthenticationHandler(), username, password)
                        .execute(null, null);
                setResult(RESULT_OK, getIntent());
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
    

    //FIXME POUR ORIENTATION : onPause, onResume, on Stop ?
    @Override
    public void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    /**
     * Handler for the TequilaAuthentication
     * 
     * @author lweingart
     * 
     */
    private class TequilaAuthenticationHandler implements
            TequilaAuthenticationListener {
        @Override
        public void onError(String msg) {
            AuthenticationActivity.this.mTxtPassword.setText("");
            AuthenticationActivity.this.mTxtUsername.setText("");
            Toast.makeText(AuthenticationActivity.this.getBaseContext(), msg,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String sessionID) {
            App.setCurrentUsername(AuthenticationActivity.this.mTxtUsername
                    .getText().toString());
            // store the sessionID in the preferences
            TequilaAuthenticationAPI.getInstance().setSessionID(
                    getApplicationContext(), sessionID);
            TequilaAuthenticationAPI.getInstance().setUsername(
                    getApplicationContext(),
                    AuthenticationActivity.this.mTxtUsername.getText()
                            .toString());
            Toast.makeText(AuthenticationActivity.this.mThisActivity,
                    R.string.authenticated, Toast.LENGTH_SHORT).show();
            AuthenticationActivity.this.mThisActivity.finish();
        }
    }

}
