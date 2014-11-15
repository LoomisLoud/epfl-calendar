package ch.epfl.calendar.authentication.tests;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import ch.epfl.calendar.authentication.HttpClientFactory;

import junit.framework.TestCase;

/**
 * Test class for the HttpClientFactory
 * @author AblionGE
 *
 */
public class HttpClientFactoryTest extends TestCase{
    private AbstractHttpClient client = null;

    public final void testGetInstance() {
        //First call
        assertNull(client);
        client = HttpClientFactory.getInstance();
        assertNotNull(client);
        assertEquals(DefaultHttpClient.class, client.getClass());
        
        //Second call
        AbstractHttpClient secondClient = HttpClientFactory.getInstance();
        assertEquals(client, secondClient);
        
        //Check that the connectionManager is the right one
        assertEquals(client.getConnectionManager().getClass(), ThreadSafeClientConnManager.class);
    }

    public final void testSetInstanceNull() {
        client = HttpClientFactory.getInstance();
        HttpClientFactory.setInstance(null);
        AbstractHttpClient secondClient = HttpClientFactory.getInstance();

        assertEquals(client, secondClient);
    }
    
    public final void testSetInstance() {
        AbstractHttpClient defaultClient = new DefaultHttpClient();
        HttpClientFactory.setInstance(defaultClient);
        client = HttpClientFactory.getInstance();
        assertEquals(defaultClient, client);
    }
    
    public final void testRedirectNoFollow() {
        
    }

}
