package ch.epfl.calendar.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ch.epfl.calendar.R;

/**
 * The main class that connects with the Tequila server and authenticates
 * user credentials
 * @author lweingart
 *
 */
public class TequilaAuthenticationTask extends AsyncTask<Void, Void, String> {


    private Context mContext = null;
    private String mSessionID;

    private ProgressDialog dialog;
    private final String mUsername;
    private final String mPassword;

    private final boolean exceptionOccured = false;

    private static final String ACCEPT = "Accept";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String PASSWORD = "password";
    private static final String REQUEST_KEY = "requestkey";
    private static final String SESSION = "session";
    private static final String TOKEN = "token";
    private static final String USERNAME = "username";

    public TequilaAuthenticationTask(Context context, String username, String password) {
        mContext = context;
        mUsername = username;
        mPassword = password;
    }

    @Override
    protected void onPreExecute() {
    	dialog = new ProgressDialog(mContext);
        dialog.setTitle(mContext.getString(R.string.be_patient));
        dialog.setMessage(mContext.getString(R.string.authenticating));
        dialog.setCancelable(false);
        dialog.show();
    }

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
