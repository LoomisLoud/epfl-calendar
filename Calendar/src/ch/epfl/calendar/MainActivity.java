package ch.epfl.calendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
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

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		new FetchRandomQuestionTask().execute();
      
    }

    public String doInternetStuff() {
    	String sUrl = "https://isa.epfl.ch/services/gps/EDOC";
    	sUrl = "https://isa.epfl.ch/services/timetable/2013-2014/course/PHYS-320";
    	//sUrl = "https://isa.epfl.ch/service/secure/student/timetable/week";
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
			httpURLConnection.setReadTimeout(7000);
	    	httpURLConnection.setConnectTimeout(8000);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.addRequestProperty("Accept", "application/json");
			//httpURLConnection.addRequestProperty("Accept", "application/json");
			//start query - update, this is implicitly done by getInputStream()
	    	//httpURLConnection.connect();
	    	InputStream inputStreamObject = httpURLConnection.getInputStream();
	    	// Convert the InputStream into a string
	        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
	        StringBuilder responseStrBuilder = new StringBuilder();
	       // responseStrBuilder.
	        //char buffer[] = new char[1024];
	        String inputStr;
	        while ((inputStr = streamReader.readLine()) != null) {
	        //while(streamReader.read(buffer) != -1) {
				responseStrBuilder.append(inputStr);
			}
	        responseStrBuilder.append("\n");
	        Log.i(TAG, "" + responseStrBuilder.toString().length());
            Log.i(TAG, responseStrBuilder.toString());
            Log.i(TAG, ""+this.getFilesDir());
            PrintWriter writer = new PrintWriter(this.getFilesDir() + File.pathSeparator + "the-file-name.txt", "UTF-8");
            writer.println(responseStrBuilder.toString());
	        
	        JSONArray json = new JSONArray(responseStrBuilder.toString());

	        
	        //close
	        //inputStreamObject.close();
	        streamReader.close();
	        httpURLConnection.disconnect();
	        Log.d(TAG, json.toString());
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
    private class FetchRandomQuestionTask extends AsyncTask<String, Void, String> {
		
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
