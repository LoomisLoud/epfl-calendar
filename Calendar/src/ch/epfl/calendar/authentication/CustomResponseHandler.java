package ch.epfl.calendar.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import android.util.Log;

/**
 * Custom response handler for the Tequila authorization.
 * @author lweingart
 *
 */
public class CustomResponseHandler implements ResponseHandler<String> {

    private static final int BUF_SIZE = 1024;
    private static final String TAG = "CustomResponseHandler Class:: ";

    private final int mStatusCodeExpected;

    public CustomResponseHandler(int statusCodeExpected) {
        this.mStatusCodeExpected = statusCodeExpected;
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        if (response.getStatusLine().getStatusCode() != this.mStatusCodeExpected) {
            Log.d(TAG, "Get : " + response.getStatusLine().getStatusCode()
                    + " and expected : "
                    + this.mStatusCodeExpected);
            throw new ClientProtocolException("Wrong Http Code received");
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        return readInputStream(is);
    }

    private static String readInputStream(InputStream is) throws IOException {
        Reader in = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[BUF_SIZE];
        int read = in.read(buffer);
        StringBuilder sb = new StringBuilder();

        while (read > 0) {
            sb.append(String.copyValueOf(buffer, 0, read));
            buffer = new char[BUF_SIZE];
            read = in.read(buffer);
        }
        in.close();

        return sb.toString();
    }
}
