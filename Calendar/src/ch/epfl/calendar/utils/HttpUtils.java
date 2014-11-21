package ch.epfl.calendar.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ch.epfl.calendar.apiInterface.CalendarClientException;

/**
 * This class contains several tools to work with http
 * @author AblionGE
 *
 */
public class HttpUtils {
    
    private static final int RESPONSE_OK = 200;
    
    private static final String EXCEPTION_LOCATION_MALFORMED = 
            "The location field is not well formed in the headers";
    
    public static boolean isNetworkWorking(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public Cookie getCookie(AbstractHttpClient client, String field) {
        if (client == null) {
            return null;
        }
        List<Cookie> lc = client.getCookieStore().getCookies();
        for (Cookie c : lc) {
            if (c.getName().equals(field)) {
                return c;
            }
        }
        return null;
    }
    
    public String getTokenFromHeader(Header location) throws ClientProtocolException {
        if (location == null) {
            throw new ClientProtocolException("No Location field in the headers");
        }
        
        String tokenHeader = location.getValue();
        int i = tokenHeader.indexOf("=");
        if (i == -1) {
            throw new ClientProtocolException(EXCEPTION_LOCATION_MALFORMED);
        }
        return tokenHeader.substring(i+1);
    }
    
    public static void handleResponse(int responseCode) throws CalendarClientException {
        if (responseCode != RESPONSE_OK) {
            throw new CalendarClientException();
        }
    }
    
    public HttpResponse executeGet(AbstractHttpClient httpClient, HttpGet get, HttpContext context)
        throws ClientProtocolException, IOException {
        
        return httpClient.execute(get, context);
    }
    
    public String executePost(AbstractHttpClient httpClient, HttpPost post, ResponseHandler<String> handler)
        throws ClientProtocolException, IOException {
        
        return httpClient.execute(post, handler);
    }
}
