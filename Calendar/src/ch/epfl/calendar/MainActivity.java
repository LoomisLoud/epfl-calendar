package ch.epfl.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.utils.GlobalPreferences;

/**
 *
 * @author lweingart
 *
 */
public class MainActivity extends Activity {

	private static final int REQUEST_CODE = 1;

	private Button btnLogin;
	private Activity mThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThisActivity = this;
        this.btnLogin = (Button) this.findViewById(R.id.btnLogin);

        this.switchLoginLogout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle button activation based on whether the user is authenticated or not.
     */
    private void switchLoginLogout() {
        Boolean isAuthenticated = GlobalPreferences.isAuthenticated(this);
        if (isAuthenticated) {
            btnLogin.setText(R.string.logout);
            btnLogin.setOnClickListener(new LogoutListener());
        } else {
            btnLogin.setText(R.string.login);
            btnLogin.setOnClickListener(new LoginListener());
        }
    }

    /**
     *
     * @author lweingart
     *
     */
    private class LogoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TequilaAuthenticationAPI.getInstance().clearSessionID(MainActivity.this.mThisActivity);

            MainActivity.this.switchLoginLogout();
        }
    }

    /**
     *
     * @author lweingart
     *
     */
    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity.this.startAuthenticationActivity();
        }
    }

    private void startAuthenticationActivity() {
        Intent displayAuthenticationActivityIntent = new Intent(this,
                AuthenticationActivity.class);
        this.startActivityForResult(displayAuthenticationActivityIntent,
                MainActivity.REQUEST_CODE);
    }

}
