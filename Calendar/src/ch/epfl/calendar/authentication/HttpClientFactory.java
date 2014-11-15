package ch.epfl.calendar.authentication;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.RedirectHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * Factory for HttpClient
 * @author lweingart
 *
 */
public final class HttpClientFactory {

    private static AbstractHttpClient httpClient;
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private static final String DEBUG = "Debug";

    public static synchronized AbstractHttpClient getInstance() {
        if (httpClient == null) {
            //create() is used instead of create an instance of do a 
            //new HttpClientFactory because create returns an AbstractHttpClient
            httpClient = create();
        }

        return httpClient;
    }

    public static synchronized void setInstance(AbstractHttpClient instance) {
        if (instance == null) {
            Log.d(DEBUG, "Argument of setInstance is null");
        } else {
            httpClient = instance;
        }
    }

    private static final RedirectHandler REDIRECT_NO_FOLLOW = new RedirectHandler() {
        @Override
        public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
            return false;
        }

        @Override
        public URI getLocationURI(HttpResponse response, HttpContext context) throws org.apache.http.ProtocolException {
            return null;
        }
    };

    private static final HttpRequestInterceptor LOGGING_REQUEST_INTERCEPTOR = new HttpRequestInterceptor() {
        @Override
        public void process(HttpRequest request, HttpContext context) {
            Log.d("HTTP REQUEST", request.getRequestLine().toString());
        }
    };

    private static final HttpResponseInterceptor LOGGING_RESPONSE_INTERCEPTOR = new HttpResponseInterceptor() {
        @Override
        public void process(HttpResponse response, HttpContext context) {
            Log.d("HTTP RESPONSE", response.getStatusLine().toString());
        }
    };

    private static AbstractHttpClient create() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT));
        HttpParams params = new BasicHttpParams();
        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
        AbstractHttpClient result = new DefaultHttpClient(connManager, params);
        result.setRedirectHandler(REDIRECT_NO_FOLLOW);
        result.addRequestInterceptor(LOGGING_REQUEST_INTERCEPTOR);
        result.addResponseInterceptor(LOGGING_RESPONSE_INTERCEPTOR);
        return result;
    }
}