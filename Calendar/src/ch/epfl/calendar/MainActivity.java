package ch.epfl.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String sUrl = "https://isa.epfl.ch/services/gps/EDOC";
        sUrl = "http://www.google.com";
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
			//httpURLConnection.addRequestProperty("Accept", "application/json");
			//start query - update, this is implicitly done by getInputStream()
	    	//httpURLConnection.connect();
	    	InputStream inputStreamObject = httpURLConnection.getInputStream();
	    	// Convert the InputStream into a string
	        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
	        StringBuilder responseStrBuilder = new StringBuilder();

	        String inputStr;
	        while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}

	        //JSONObject json = new JSONObject(responseStrBuilder.toString());
	        
	        //close
	        //inputStreamObject.close();
	        streamReader.close();
	        httpURLConnection.disconnect();
	        
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
      
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
}
