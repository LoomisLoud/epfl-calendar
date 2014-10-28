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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.R;
import ch.epfl.calendar.utils.InputStreamUtils;

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

    private static final String PASSWORD = "password";
    private static final String REQUEST_KEY = "requestkey";
    private static final String KEY = "key";
    private static final String USERNAME = "username";

    /**
     * The interface pf the authentication task
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
            int httpCode = 0;
            String tokenList = "?";
            HttpResponse respGetTimetable = null;
            boolean firstTry = true;
            AbstractHttpClient client = HttpClientFactory.getInstance();
            TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();

            Log.i("INFO : ", "Get Timetable :");
            HttpGet getTimetable = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            HttpContext localContext = new BasicHttpContext();
            respGetTimetable = client.execute(getTimetable, localContext);
            
            if (respGetTimetable.getStatusLine().getStatusCode() == TequilaAuthenticationAPI.STATUS_CODE_OK) {
                httpCode = TequilaAuthenticationAPI.STATUS_CODE_OK;
            } else if (respGetTimetable.getStatusLine().getStatusCode()
                    == TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE) {
                
                while (httpCode != TequilaAuthenticationAPI.STATUS_CODE_OK) {
                    Header location = respGetTimetable.getFirstHeader("Location");
                    
                    String tokenHeader = location.getValue();
                    int i = tokenHeader.indexOf("=");
                    String token = tokenHeader.substring(i+1);
                    
                    Cookie cookie = null;
                    String cookieValue = null;
                    List<Cookie> lc = client.getCookieStore().getCookies();
                    for (Cookie c : lc) {
                        System.out.println(c.toString());
                        if (c.getName().equals("JSESSIONID")) {
                            cookie = c;
                            cookieValue = c.getValue();
                        }
                    }
                    
                    Log.i("INFO : ", "Authentication to Tequila");
                    
                    HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
                    List<NameValuePair> postBody = new ArrayList<NameValuePair>();
                    postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
                    if (firstTry) {
                        postBody.add(new BasicNameValuePair(USERNAME, mUsername));
                        postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
                        firstTry = false;
                    }
                    authReq.setEntity(new UrlEncodedFormEntity(postBody));
                    client.execute(authReq, 
                            new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
                    
                    
                    Log.i("INFO : ", "Try getting Timetable with the last token");
                    Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL()+"?"+KEY+"="+token);
                    HttpGet sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?"+KEY+"="+token);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("key", token));
                    respGetTimetable.getEntity().getContent().close();
                    respGetTimetable = client.execute(sessionReq, localContext);
                    if (respGetTimetable.getStatusLine().getStatusCode() != TequilaAuthenticationAPI.STATUS_CODE_OK) {
                        if (!tokenList.equals("?")) {
                            tokenList = tokenList + "&key=" +token;
                            Log.i("INFO : ", "Try getting Timetable with all token");
                            Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL()+tokenList);
                            sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+tokenList);
                            sessionReq.addHeader("Set-Cookie", "JSESSIONID="+cookieValue);
                            client.getCookieStore().addCookie(cookie);
                            respGetTimetable = client
                                    .execute(sessionReq, localContext);
                        }
                        if (tokenList.equals("?")) {
                            tokenList = tokenList + "key=" + token;
                        }
                    }
                    httpCode = respGetTimetable.getStatusLine().getStatusCode();
                }
            }
            
            String result = InputStreamUtils.readInputStream(respGetTimetable.getEntity().getContent());
            
            System.out.println(result);
            
        } catch (ClientProtocolException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::ClientProtocolException", e);
            return mContext.getString(R.string.error_http_protocol);
        } catch (IOException e) {
            exceptionOccured = true;
            Logger.getAnonymousLogger().log(Level.SEVERE, "AuthTask::IOException", e);
            return mContext.getString(R.string.error_io);
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
