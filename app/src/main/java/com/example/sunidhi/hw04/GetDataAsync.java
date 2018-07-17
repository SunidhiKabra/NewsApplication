package com.example.sunidhi.hw04;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sunidhi on 12-Mar-18.
 */

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<Item>> {
    HandleData handleData;

    public GetDataAsync(HandleData handleData) {
        this.handleData = handleData;
    }

    @Override
    protected void onPreExecute() {
        MainActivity.progressDialog.setMessage("Loading News");
        MainActivity.progressDialog.setCancelable(false);
        MainActivity.progressDialog.setMax(10000);
        MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        MainActivity.progressDialog.show();




        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Item> result) {
        Log.d("demo","size of items "+ result.size());

        Log.d("demo", "XML Parsing" + result.toString());
        handleData.handledata(result);

    }

    @Override
    protected ArrayList<Item> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<Item> result = new ArrayList<>();
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            Log.d("demo","url in GetDataAsync "+url);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = RssParser.RssSAXParser.parseRss(connection.getInputStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }
    public static interface HandleData
    {
        public void handledata(ArrayList<Item> items);
    }

}

