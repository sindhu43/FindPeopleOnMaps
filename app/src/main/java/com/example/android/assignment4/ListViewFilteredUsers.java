package com.example.android.assignment4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by SINDHU on 19-03-2017.
 */

public class ListViewFilteredUsers extends AppCompatActivity {
    ListView lviewfilter;
    Button okbtn,morebtn;
    int count=0;

    String user_Array,result,param;
    ArrayList<String> userslists = new ArrayList<String>();
    HttpURLConnection urlConnection = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        param=getIntent().getStringExtra("parameter");
        Log.d("message","parameters from listviewfiteredusers.java"+param);
        lviewfilter = (ListView) findViewById(R.id.userlist);
        okbtn= (Button) findViewById(R.id.okaybtn);
        morebtn = (Button) findViewById(R.id.moreUsers);
        try {
            DownloadWebpageTask userdownloadfilter= new DownloadWebpageTask();
            user_Array = userdownloadfilter.execute("http://bismarck.sdsu.edu/hometown/users?page=0"+"&"+param).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("message","user List"+user_Array);
        try {
            JSONArray jObj = new JSONArray(user_Array);
            Log.d("message", "jobj" + jObj.length());
            if (jObj.length() == 0) {
                Log.d("message","inside if for object length checking");
               Toast.makeText(getApplicationContext(),"No records exist for this filter!",Toast.LENGTH_LONG);
            }
            for (int i = 0; i < jObj.length(); i++) {
                JSONObject userJSON = jObj.getJSONObject(i);
                result = "Nick Name: " + userJSON.getString("nickname") + " Country: " + userJSON.getString("country") + " State: " + userJSON.getString("state") + " Year: " + userJSON.getString("year");
                userslists.add(result);
            }

        }catch(JSONException e){
                e.printStackTrace();
            }
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userslists);
            lviewfilter.setAdapter(itemsAdapter);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                DownloadWebpageTask userdownloads=new DownloadWebpageTask();
                try {
                    user_Array = userdownloads.execute("http://bismarck.sdsu.edu/hometown/users?page="+count+"&"+param).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("message","user List"+user_Array);
                try {
                    List<String> moreuserList = new ArrayList<String>();
                    JSONArray jObj = new JSONArray(user_Array);
                    Log.d("message","jobj"+jObj.length());
                    for (int i=0;i<jObj.length();i++) {
                        JSONObject userJSON = jObj.getJSONObject(i);
                        String  moreresult = "Nick Name: "+userJSON.getString("nickname")+" Country: "+userJSON.getString("country")+" State: "+userJSON.getString("state")+" Year: "+userJSON.getString("year");
                        moreuserList.add(moreresult);
                    }
                    ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(ListViewFilteredUsers.this, android.R.layout.simple_list_item_1, moreuserList);
                    lviewfilter.setAdapter(userAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("message","userlist= "+userslists);
            }
        });



    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                Log.e("rew", "Error accessing " + params[0], e);
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        protected void onPostExecute(Double result){
            //pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
        }

        private String downloadUrl(String urlString) throws IOException {
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                int contentLength = urlConnection.getContentLength();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode != 200) {
                    // handle error here
                    Log.d("message","Server not happy") ;
                }
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readIt(in, contentLength);
            } catch(MalformedURLException badURL) {
                Log.e("rew", "Bad URL", badURL);
            } catch (IOException io) {
                Log.e("rew", "network issue", io);
            }
            finally {
                urlConnection.disconnect();
            }
            return "";//error
        }

        public String readIt(InputStream stream, int len) throws IOException,
                UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
