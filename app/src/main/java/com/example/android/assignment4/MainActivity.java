package com.example.android.assignment4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;


/**
 * Created by SINDHU on 18-03-2017.
 */

public class MainActivity extends AppCompatActivity {
    String mode;
    FrameLayout mframe;
    ListView lview;
    HttpURLConnection urlConnection = null;
     String userArray="",nickname="",latitude="",longitude="";
    String result;
    ArrayList<String> userlist = new ArrayList<String>();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mframe = (FrameLayout) findViewById(R.id.mapFrame);
        lview = (ListView) findViewById(R.id.userlist);
        PostFragment postFragment = new PostFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragtran = fm.beginTransaction();
        fragtran.replace(R.id.mapFrame, postFragment);
        fragtran.addToBackStack(null);
        fragtran.commit();



    }


    //inflating menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Menu selection listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.post_mode:
                PostFragment postFragment = new PostFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mapFrame, postFragment).commit();
                Log.d("message", "Post mode selected");
                return true;
            case R.id.view_mode:
                Log.d("message", "Search for people selected");
                return true;
            case R.id.viewsubmenu1:
                Log.d("message", "inside view all users");
                return true;
            case R.id.subsubmenu1:
                Intent intent = new Intent(this, ListViewAllUsers.class);
                startActivity(intent);
                return true;
            case R.id.subsubmenu2:
                String users="";
                DownloadWebpageTask download =new DownloadWebpageTask();
                try {
                    users=download.execute("http://bismarck.sdsu.edu/hometown/users?page=0").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                MapView mapView = new MapView();
                Bundle bundleuser=new Bundle();
                bundleuser.putString("userarray",users);
                mapView.setArguments(bundleuser);
                getSupportFragmentManager().beginTransaction().replace(R.id.mapFrame, mapView).commit();
                return true;
            case R.id.viewsubmenu2:
                Intent intentNew = new Intent(this, FilterUsers.class);
                startActivity(intentNew);
                return true;
        }
        return super.onOptionsItemSelected(item);

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

        protected void onPostExecute(Double result) {
            //pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress) {
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
                    Log.d("message", "Server not happy");
                }
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readIt(in, contentLength);
            } catch (MalformedURLException badURL) {
                Log.e("rew", "Bad URL", badURL);
            } catch (IOException io) {
                Log.e("rew", "network issue", io);
            } finally {
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

