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

    public static Cookie getCookie(AbstractHttpClient client, String field) {
        List<Cookie> lc = client.getCookieStore().getCookies();
        for (Cookie c : lc) {
            if (c.getName().equals(field)) {
                return c;
            }
        }
        return null;
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
