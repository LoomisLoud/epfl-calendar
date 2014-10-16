package ch.epfl.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author lameAppInc
 *
 */
public class MainActivity extends Activity {
    private static final int READ_TIMEOUT = 7000;
    private static final int CONNECT_TIMEOUT = 8000;
    private static final String TAG = "MainActivity";
    private static final int BUFFER_LENGTH = 100000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		new FetchInformations().execute();
      
    }

    public String doInternetStuff() {
        String sUrl = "https://isa.epfl.ch/services/pedaperiods";
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.addRequestProperty("Accept", "application/json");
            //start query - update, this is implicitly done by getInputStream()
            //httpURLConnection.connect();
            InputStream inputStreamObject = httpURLConnection.getInputStream();
            // Convert the InputStream into a string
            //BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
            //StringBuilder responseStrBuilder = new StringBuilder();
            String contentAsString = readInput(inputStreamObject, BUFFER_LENGTH);
            System.out.println(contentAsString);
            
            /*while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }*/
            Log.d(TAG, contentAsString);

            //JSONObject json = new JSONObject(responseStrBuilder.toString()+"}");
            JSONObject json = new JSONObject(contentAsString);

            //close
            //inputStreamObject.close();
            //streamReader.close();
            httpURLConnection.disconnect();
            //System.out.println(json.toString());
            //Log.d(TAG, json.toString());
            return json.toString();

        } catch (ProtocolException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Read an InputStream and returns the content as a String
     * @param inputStream
     * @param bufferLen
     * @return
     * @author AblionGE
     * @throws IOException 
     */
    private String readInput(InputStream inputStream, int bufferLen) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(inputStream, "UTF-8");
        char[] buffer = new char[bufferLen];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 
     * @author AblionGE
     *
     */
    private class FetchInformations extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return doInternetStuff();
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i("result request", result);
        }
    }
}
