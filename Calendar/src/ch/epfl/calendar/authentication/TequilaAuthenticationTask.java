package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

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

            Log.i("INFO : ", "STEP 1");
            // step 1 - get the authentication token
            HttpGet tokenReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            //ResponseHandler<String> handler = new BasicResponseHandler();
            HttpContext localContext = new BasicHttpContext();
            HttpResponse resp = HttpClientFactory.getInstance().execute(tokenReq, localContext);
            for (org.apache.http.Header h : resp.getAllHeaders()) {
            	System.out.println(h.toString());
            }

            Header location = resp.getFirstHeader("Location");
            resp.getEntity().getContent().close();
            /*HttpUriRequest currentReq = (HttpUriRequest) localContext.getAttribute(
                    ExecutionContext.HTTP_REQUEST);*/
            if (location != null) {
            	System.out.println(location.getValue());
            }

            //HttpHost currentHost = (HttpHost)  localContext.getAttribute(
                    //ExecutionContext.HTTP_TARGET_HOST);
            //System.out.println(currentReq.getURI().getRawQuery().replace("requestkey=", ""));
            //System.out.println(currentHost.toURI());
            //System.out.println((currentReq.getURI().isAbsolute()) ?
            //  currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI()));

            //Log.d("Token response = ", tokenResponse);

            //JSONObject tokenJson = new JSONObject(tokenResponse);
            String tokenHeader = location.getValue();
            int i = tokenHeader.indexOf("=");
            String token = tokenHeader.substring(i+1);
            System.out.println("token = "+token);
            //currentReq.getURI().getRawQuery().replace("requestkey=", "");

            //HttpClientFactory.setNoFollow();
            //Log.d("Step 1 - AuthenticationTask", tokenResponse);

            Log.i("INFO : ", "STEP 2");
            // step 2 - authenticate the user credentials
            //HttpClientFactory.setFollow();
            HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            List<NameValuePair> postBody = new ArrayList<NameValuePair>();
            postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            String authResponse = HttpClientFactory.getInstance()
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK));

            Log.d("Step 2 - AuthenticationTask", authResponse);

            Log.i("INFO : ", "STEP 3");
//
//            // step 3 - send the token in order to receive the session_id
//            HttpPost sessionReq = new HttpPost(tequilaApi.getIsAcademiaLoginURL());
//            /*JSONObject sessionReqJson = new JSONObject();
//            sessionReqJson.put(TOKEN, tokenJson);
//            StringEntity sessionReqBody = new StringEntity(sessionReqJson.toString());
//            sessionReq.setEntity(sessionReqBody);
//            sessionReq.setHeader(ACCEPT, APPLICATION_JSON);
//            sessionReq.setHeader(CONTENT_TYPE, APPLICATION_JSON);//*/
//            List<NameValuePair> post = new ArrayList<NameValuePair>();
//            post.add(new BasicNameValuePair(REQUEST_KEY, token));
//            authReq.setEntity(new UrlEncodedFormEntity(post));
//            String sessionResponse = HttpClientFactory.getInstance()
//                    .execute(sessionReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK));
//            System.out.println(sessionResponse);
//            JSONObject sessionJson = new JSONObject(sessionResponse);
//
//            Log.d("Step 3 - AuthenticationTask", sessionResponse);
//
//            mSessionID = sessionJson.getString(SESSION);
        } catch (ClientProtocolException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::ClientProtocolException", e);
            return mContext.getString(R.string.error_http_protocol);
        } catch (IOException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::IOException", e);
            return mContext.getString(R.string.error_io);
        }
//        } catch (JSONException e) {
//            exceptionOccured = true;
//            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::JSONException", e);
//            return mContext.getString(R.string.error_parse_response);
//        }

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
