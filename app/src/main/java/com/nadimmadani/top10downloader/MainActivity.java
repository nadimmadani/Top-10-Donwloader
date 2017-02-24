package com.nadimmadani.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button parseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.xmlListView);
        parseButton = (Button) findViewById(R.id.btnParse);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private String mFileContents;

        @Override
        protected String doInBackground(String... strings) {
            mFileContents = downloadXMLFile(strings[0]);

            if (mFileContents == null)
                Log.d("DownloadData", "Error Downloading");

            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was " + result);

            parseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseApplications parseApplications = new ParseApplications(mFileContents);
                    parseApplications.process();
                }
            });
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData", "The response code was " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charRead = isr.read(inputBuffer);
                    if (charRead <= 0)
                        break;
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            } catch (IOException e) {
                Log.d("DownloadData", "IO Exception Reading Data: " + e.getMessage());
                return null;
            }catch (SecurityException e) {
                Log.d("DownloadData", "Security exception. Needs Permission? " + e.getMessage());
            }
            return null;
        }
    }
}
