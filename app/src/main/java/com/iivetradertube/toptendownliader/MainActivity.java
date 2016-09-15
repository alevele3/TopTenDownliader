package com.iivetradertube.toptendownliader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String mFileContents;
    private Button btnParse;
    private ListView listApps;
    //private TextView xmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //xmlTextView = (TextView) findViewById(R.id.xmlTextView);
        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // to do add parsse  activtsn code

                ParseApps parseApplications = new ParseApps(mFileContents);
                parseApplications.process();
                ArrayAdapter<Application> arrayAdapter = new ArrayAdapter<Application>(
                        MainActivity.this, R.layout.list_item, parseApplications.getApplications());
                listApps.setAdapter(arrayAdapter);




            }
        });
        listApps = (ListView) findViewById(R.id.xmlListView);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=25/xml");

    }


    private class DownloadData extends AsyncTask<String,Void, String>{



        @Override
        protected String doInBackground(String... params) {
            mFileContents = downXMLFile(params[0]);
            if(mFileContents == null){
                Log.d("DownloadData","Error downlad");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result );
            //xmlTextView.setText(mFileContents);

        }

        private String downXMLFile(String urlPath){

            StringBuilder tempBuffer = new StringBuilder();
            try {

                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData", "IO Exception data: " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];
                while (true){
                    charRead = isr.read(inputBuffer);
                    if(charRead <= 0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }
                return tempBuffer.toString();


            }catch (IOException e){
                Log.d("DownloadData", "IO Exception readiing data: " + e.getMessage());
                //e.printStackTrace();

            }catch(SecurityException e){
                Log.d("DownloadData" , "Security exception. Needs permission? " + e.getMessage());
            }

            return null;
        }

    }

}
