package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.R;

/**
 * The main class that connects with the Tequila server and authenticates user
 * credentials
 *
 * @author lweingart
 *
 */
public class TequilaAuthenticationTask extends AsyncTask<Void, Void, String> {

	private Context mContext = null;
	private String mSessionID;
	private ProgressDialog dialog;
	private TequilaAuthenticationListener mListener = null;
	private boolean exceptionOccured = false;

	private final String mUsername;
	private final String mPassword;

	private static final String ACCEPT = "Accept";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-type";
	private static final String PASSWORD = "password";
	private static final String REQUEST_KEY = "requestkey";
	private static final String SESSION = "session";
	private static final String TOKEN = "token";
	private static final String USERNAME = "username";

	/**
	 *
	 * @author lweingart
	 *
	 */
    public interface TequilaAuthenticationListener{
        void onError(String msg);
        void onSuccess(String sessionID);
    }

	public TequilaAuthenticationTask(Context context,
									 TequilaAuthenticationListener listener,
									 String username,
									 String password) {
		mContext = context;
		mListener = listener;
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

        try {

            TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();

            // step 1 - get the authentication token
            HttpGet tokenReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            ResponseHandler<String> handler = new BasicResponseHandler();
            String tokenResponse = HttpClientFactory.getInstance().execute(tokenReq, handler);
            JSONObject tokenJson= new JSONObject(tokenResponse);

            Log.d("Step 1 - AuthenticationTask", tokenResponse);

            // step 2 - authenticate the user credentials
            HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            List<NameValuePair> postBody = new ArrayList<NameValuePair>();
            postBody.add(new BasicNameValuePair(REQUEST_KEY, tokenJson.getString(TOKEN)));
            postBody.add(new BasicNameValuePair(USERNAME, this.mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, this.mPassword));
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            String authResponse = HttpClientFactory.getInstance()
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));

            Log.d("Step 2 - AuthenticationTask", authResponse);

            // step 3 - send the token in order to receive the session_id
            HttpPost sessionReq = new HttpPost(tequilaApi.getIsAcademiaLoginURL());
            JSONObject sessionReqJson = new JSONObject();
            sessionReqJson.put(TOKEN, tokenJson.getString(TOKEN));
            StringEntity sessionReqBody = new StringEntity(sessionReqJson.toString());
            sessionReq.setEntity(sessionReqBody);
            sessionReq.setHeader(ACCEPT, APPLICATION_JSON);
            sessionReq.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            String sessionResponse = HttpClientFactory.getInstance()
                    .execute(sessionReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK));
            JSONObject sessionJson= new JSONObject(sessionResponse);

            Log.d("Step 3 - AuthenticationTask", sessionResponse);

            this.mSessionID = sessionJson.getString(SESSION);
        } catch (ClientProtocolException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::ClientProtocolException", e);
            return mContext.getString(R.string.error_http_protocol);
        } catch (IOException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::IOException", e);
            return mContext.getString(R.string.error_io);
        } catch (JSONException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::JSONException", e);
            return mContext.getString(R.string.error_parse_response);
        }

        if (!this.isCancelled()) {
            return mContext.getString(R.string.login_success);
        } else {
            // onCancelled() will be called instead of onPostExecute()
            return null;
        }
	}

    @Override
    protected void onCancelled(String result) {
        onCancelled();
    }
    @Override
    protected void onCancelled() {
        if (dialog != null) {
            dialog.dismiss();
        }
        mListener.onError(mContext.getString(R.string.error_authentication_cancel));
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (exceptionOccured) {
            // notify success listener
            mListener.onError(result);
        } else {
            // notify error listener
            mListener.onSuccess(mSessionID);
        }
    }

}
