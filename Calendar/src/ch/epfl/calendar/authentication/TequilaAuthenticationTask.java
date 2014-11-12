package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.R;
import ch.epfl.calendar.utils.GlobalPreferences;
import ch.epfl.calendar.utils.HttpUtils;
import ch.epfl.calendar.utils.InputStreamUtils;

/**
 * The main class that connects with the Tequila server and authenticates user
 * credentials
 *
 * @author lweingart
 *
 */
public class TequilaAuthenticationTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "AuthenticationTas Class::";

    private Context mContext = null;
    private TequilaAuthenticationListener mListener = null;
    private boolean mExceptionOccured = false;
    private HttpContext mLocalContext = null;
    private HttpResponse mRespGetTimetable = null;
    private TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();
    private AbstractHttpClient client = HttpClientFactory.getInstance();
    private GlobalPreferences globalPrefs = GlobalPreferences.getInstance();
    private ProgressDialog mDialog;
    private String mSessionID;
    private String mUsername;
    private String mPassword;
    private String mCurrentToken;

    private static final String PASSWORD = "password";
    private static final String REQUEST_KEY = "requestkey";
    private static final String KEY = "key";
    private static final String USERNAME = "username";
    private static final String SESSIONID = "JSESSIONID";
    private static final String TEQUILA_KEY = "tequila_key";
    private static final String TEQUILA_USER = "tequila_user";
    private static final int TIMEOUT_AUTHENTICATION = 10;

    /**
     * @author lweingart
     *
     */
    public interface TequilaAuthenticationListener {
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
        mDialog = new ProgressDialog(mContext);
        mDialog.setTitle(mContext.getString(R.string.be_patient));
        mDialog.setMessage(mContext.getString(R.string.authenticating));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result;
        try {
            mLocalContext = new BasicHttpContext();
            int httpCode = 0;
            boolean firstTry = true;
            String tokenList = "";

            Log.d(TAG, "AUTHENTICATED : "+GlobalPreferences.isAuthenticated(mContext));
            if (GlobalPreferences.isAuthenticated(mContext)) {
                mSessionID = TequilaAuthenticationAPI.getInstance().getSessionID(mContext);
                Log.i(TAG, "SESSION ID : " + mSessionID);
                //Try to access to ISA to get a token
                httpCode = getAccessToIsa(mSessionID, null);
                firstTry = false;
            } else {
                httpCode = getAccessToIsa(null, null);
            }

            if (httpCode == TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE
                    || httpCode == TequilaAuthenticationAPI.STATUS_CODE_OK) {

                int timeoutAuthentication = TIMEOUT_AUTHENTICATION;

                while (httpCode != TequilaAuthenticationAPI.STATUS_CODE_OK && timeoutAuthentication > 0) {
                    try {
                        mCurrentToken = HttpUtils.getTokenFromHeader(mRespGetTimetable.getFirstHeader("Location"));
                    } catch (TequilaAuthenticationException e) {
                        throw new ClientProtocolException(e);
                    }
                    
                    timeoutAuthentication--;
                    
                    //Authentication on Tequila needed the token + to know if
                    //it is the first authentication (to know if use username+pwd)
                    authenticateOnTequila(mCurrentToken, firstTry);
                    if (firstTry) {
                        firstTry = false;
                        tokenList = mCurrentToken;
                    } else {
                        if (!tokenList.equals("")) {
                            tokenList = tokenList + "&" + KEY + "=" + mCurrentToken;
                        } else {
                            tokenList = mCurrentToken;
                        }
                    }
                    //Try to get the page on Isa
                    httpCode = getAccessToIsa(mSessionID, tokenList);
                    mSessionID = globalPrefs.getSessionIDCookie().getValue();
                }
            } else {
                throw new ClientProtocolException("Wrong Http Code");
            }
            
            result = InputStreamUtils.readInputStream(mRespGetTimetable.getEntity().getContent());

        } catch (ClientProtocolException e) {
            mExceptionOccured = true;
            Log.e("AuthTask::ClientProtocolException", e.getMessage());
            return mContext.getString(R.string.error_http_protocol);
        } catch (IOException e) {
            mExceptionOccured = true;
            Log.e("AuthTask::IOException", e.getMessage());
            return mContext.getString(R.string.error_io);
        } catch (IllegalStateException e) {
            mExceptionOccured = true;
            Log.e("AuthTask::IllegalStateException", e.getMessage());
            return mContext.getString(R.string.error_illegal_state);
        } catch (TequilaAuthenticationException e) {
            mExceptionOccured = true;
            Log.e("AuthTask::TequilaAuthenticationException", e.getMessage());
            return mContext.getString(R.string.error_wrong_credentials);
        }

        if (!this.isCancelled()) {
            return result;
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
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mListener.onError(mContext.getString(R.string.error_authentication_cancel));
    }

    @Override
    protected void onPostExecute(String result) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mExceptionOccured) {
            // notify success listener
            mListener.onError("Authentication : " + result);
        } else {
            // notify error listener
            mListener.onSuccess(mSessionID);
        }
    }


    /**
     * This function takes a token a authenticate it on Tequila
     * @param token
     * @param firstTry - If it's the first try, we have to use the username and the passwd
     * @throws ClientProtocolException
     * @throws IOException
     * @throws TequilaAuthenticationException 
     */
    private void authenticateOnTequila(String token, boolean firstTry) throws IOException, TequilaAuthenticationException {
        Log.i("INFO : ", "Authentication to Tequila");

        HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
        List<NameValuePair> postBody = new ArrayList<NameValuePair>();
        postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
        if (firstTry && mUsername != null && mPassword != null) {
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
        } else {
            //We set the cookies for Tequila authentication
            client.setCookieStore(new BasicCookieStore());
            client.getCookieStore().addCookie(globalPrefs.getTequilaKeyCookie());
            client.getCookieStore().addCookie(globalPrefs.getTequilaUsernameCookie());

        }
        authReq.setEntity(new UrlEncodedFormEntity(postBody));
        try {
            client.execute(authReq,
                    new TequilaResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
        } catch (ClientProtocolException e) {
            throw new TequilaAuthenticationException(e);
        }


        //We get the cookies from Tequila
        if (firstTry) {
            globalPrefs.setTequilaUsernameCookie(HttpUtils.getCookie(client, TEQUILA_USER));
            globalPrefs.setTequilaKeyCookie(HttpUtils.getCookie(client, TEQUILA_KEY));
        }
    }

    /**
     * @param sessionID
     * @param tokenList
     * @return
     * @throws IllegalStateException 
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int getAccessToIsa(String sessionID, String tokenList) throws IllegalStateException, IOException {
        HttpGet getTimetable = null;
        Log.i("INFO : ", "Try getting access to ISA Services");
        if (tokenList == null) {
            Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL());
            getTimetable = new HttpGet(tequilaApi.getIsAcademiaLoginURL());
        } else {
            Log.i("INFO : ", "Address : " + tequilaApi.getIsAcademiaLoginURL()+"?"+KEY+"="+tokenList);
            getTimetable = new HttpGet(tequilaApi.getIsAcademiaLoginURL()+"?"+KEY+"="+tokenList);
        }

        if (sessionID != null) {
            getTimetable.addHeader("Set-Cookie", SESSIONID + "=" +sessionID);
            client.setCookieStore(new BasicCookieStore());
            client.getCookieStore().addCookie(globalPrefs.getSessionIDCookie());
            if (mRespGetTimetable != null) {
                mRespGetTimetable.getEntity().getContent().close();
            }
        }

        mRespGetTimetable = client
                .execute(getTimetable, mLocalContext);
        Log.i("INFO : ", "Http code received when trying access to ISA Service : "
                + mRespGetTimetable.getStatusLine().getStatusCode());
        globalPrefs.setSessionIDCookie(HttpUtils.getCookie(client, SESSIONID));

        return mRespGetTimetable.getStatusLine().getStatusCode();
    }
}
