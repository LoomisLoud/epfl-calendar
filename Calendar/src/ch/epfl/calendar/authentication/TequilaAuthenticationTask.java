package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.epfl.calendar.R;
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

    private Context mContext = null;
    private String mSessionID;
    private ProgressDialog dialog;
    private TequilaAuthenticationListener mListener = null;
    private boolean exceptionOccured = false;

    private HttpContext mLocalContext = null;
    private HttpResponse mRespGetTimetable = null;
    private Cookie mCookieWithSessionID = null;
    private TequilaAuthenticationAPI tequilaApi = TequilaAuthenticationAPI.getInstance();
    private AbstractHttpClient client = HttpClientFactory.getInstance();

    private final String mUsername;
    private final String mPassword;
    private String currentToken;

    private static final String PASSWORD = "password";
    private static final String REQUEST_KEY = "requestkey";
    private static final String KEY = "key";
    private static final String USERNAME = "username";
    private static final String SESSIONID = "JSESSIONID";

    private static final int TIMEOUT_AUTHENTICATION = 10;

    /**
     * The interface pf the authentication task
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
        dialog = new ProgressDialog(mContext);
        dialog.setTitle(mContext.getString(R.string.be_patient));
        dialog.setMessage(mContext.getString(R.string.authenticating));
        dialog.setCancelable(false);
        dialog.show();
    }

	@Override
	protected String doInBackground(Void... params) {

        try {
            mLocalContext = new BasicHttpContext();
            int httpCode = 0;

            /***************SHOULD BE IN CALENDARCLIENT************/
            //Try to access to ISA to get a token
            httpCode = getAccessToIsa(null, null);
            
            currentToken = HttpUtils.getTokenFromHeader(mRespGetTimetable.getFirstHeader("Location"));
            String tokenList = currentToken;
            /******************************************************/
            if (httpCode == TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE
                    || httpCode == TequilaAuthenticationAPI.STATUS_CODE_OK) {

                boolean firstTry = true;
                int timeoutAuthentication = TIMEOUT_AUTHENTICATION;

                while (httpCode != TequilaAuthenticationAPI.STATUS_CODE_OK && timeoutAuthentication > 0) {
                    timeoutAuthentication--;
                    //Authentication on Tequila needed the token + to know if
                    //it is the first authentication (to know if use username+pwd)
                    authenticateOnTequila(currentToken, firstTry);
                    if (firstTry) {
                        firstTry = false;
                    } else {
                        currentToken = HttpUtils.getTokenFromHeader(mRespGetTimetable.getFirstHeader("Location"));
                        tokenList = tokenList + "&" + KEY + "=" + currentToken;
                    }
                    /***********************SHOULD BE IN CALENDARCLIENT*********************/
                    //Try to get the page on Isa
                    httpCode = getAccessToIsa(mSessionID, tokenList);
                    /************************************************************************/

                    mSessionID = mCookieWithSessionID.getValue();
                }
            } else {
                throw new TequilaAuthenticationException("Wrong Http code");
            }

            /************************/
            //FIXME : Needs to be removed - keep for testing manually
            String result = InputStreamUtils.readInputStream(mRespGetTimetable.getEntity().getContent());
            System.out.println(result);
            /************************/

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


    /**
     * This function takes a token a authenticate it on Tequila
     * @param token
     * @param firstTry - If it's the first try, we have to use the username and the passwd
     * @throws ClientProtocolException
     * @throws IOException
     */
    private void authenticateOnTequila(String token, boolean firstTry) throws ClientProtocolException, IOException {
        Log.i("INFO : ", "Authentication to Tequila");

        HttpPost authReq = new HttpPost(tequilaApi.getTequilaAuthenticationURL());
        List<NameValuePair> postBody = new ArrayList<NameValuePair>();
        postBody.add(new BasicNameValuePair(REQUEST_KEY, token));
        if (firstTry && mUsername != null && mPassword != null) {
            postBody.add(new BasicNameValuePair(USERNAME, mUsername));
            postBody.add(new BasicNameValuePair(PASSWORD, mPassword));
        }
        authReq.setEntity(new UrlEncodedFormEntity(postBody));
        client.execute(authReq,
                new CustomResponseHandler(TequilaAuthenticationAPI.STATUS_CODE_AUTH_RESPONSE));
    }

    /**
     * THIS PURPOSE SHOULD BE AIMED IN CALENDARCLIENT
     * @param sessionID
     * @param tokenList
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int getAccessToIsa(String sessionID, String tokenList)
        throws ClientProtocolException, IOException {
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
            //client.getCookieStore().addCookie(mCookieWithSessionID);
            client.getCookieStore().addCookie(new BasicClientCookie(SESSIONID, sessionID));
            mRespGetTimetable.getEntity().getContent().close();
        }
        
        mRespGetTimetable = client
                .execute(getTimetable, mLocalContext);
        Log.i("INFO : ", "Http code received when trying access to ISA Service : "
                + mRespGetTimetable.getStatusLine().getStatusCode());
        this.mCookieWithSessionID = HttpUtils.getCookieSessionID(client);
        
        return mRespGetTimetable.getStatusLine().getStatusCode();
    }
}
