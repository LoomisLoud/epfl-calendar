package ch.epfl.calendar.utils;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;

import ch.epfl.calendar.authentication.TequilaAuthenticationException;

/**
 * This class contains several tools to work with http
 * @author AblionGE
 *
 */
public class HttpUtils {

    public static Cookie getCookieSessionID(AbstractHttpClient client) {
        List<Cookie> lc = client.getCookieStore().getCookies();
        Cookie cookieSessionID = null;
        for (Cookie c : lc) {
            System.out.println(c.toString());
            if (c.getName().equals("JSESSIONID")) {
                cookieSessionID = c;
            }
        }
        return cookieSessionID;
    }
    
    public static String getTokenFromHeader(Header location) {
        if (location == null) {
            throw new TequilaAuthenticationException("Try to get token, but already authenticated");
        }
        
        String tokenHeader = location.getValue();
        int i = tokenHeader.indexOf("=");
        return tokenHeader.substring(i+1);
    }
}