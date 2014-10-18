package ch.epfl.calendar.authentication;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author lweingart
 *
 */
public class HttpClientFactory {

    private static AbstractHttpClient httpClient;
    private static final int HTTPS_PORT = 443;

    public static synchronized AbstractHttpClient getInstance() {
        if (httpClient == null) {
            httpClient = create();
        }

        return httpClient;
    }

    private HttpClientFactory() {

    }

    private static AbstractHttpClient create() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        Scheme scheme = new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT);
        schemeRegistry.register(scheme);
        HttpParams params = new BasicHttpParams();
        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
        AbstractHttpClient result = new DefaultHttpClient(connManager, params);

        return result;
    }

}
