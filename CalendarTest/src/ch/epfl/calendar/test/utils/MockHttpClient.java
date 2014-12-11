/**
 * 
 */
package ch.epfl.calendar.test.utils;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * Mocks an HttpClient (simply returns a given (in the constructor) HttpResponse).
 * 
 * @author gilbrechbuhler
 *
 */
public class MockHttpClient implements HttpClient {

    private HttpResponse mResponse;
    
    public MockHttpClient(HttpResponse reponse) {
        mResponse = reponse;
    }
    
    /**
     * 
     * @return the HttpResponse given in the constructor of the class.
     */
    public HttpResponse execute() {
        return mResponse;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest)
     */
    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return mResponse;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, 
     *      org.apache.http.protocol.HttpContext)
     */
    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context)
        throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return mResponse;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, org.apache.http.HttpRequest)
     */
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request)
        throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return mResponse;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, 
     *      org.apache.http.client.ResponseHandler)
     */
    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
        throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, 
     *      org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
     */
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
        HttpContext context) throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return mResponse;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, 
     *      org.apache.http.client.ResponseHandler, org.apache.http.protocol.HttpContext)
     */
    @Override
    public <T> T execute(HttpUriRequest arg0,
        ResponseHandler<? extends T> arg1, HttpContext arg2)
        throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, 
     *      org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler)
     */
    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1,
        ResponseHandler<? extends T> arg2) throws IOException,
        ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, 
     *      org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler, org.apache.http.protocol.HttpContext)
     */
    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1,
        ResponseHandler<? extends T> arg2, HttpContext arg3)
        throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#getConnectionManager()
     */
    @Override
    public ClientConnectionManager getConnectionManager() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.http.client.HttpClient#getParams()
     */
    @Override
    public HttpParams getParams() {
        // TODO Auto-generated method stub
        return null;
    }
}
