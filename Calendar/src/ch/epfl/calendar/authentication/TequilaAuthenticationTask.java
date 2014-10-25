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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
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
            /*HttpGet tokenReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            //ResponseHandler<String> handler = new BasicResponseHandler();
            HttpContext localContext = new BasicHttpContext();
            HttpResponse tokenResponse = HttpClientFactory.getInstance().execute(tokenReq, localContext);
            
            HttpUriRequest currentReq = (HttpUriRequest) localContext.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            //HttpHost currentHost = (HttpHost)  localContext.getAttribute(
                    //ExecutionContext.HTTP_TARGET_HOST);
            System.out.println(currentReq.getURI().getRawQuery().replace("requestkey=", ""));
            //System.out.println((currentReq.getURI().isAbsolute()) ?
            //  currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI()));
            
            System.out.println("Token response = " + tokenResponse);
            //Log.d("Token response = ", tokenResponse);
            
            //JSONObject tokenJson = new JSONObject(tokenResponse);
            String tokenJson = currentReq.getURI().getRawQuery().replace("requestkey=", "");
            */

            //Log.d("Step 1 - AuthenticationTask", tokenResponse);

            // step 2 - authenticate the user credentials
            HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            List<NameValuePair> postBody = new ArrayList<NameValuePair>();
            //postBody.add(new BasicNameValuePair(REQUEST_KEY, tokenJson));
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            /*String authResponse = HttpClientFactory.getInstance()
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
            */
            DefaultHttpClient client = new DefaultHttpClient();
            client.execute(authReq).getEntity().getContent().close();
            
            
            String value = "";
            List<Cookie> lc = HttpClientFactory.getInstance().getCookieStore().getCookies();
            for (Cookie c : lc) {
                System.out.println(c.toString());
                if ("tequila_key".equals(c.getName())) {
                    value = "tequila_key" + "=" + c.getValue();
                }
            }
            System.out.println(value);
            
            
            
            authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            postBody = new ArrayList<NameValuePair>();
            postBody.add(new BasicNameValuePair(REQUEST_KEY, value));
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            /*String authResponse = HttpClientFactory.getInstance()
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
            */
            HttpResponse resp = client.execute(authReq);
            Header location = resp.getFirstHeader("Location");
            for (Header h : resp.getAllHeaders()) {
                System.out.println(h.toString());
            }
            if (location == null) {
                System.out.println("ON A UN PROBLEME"); 
            }
            resp.getEntity().getContent().close();
            
            //Log.d("Step 2 - AuthenticationTask", authResponse);

            // step 3 - send the token in order to receive the session_id
            HttpPost sessionReq = new HttpPost(tequilaApi.getIsAcademiaLoginURL());
            JSONObject sessionReqJson = new JSONObject();
            sessionReqJson.put(TOKEN, value);
            StringEntity sessionReqBody = new StringEntity(sessionReqJson.toString());
            sessionReq.setEntity(sessionReqBody);
            sessionReq.setHeader(ACCEPT, APPLICATION_JSON);
            sessionReq.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            HttpResponse sessionResponse = client.execute(sessionReq);
            //JSONObject sessionJson= new JSONObject(sessionResponse.toString());

            //Log.d("Step 3 - AuthenticationTask", sessionResponse);

            for (Header h : sessionResponse.getAllHeaders()) { 
                System.out.println(h.toString());
            }
            mSessionID = sessionResponse.toString();//sessionJson.getString(SESSION);
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
