package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.utils.AuthenticationUtils;
import ch.epfl.calendar.utils.GlobalPreferences;
import ch.epfl.calendar.utils.HttpUtils;
import ch.epfl.calendar.utils.InputStreamUtils;
import ch.epfl.calendar.utils.NetworkException;

/**
 * The main class of authentication that connects with the Tequila server and authenticates user
 * credentials
 * 
 * @author lweingart
 * 
 */
public class TequilaAuthenticationTask extends AsyncTask<Void, Void, String> {

    /**
     * Used for Logs, the identifier of this class to spot it easily.
     */
    public static final String TAG = "AuthenticationTas Class::";

    private Context mContext = null;
    private String mResult = null;
    private TequilaAuthenticationListener mListener = null;
    private boolean mExceptionOccured = false;
    private HttpContext mLocalContext = null;
    private HttpResponse mRespGetTimetable = null;
    private TequilaAuthenticationAPI mTequilaApi = null;
    private AbstractHttpClient mClient = null;
    private GlobalPreferences mGlobalPrefs = null;
    private ProgressDialog mDialog;
    private HttpUtils mHttpUtils = null;
    private String mSessionID;
    private String mUsername;
    private String mPassword;
    private String mCurrentToken;
    private AuthenticationUtils mAuthUtils;

    private static final String PASSWORD = "password";
    private static final String REQUEST_KEY = "requestkey";
    private static final String KEY = "key";
    private static final String USERNAME = "username";
    private static final String SESSIONID = "JSESSIONID";
    private static final String TEQUILA_KEY = "tequila_key";
    private static final String TEQUILA_USER = "tequila_user";
    private static final String DOMAIN_ISA = "isa.epfl.ch";
    private static final String DOMAIN_TEQUILA = "tequila.epfl.ch";
    private static final String PATH_ISA = "/service";
    private static final int TIMEOUT_AUTHENTICATION = 10;
    private static final String TEQUILA_ENCODING = "ISO-8859-1";

    /**
     * An interface for a listener called when the authentication ends (in sucess or in failure).
     * @author lweingart
     * 
     */
    public interface TequilaAuthenticationListener {
        void onError(String msg);

        void onSuccess(String sessionID);
    }

    /**
     * The constructor of this class
     * @param context the context of the {@link Activity} creating this object
     * @param listener the listener {@link TequilaAuthenticationListener} implementation to use
     * @param username the username of the user
     * @param password the password of the user
     */
    public TequilaAuthenticationTask(Context context,
            TequilaAuthenticationListener listener, String username,
            String password) {
        mContext = context;
        mListener = listener;
        mUsername = username;
        mPassword = password;

        mClient = HttpClientFactory.getInstance();
        mTequilaApi = TequilaAuthenticationAPI.getInstance();
        mGlobalPrefs = GlobalPreferences.getInstance();
        mHttpUtils = new HttpUtils();
        mAuthUtils = new AuthenticationUtils();
    }

    /**
     * 
     * @return the result of the Authentication.
     */
    public String getResult() {
        return mResult;
    }
    
    /**
     * {@see HttpUtils.isNetworkWorking(Context)}
     * @param context the context of the {@link Activity} calling this method
     * @return true if there is a network connection on the android device, false otherwise.
     */
    public boolean isNetworkWorking(Context context) {
        return HttpUtils.isNetworkWorking(context);
    }

    /**
     * 
     * @return the {@link TequilaAuthenticationAPI} used by this instance.
     */
    public TequilaAuthenticationAPI getTequilaApi() {
        return mTequilaApi;
    }

    /**
     * 
     * @return the {@link AbstractHttpClient} used by this instance.
     */
    public AbstractHttpClient getClient() {
        return mClient;
    }

    /**
     * 
     * @return the {@link GlobalPreferences} instance used by this instance.
     */
    public GlobalPreferences getGlobalPrefs() {
        return mGlobalPrefs;
    }

    /**
     * 
     * @return the {@link AuthenticationUtils} used by this instance.
     */
    public AuthenticationUtils getAuthUtils() {
        return mAuthUtils;
    }

    /**
     * 
     * @return the {@link HttpUtils} used by this instance.
     */
    public HttpUtils getHttpUtils() {
        return mHttpUtils;
    }

    /**
     * 
     * @return the {@link HttpResponse} containing the fetched timetable.
     */
    public HttpResponse getRespGetTimetable() {
        return mRespGetTimetable;
    }

