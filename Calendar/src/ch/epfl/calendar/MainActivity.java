package ch.epfl.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

/**
 *
 * @author lweingart
 *
 */
public class MainActivity extends Activity {

	private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnLogin = (Button) this.findViewById(R.id.btnLogin);
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
            this.btnLogin.setText(R.string.logout);
            this.btnLogin.setOnClickListener(new LogoutListener());
        } else {
            this.btnLogin.setText(R.string.login);
            this.btnLogin.setOnClickListener(new LoginListener());
        }
    }
}
