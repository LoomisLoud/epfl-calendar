package ch.epfl.calendar.utils;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;

/**
 * This class contains several tools to work with http
 * @author AblionGE
 *
 */
public class HttpUtils {
    
    private static final int RESPONSE_OK = 200;
    
    public static boolean isNetworkWorking(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static Cookie getCookie(AbstractHttpClient client, String field) {
        List<Cookie> lc = client.getCookieStore().getCookies();
        for (Cookie c : lc) {
            if (c.getName().equals(field)) {
                return c;
            }
        }
        return null;
    }
    
    public static String getTokenFromHeader(Header location) throws TequilaAuthenticationException {
        if (location == null) {
            throw new TequilaAuthenticationException("Try to get token, but already authenticated");
        }
        
        String tokenHeader = location.getValue();
        int i = tokenHeader.indexOf("=");
        return tokenHeader.substring(i+1);
    }
    
    public static void handleResponse(int responseCode) throws CalendarClientException {
        if (responseCode != RESPONSE_OK) {
            throw new CalendarClientException();
        }
    }
}
