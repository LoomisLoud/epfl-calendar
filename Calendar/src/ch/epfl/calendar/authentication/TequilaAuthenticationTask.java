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
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
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
            AbstractHttpClient client = HttpClientFactory.getInstance();
            TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();

            Log.i("INFO : ", "STEP 1");
            // step 1 - get the authentication token
            HttpGet tokenReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            //ResponseHandler<String> handler = new BasicResponseHandler();
            HttpContext localContext = new BasicHttpContext();
            HttpResponse resp = client.execute(tokenReq, localContext);
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
            
            System.out.println("COOKIE :");
            Cookie cookie = null;
            String cookieValue = null;
            List<Cookie> lc = client.getCookieStore().getCookies();
            for (Cookie c : lc) {
                System.out.println(c.toString());
                if (c.getName().equals("JSESSIONID")) {
                    cookie = c;
                    cookieValue = c.getValue();
                }
                /*if(AuthenticationController.tequilaCookieName.equals(c.getName())) {
                    return AuthenticationController.tequilaCookieName + "=" + c.getValue();
                }*/
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
            //HttpClientFactory.setNoFollow();
            HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            List<NameValuePair> postBody = new ArrayList<NameValuePair>();
            postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
            //client.getCookieStore().addCookie(cookie);
            //authReq.setHeader("JSESSIONID", cookieValue);
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            String authResponse = client
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
            cookie = null;
            String cookieValue1 = null;
            lc = client.getCookieStore().getCookies();
            for (Cookie c : lc) {
                System.out.println(c.toString());
                if (c.getName().equals("JSESSIONID")) {
                    cookie = c;
                    cookieValue1 = c.getValue();
                }
            }
            
            Log.d("Step 2 - AuthenticationTask", authResponse);

            Log.i("INFO : ", "STEP 3");
            // step 3 - send the token in order to receive the session_id
            HttpGet sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+token);
            sessionReq.addHeader("SetCookie", "JSESSIONID="+cookieValue);
            
            HttpResponse sessionResponse = client
                    .execute(sessionReq, localContext);
            for (Header h : sessionResponse.getAllHeaders()) {
                System.out.println(h.toString());
            }
            
            
            Log.i("INFO : ", "STEP 4");
            
            location = sessionResponse.getFirstHeader("Location");
            
            String secondTokenHeader = location.getValue();
            i = secondTokenHeader.indexOf("=");
            String secondToken = secondTokenHeader.substring(i+1);
            System.out.println("secondToken = "+secondToken);
            
            HttpGet authReq2 = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+secondToken);
            List<NameValuePair> postBody2 = new ArrayList<NameValuePair>();
            postBody2.add(new BasicNameValuePair("key", secondToken));
            //postBody2.add(new BasicNameValuePair(USERNAME, mUsername));
            //postBody2.add(new BasicNameValuePair(PASSWORD, mPassword));
            //authReq2.setEntity(new UrlEncodedFormEntity(postBody2));
            String authResponse2 = client
                    .execute(authReq2, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
            
            System.out.println(authResponse2);
            
            Log.i("INFO : ", "STEP 5");
            HttpGet timetableReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+token+"&key="+secondToken);
            timetableReq.addHeader("SetCookie", "JSESSIONID="+cookieValue1);
            sessionReq.addHeader("SetCookie", "JSESSIONID="+cookieValue1);
            CookieStore cs = client.getCookieStore();
            cs.addCookie(cookie);
            String reponse = client
                    .execute(timetableReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK));
            /*for (Header h : reponse.getAllHeaders()) {
                System.out.println(h.toString());
            }*/
            System.out.println(reponse);
            
            
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
