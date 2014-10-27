package ch.epfl.utils.isaparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * Parser of ISA services with json representation.
 * @author Enea Bell
 *
 */
public class ISAJsonParser {
    
    private static final String TAG = "ISAJsonParser";
    private static final int READ_TIMEOUT = 7000;
    private static final int CONNECT_TIMEOUT = 8000;
    
    //TODO add arg code course
    public void parseDetailsOfCourse() {
        //get request address
        //TODO remove hardcoded arg
        String courseCode = "CS-207";
        String address = ISAServices.getCourseDetailByCourseCode() + courseCode;
        
        JSONArray json = this.getJsonArrayFromAddress(address);
        Log.i(TAG, ""+json);
        
    }
    
    /**
     * 
     * @param address
     * @return null if error, or JSONArray of address url given in argument.
     */
    private JSONArray getJsonArrayFromAddress(String address) {
        HttpURLConnection httpURLConnection = this.initHttpURLConnectionFromUrl(address);
        this.addJSONHeaderInRequest(httpURLConnection);
        try {
            return new JSONArray(this.readInputStreamFromConnection(httpURLConnection));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JSONException e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * {@link #initHttpURLConnectionFromUrl(URL)}.
     * @param url
     */
    private HttpURLConnection initHttpURLConnectionFromUrl(String sUrl) {
        try {
            URL url = new URL(sUrl);

            return this.initHttpURLConnectionFromUrl(url);
            //TODO create exception for higher level
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Init (open and set a get request) an url.
     * @param url Url of isa service wanted.
     * @return A get request.
     */
    private HttpURLConnection initHttpURLConnectionFromUrl(URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //TODO store theses in config file
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection;
            //handle protocol exception too
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Add an header to a get request to ask for json format instead of html/xml...
     * @param httpURLConnection the connection opened as a get request.
     * @throws IllegalStateException if the connection isn't correctly initiated.
     */
    public void addJSONHeaderInRequest(HttpURLConnection httpURLConnection) throws IllegalStateException {
        httpURLConnection.addRequestProperty("Accept", "application/json");
    }
    
    /**
     * Read from an opened connection with json header and store it in a string.
     * @param httpURLConnection The connection opened to remote website with json header.
     * @return json format of website from connection.
     * @throws IOException if couldn't read from the connection.
     */
    private String readInputStreamFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStreamObject = httpURLConnection.getInputStream();
        // Convert the InputStream into a string
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
        //while(streamReader.read(buffer) != -1) {
            responseStrBuilder.append(inputStr);
        }
        String result = responseStrBuilder.toString();
        //close
        streamReader.close();
        httpURLConnection.disconnect();
        return result;
    }
}