    @Override
    protected void onPreExecute() {
        // In case of mUsername and mPassword are null, that means that
        // there already is a Dialog for fetching Data from CalendarClient
        if (mUsername != null && mPassword != null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setTitle(mContext.getString(R.string.be_patient));
            mDialog.setMessage(mContext.getString(R.string.authenticating));
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String result;
        try {
            if (!this.isNetworkWorking(mContext)) {
                throw new NetworkException(
                        mContext.getString(R.string.network_unreachable));
            }
            mLocalContext = new BasicHttpContext();
            int httpCode = 0;
            boolean firstTry = true;
            String tokenList = "";
            setNewCookieStore();

            Log.d(TAG,
                    "AUTHENTICATED : "
                            + getAuthUtils().isAuthenticated(mContext));
            if (getAuthUtils().isAuthenticated(mContext)) {
                mSessionID = getTequilaApi().getSessionID(mContext);
                mUsername = getTequilaApi().getUsername(mContext);
                String tequilaKey = getTequilaApi().getTequilaKey(mContext);

                BasicClientCookie isaCookie = new BasicClientCookie(SESSIONID,
                        mSessionID);
                isaCookie.setDomain(DOMAIN_ISA);
                isaCookie.setPath(PATH_ISA);

                BasicClientCookie tequilaUsernameCookie = new BasicClientCookie(
                        TEQUILA_USER, mUsername);
                tequilaUsernameCookie.setDomain(DOMAIN_TEQUILA);
                BasicClientCookie tequilaKeyCookie = new BasicClientCookie(
                        TEQUILA_KEY, tequilaKey);
                tequilaKeyCookie.setDomain(DOMAIN_TEQUILA);

                getGlobalPrefs().setSessionIDCookie(isaCookie);
                getGlobalPrefs()
                        .setTequilaUsernameCookie(tequilaUsernameCookie);
                getGlobalPrefs().setTequilaKeyCookie(tequilaKeyCookie);
                Log.i(TAG, "SESSION ID : " + mSessionID);

                // Try to access to ISA to get a token
                httpCode = getAccessToIsa(mSessionID, null);
                firstTry = false;
            } else {
                httpCode = getAccessToIsa(null, null);
                // Should Never happen !
                if (httpCode == TequilaAuthenticationAPI.STATUS_CODE_OK) {
                    mSessionID = "fake";
                }
            }

            int timeoutAuthentication = TIMEOUT_AUTHENTICATION;

            if (httpCode == TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE
                    || httpCode == TequilaAuthenticationAPI.STATUS_CODE_OK) {

                while (httpCode != TequilaAuthenticationAPI.STATUS_CODE_OK
                        && timeoutAuthentication > 0) {
                    if (!this.isNetworkWorking(mContext)) {
                        throw new NetworkException(
                                mContext.getString(R.string.network_unreachable));
                    }
                    mCurrentToken = getHttpUtils().getTokenFromHeader(
                            getRespGetTimetable().getFirstHeader("Location"));
                    timeoutAuthentication--;

                    // Authentication on Tequila needed the token + to know if
                    // it is the first authentication (to know if use
                    // username+pwd)
                    authenticateOnTequila(mCurrentToken, firstTry);
                    if (firstTry) {
                        App.setDBHelper("calendar_db_" + mUsername);
                        firstTry = false;
                        tokenList = mCurrentToken;
                    } else {
                        if (!tokenList.equals("")) {
                            tokenList = tokenList + "&" + KEY + "="
                                    + mCurrentToken;
                        } else {
                            tokenList = mCurrentToken;
                        }
                    }
                    // Try to get the page on Isa
                    httpCode = getAccessToIsa(mSessionID, tokenList);
                    mSessionID = getGlobalPrefs().getSessionIDCookie()
                            .getValue();
                }
            } else {
                if (mRespGetTimetable != null) {
                    mRespGetTimetable.getEntity().getContent().close();
                }
                throw new ClientProtocolException("Wrong Http Code");
            }

            if (timeoutAuthentication <= 0) {
                throw new ClientProtocolException("Authentication Timeout");
            }

            InputStream in = getRespGetTimetable().getEntity().getContent();

            result = InputStreamUtils.readInputStream(in, TEQUILA_ENCODING);

        } catch (ClientProtocolException e) {
            mExceptionOccured = true;
            Log.e(TAG + "ClientProtocolException", e.getMessage());
            return mContext.getString(R.string.error_http_protocol);
        } catch (IOException e) {
            mExceptionOccured = true;
            Log.e(TAG + "IOException", e.getMessage());
            return mContext.getString(R.string.error_io);
        } catch (TequilaAuthenticationException e) {
            if (mRespGetTimetable != null) {
                try {
                    mRespGetTimetable.getEntity().getContent().close();
                } catch (IllegalStateException e1) {
                    Log.e("ERROR : ", "IllegalStateException");
                } catch (IOException e1) {
                    Log.e("ERROR : ", "IOException");
                }
            }
            mExceptionOccured = true;
            Log.e(TAG + "TequilaAuthenticationException", e.getMessage());
            if (mSessionID == null) {
                return mContext.getString(R.string.error_wrong_credentials);
            } else {
                return mContext.getString(R.string.error_disconnected);
            }
        } catch (NetworkException e) {
            mExceptionOccured = true;
            Log.e(TAG + "NetworkException", e.getMessage());
            return mContext.getString(R.string.network_unreachable);
        }

        if (!this.isCancelled()) {
            getTequilaApi().setUsername(mContext,
                    getGlobalPrefs().getTequilaUsernameCookie().getValue());
            getTequilaApi().setTequilaKey(mContext,
                    getGlobalPrefs().getTequilaKeyCookie().getValue());
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
        mListener.onError(mContext
                .getString(R.string.error_authentication_cancel));
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
            mResult = result;
            // notify error listener
            mListener.onSuccess(mSessionID);
        }
    }

    /**
     * This function takes a token a authenticate it on Tequila
     * 
     * @param token
     * @param firstTry
     *            - If it's the first try, we have to use the username and the
     *            passwd
     * @throws ClientProtocolException
     * @throws IOException
     * @throws TequilaAuthenticationException
     */
    private void authenticateOnTequila(String token, boolean firstTry)
        throws IOException, TequilaAuthenticationException {

        Log.i("INFO : ", "Authentication to Tequila");

        HttpPost authReq = getTequilaUrl();

        List<NameValuePair> postBody = setPostBody(token, firstTry);

        setCookiesForTequila(firstTry);

        authReq.setEntity(new UrlEncodedFormEntity(postBody));
        try {
            getHttpUtils()
                    .executePost(
                            getClient(),
                            authReq,
                            new TequilaResponseHandler(
                                    TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
        } catch (ClientProtocolException e) {
            throw new TequilaAuthenticationException(e.getMessage());
        }

        // We get the cookies from Tequila
        if (firstTry) {
            storeCookiesFromTequila();
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
    private int getAccessToIsa(String sessionID, String tokenList)
        throws IllegalStateException, IOException {
        Log.i("INFO : ", "Try getting access to ISA Services");
        HttpGet getTimetable = getIsaCompleteURL(tokenList);

        if (sessionID != null) {
            getTimetable = setCookiesAndHeaders(getTimetable, sessionID);
        }

        mRespGetTimetable = getHttpUtils().executeGet(getClient(),
                getTimetable, mLocalContext);

        Log.i("INFO : ",
                "Http code received when trying access to ISA Service : "
                        + getRespGetTimetable().getStatusLine().getStatusCode());
        getGlobalPrefs().setSessionIDCookie(
                getHttpUtils().getCookie(getClient(), SESSIONID));

        return getRespGetTimetable().getStatusLine().getStatusCode();
    }

    private HttpGet setCookiesAndHeaders(HttpGet getTimetable, String sessionID)
        throws IllegalStateException, IOException {

        getTimetable.addHeader("Set-Cookie", SESSIONID + "=" + sessionID);
        setNewCookieStore();
        getClient().getCookieStore().addCookie(
                getGlobalPrefs().getSessionIDCookie());
        if (getRespGetTimetable() != null) {
            getRespGetTimetable().getEntity().getContent().close();
        }
        return getTimetable;
    }

    private void setNewCookieStore() {
        getClient().setCookieStore(new BasicCookieStore());
    }

    private HttpGet getIsaCompleteURL(String tokenList) {
        if (tokenList == null) {
            Log.i("INFO : ", "Address : "
                    + getTequilaApi().getIsAcademiaLoginURL());
            return new HttpGet(getTequilaApi().getIsAcademiaLoginURL());
        } else {
            Log.i("INFO : ", "Address : "
                    + getTequilaApi().getIsAcademiaLoginURL() + "?" + KEY + "="
                    + tokenList);
            return new HttpGet(getTequilaApi().getIsAcademiaLoginURL() + "?"
                    + KEY + "=" + tokenList);
        }
    }

    private HttpPost getTequilaUrl() {
        return new HttpPost(getTequilaApi().getTequilaAuthenticationURL());
    }

    private List<NameValuePair> setPostBody(String token, boolean firstTry) {
        List<NameValuePair> postBody = new ArrayList<NameValuePair>();

        postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
        if (firstTry && mUsername != null && mPassword != null) {
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
        }

        return postBody;
    }

    private void setCookiesForTequila(boolean firstTry) {
        if (!(firstTry && mUsername != null && mPassword != null)) {
            // We set the cookies for Tequila authentication
            getClient().setCookieStore(new BasicCookieStore());
            getClient().getCookieStore().addCookie(
                    getGlobalPrefs().getTequilaKeyCookie());
            getClient().getCookieStore().addCookie(
                    getGlobalPrefs().getTequilaUsernameCookie());
        }
    }

    private void storeCookiesFromTequila() {
        getGlobalPrefs().setTequilaUsernameCookie(
                getHttpUtils().getCookie(getClient(), TEQUILA_USER));
        getGlobalPrefs().setTequilaKeyCookie(
                getHttpUtils().getCookie(getClient(), TEQUILA_KEY));
    }
}
