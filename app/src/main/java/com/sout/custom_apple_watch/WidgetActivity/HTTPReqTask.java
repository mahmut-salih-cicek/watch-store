package com.sout.custom_apple_watch.WidgetActivity;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HTTPReqTask extends AsyncTask<Void, Void, Void> {

    public static String HTTP_REQUEST_temp_c = "";
    public static String HTTP_REQUEST_Icon = "";

    HttpURLConnection urlConnection = null;
    URL url = null;
    JSONObject object = null;
    JSONArray myArray = null;
    InputStream inStream = null;


    String FUUL_URL;

    public HTTPReqTask(String name) {
        FUUL_URL = name;
    }


    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        System.out.println("Do ın back calıstı");
        System.out.println("FULLLLLLLLLLLL === "+FUUL_URL);
        // GET
        try {

            URL url = new URL(FUUL_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();
            if (code != 200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            String response = "";
            while ((line = rd.readLine()) != null) {
                Log.i("data", line);
                response = response + line;
            }

            object = (JSONObject) new JSONTokener(response).nextValue();
            JSONObject obj = object.getJSONObject("current");
            String weatherTemp = obj.getString("temp_c");
            JSONObject obj2 = obj.getJSONObject("condition");
            String weatherIcon = obj2.getString("icon");

            HTTP_REQUEST_temp_c = weatherTemp;
            HTTP_REQUEST_Icon = weatherIcon;

            System.out.println("sadasdasd"+weatherTemp);
            System.out.println("tempcccccccccccccc");



        } catch (Exception e) {
            System.out.println("bıg mıstakes "+e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // POST

        return null;
    }
}

