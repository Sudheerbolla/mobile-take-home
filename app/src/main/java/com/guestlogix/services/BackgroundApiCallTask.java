package com.guestlogix.services;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.guestlogix.listeners.IParserListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundApiCallTask extends AsyncTask<String, Void, String> {

    private IParserListener iParserListener;
    private static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private int requestCode;

    public BackgroundApiCallTask(IParserListener iParserListener, int requestCode) {
        this.requestCode = requestCode;
        this.iParserListener = iParserListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String stringUrl = strings[0];
            Log.e("requestUrl: ", stringUrl);
            String inputLine;
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(GET_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            iParserListener.errorResponse(requestCode, e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            iParserListener.errorResponse(requestCode, e.toString());
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!TextUtils.isEmpty(s)) {
            Log.e("response: ", s);
            iParserListener.successResponse(requestCode, s);
        }
    }

}
