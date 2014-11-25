/**
 * 
 */
package ch.epfl.calendar.apiInterface;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLEncoder;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.utils.HttpUtils;
import ch.epfl.calendar.utils.InputStreamUtils;

/**
 * URL to use to access App Engine : http://versatile-hull-742.appspot.com
 * This class implements methods to reach and communicate with the App Engine.
 * 
 * @author gilbrechbuhler
 *
 */
public class AppEngineClient implements DatabaseInterface {
    
    private static final String APP_ENGINE_ENCODING = "UTF-8";
    
    private String mDBUrl;
    private HttpClient mHttpClient;
    
    /**
     * 
     * @param dbUrl = http://versatile-hull-742.appspot.com to access app engine
     */
    public AppEngineClient(String dbUrl) throws CalendarClientException {
        try {
            new URL(dbUrl);
        } catch (MalformedURLException malformedUrlException) {
            throw new CalendarClientException(malformedUrlException);
        }
        mDBUrl = dbUrl;
        mHttpClient = new DefaultHttpClient();
    }
    
    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.databaseInterface#getCourseByName(java.lang.String)
     */
    @Override
    public Course getCourseByName(String name) throws CalendarClientException {
        String nameUrlized;
        try {
            nameUrlized = URLEncoder.encode(name.toLowerCase(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CalendarClientException("An error occured in the request.");
        } catch (NullPointerException e) {
            throw new CalendarClientException("An error occured in the request.");
        }
        String url = mDBUrl + AppEngineURLs.GET_COURSE + "?name=" + nameUrlized;
        return getCourse(url);
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.databaseInterface#getCourseByCode(java.lang.String)
     */
    @Override
    public Course getCourseByCode(String code) throws CalendarClientException {
        String url = mDBUrl + AppEngineURLs.GET_COURSE + "?code=" + code;
        return getCourse(url);
    }
    
    /**
     *
     * @return the {@link HttpClient} used by the methods of this class
     */
    public HttpClient getHttpClient() {
        return mHttpClient;
    }
    
    /**
     * Returns a String containing the content of the passed InputStream.
     * 
     * @param is the {@link InputStream} to read.
     * @return the content of is in a String.
     * @throws IOException
     */
    /*private String readStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();  
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream),
                NetworkConstants.BUFFERED_READER_SIZE);
        for (String line = reader.readLine(); line != null; line =reader.readLine()) {
            stringBuilder.append(line);  
        }  
        inputStream.close();  
        return stringBuilder.toString();
    }*/
    
    private Course getCourse(String fullUrl) throws CalendarClientException {
        Course course = null;
        InputStream inputStream;
        try {
            //HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = getHttpClient().execute(new HttpGet(fullUrl));
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpUtils.handleResponse(responseCode);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String responseBody = InputStreamUtils.readInputStream(inputStream, APP_ENGINE_ENCODING);
                JSONObject jsonObject = new JSONObject(responseBody);
                if (jsonObject.length() >= 1) {
                    course = Course.parseFromJSON(jsonObject);
                }
            }
        } catch (IllegalArgumentException illegalArgExc) {
            throw new CalendarClientException(illegalArgExc);
        } catch (IOException ioException) {
            throw new CalendarClientException(ioException);
        } catch (JSONException jsonException) {
            throw new CalendarClientException(jsonException);
        }
        
//        if (course != null) {
//            List<Period> periods = getPeriodsByCourseCode(course.getCode());
//            course.setPeriods(periods);
//        }

        return course;
    }

//    private List<Period> getPeriodsByCourseCode(String code) throws CalendarClientException {
//        String fullUrl = mDBUrl + AppEngineURLs.GET_PERIODS + "?code=" + code;
//        List<Period> periodsList = new ArrayList<Period>();
//        InputStream inputStream;
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpResponse httpResponse = httpClient.execute(new HttpGet(fullUrl));
//            inputStream = httpResponse.getEntity().getContent();
//            if (inputStream != null) {
//                String responseBody = readStream(inputStream);
//                JSONObject jsonObject = new JSONObject(responseBody);
//                if (jsonObject.length() >= 1) {
//                    periodsList = Period.parseFromJSON(jsonObject);
//                }
//            }
//        } catch (IllegalArgumentException illegalArgExc) {
//            illegalArgExc.printStackTrace();
//            throw new CalendarClientException(illegalArgExc);
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//            throw new CalendarClientException(ioException);
//        } catch (JSONException jsonException) {
//            jsonException.printStackTrace();
//            throw new CalendarClientException(jsonException);
//        }
//        
//        return periodsList;
//    }
    
    /* (non-Javadoc)
     * {@see ch.epfl.calendar.apiInterface.databaseInterface#createPeriod(ch.epfl.calendar.data.Period, 
     * java.lang.String)}
     */

//    public void createPeriod(Period period, String courseCode) throws CalendarClientException {
//        String url = mDBUrl + AppEngineURLs.CREATE_PERIOD;
//        JSONObject jsonPeriod = null;
//        try {
//            jsonPeriod = period.getJSONFromPeriod(courseCode);
//        } catch (JSONException jsonException) {
//            throw new CalendarClientException(jsonException);
//        }
//        
//        if (jsonPeriod == null) {
//            throw new CalendarClientException();
//        }
//        
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(url);
//            StringEntity jsonAsString = new StringEntity(jsonPeriod.toString());
//            httpPost.setEntity(jsonAsString);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//            BasicResponseHandler responseHandler = new BasicResponseHandler();
//            httpClient.execute(httpPost, responseHandler);
//        } catch (UnsupportedEncodingException unsupportedEncException) {
//            throw new CalendarClientException(unsupportedEncException);
//        } catch (ClientProtocolException clientProtoException) {
//            throw new CalendarClientException(clientProtoException);
//        } catch (IOException ioException) {
//            throw new CalendarClientException(ioException);
//        }
//    }

}
