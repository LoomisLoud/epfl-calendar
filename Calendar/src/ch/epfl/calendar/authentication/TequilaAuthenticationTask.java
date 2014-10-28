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
            String tokenList = "?";
            HttpResponse respGetTimetable = null;
            AbstractHttpClient client = HttpClientFactory.getInstance();
            TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();

            Log.i("INFO : ", "Get Timetable :");
            HttpGet getTimetable = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            HttpContext localContext = new BasicHttpContext();
            respGetTimetable = client.execute(getTimetable, localContext);
            
            
            if (respGetTimetable.getStatusLine().getStatusCode() == TequilaAuthenticationAPI.STATUS_CODE_OK) {
                System.out.println(InputStreamUtils.readInputStream(respGetTimetable.getEntity().getContent()));
            } else if (respGetTimetable.getStatusLine().getStatusCode()
                    == TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE) {
                
                int code = 0;
                while (code != TequilaAuthenticationAPI.STATUS_CODE_OK) {
                    Header location = respGetTimetable.getFirstHeader("Location");
                    
                    String tokenHeader = location.getValue();
                    int i = tokenHeader.indexOf("=");
                    String token = tokenHeader.substring(i+1);
                    
                    //System.out.println("token = "+token);
                    
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
                    postBody.add(new BasicNameValuePair(USERNAME, mUsername));
                    postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
                    authReq.setEntity(new UrlEncodedFormEntity(postBody));
                    client.execute(authReq, 
                            new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
                    
                    
                    Log.i("INFO : ", "Try getting Timetable with the last token");
                    Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL()+"?key="+token);
                    HttpGet sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+token);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("key", token));
                    respGetTimetable = client.execute(sessionReq, localContext);
                    
                    if (respGetTimetable.getStatusLine().getStatusCode() != TequilaAuthenticationAPI.STATUS_CODE_OK) {
                        if (!tokenList.equals("?")) {
                            Log.i("INFO : ", "Try getting Timetable with all token");
                            Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL()+tokenList);
                            sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+tokenList);
                            sessionReq.addHeader("Set-Cookie", "JSESSIONID="+cookieValue);
                            client.getCookieStore().addCookie(cookie);
                        }
                        
                        respGetTimetable = client
                                .execute(sessionReq, localContext);
                        if (!tokenList.equals("?")) {
                            tokenList = tokenList + "&key=" +token;
                        } else {
                            tokenList = tokenList + "key=" + token;
                        }
                    }
                    code = respGetTimetable.getStatusLine().getStatusCode();
                }
            }
            
            System.out.println(InputStreamUtils.readInputStream(respGetTimetable.getEntity().getContent()));
            
            
            
            
            /*
            Log.i("INFO : ", "STEP 1");
            // step 1 - get the authentication token
            HttpGet tokenReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
            HttpContext localContext = new BasicHttpContext();
            HttpResponse resp = client.execute(tokenReq, localContext);
            
            for (org.apache.http.Header h : resp.getAllHeaders()) {
            	System.out.println(h.toString());
            }

            Header location = resp.getFirstHeader("Location");
            resp.getEntity().getContent().close();
            
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
            }

            String tokenHeader = location.getValue();
            int i = tokenHeader.indexOf("=");
            String token = tokenHeader.substring(i+1);
            System.out.println("token = "+token);


            Log.i("INFO : ", "STEP 2");
            // step 2 - authenticate the user credentials
            HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
            List<NameValuePair> postBody = new ArrayList<NameValuePair>();
            postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
            authReq.setEntity(new UrlEncodedFormEntity(postBody));
            String authResponse = client
                    .execute(authReq, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
            
            
            Log.d("Step 2 - AuthenticationTask", authResponse);

            Log.i("INFO : ", "STEP 3");
            
            HttpGet sessionReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+token);
            sessionReq.addHeader("SetCookie", "JSESSIONID="+cookieValue);
            client.getCookieStore().addCookie(cookie);
            
            HttpResponse sessionResponse = client
                    .execute(sessionReq, localContext);
            for (Header h : sessionResponse.getAllHeaders()) {
                System.out.println(h.toString());
            }
            
            if (sessionResponse.getStatusLine().getStatusCode() == TequilaAuthenticationAPI.STATUS_CODE_OK) {
                System.out.println((new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK)).handleResponse(sessionResponse));
            } else {
                Cookie cookie1 = null;
                String cookieValue1 = null;
                lc = client.getCookieStore().getCookies();
                for (Cookie c : lc) {
                    System.out.println(c.toString());
                    if (c.getName().equals("JSESSIONID")) {
                        cookie1 = c;
                        cookieValue1 = c.getValue();
                    }
                }
                
                Log.i("INFO : ", "STEP 4");
                
                location = sessionResponse.getFirstHeader("Location");
                
                String secondTokenHeader = location.getValue();
                i = secondTokenHeader.indexOf("=");
                String secondToken = secondTokenHeader.substring(i+1);
                System.out.println("secondToken = "+secondToken);
                
                //NOT SURE OF THE ADDRESS ISA OR TEQUILA ?
                /*HttpGet authReq2 = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+secondToken);
                List<NameValuePair> postBody2 = new ArrayList<NameValuePair>();
                postBody2.add(new BasicNameValuePair("key", secondToken));
                String authResponse2 = client
                        .execute(authReq2, new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
                
                System.out.println(authResponse2);*/
         /*       
                Log.i("INFO : ", "STEP 5");
                HttpGet timetableReq = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?key="+token+"&key="+secondToken);
                timetableReq.addHeader("Set-Cookie", "JSESSIONID="+cookieValue1);
                CookieStore cs = client.getCookieStore();
                cs.addCookie(cookie1);
                String reponse = client
                        .execute(timetableReq, new
                                CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_OK));
                System.out.println(reponse);
            }*/
            
            
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
