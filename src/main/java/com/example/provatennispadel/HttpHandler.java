package com.example.provatennispadel;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private static final String TAG1 = "DEBUG HttpHandler: ";

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        String conn_response = null;
        try {
            URL url = new URL(reqUrl);
            Log.e(TAG1, "CONNECTION passo1 : ");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.e(TAG1, "CONNECTION passo2 : fatta connessione");
            conn.setRequestMethod("GET");
            Log.e(TAG1, "CONNECTION passo3 : indicata get");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            Log.e(TAG1, "CONNECTION passo4 : prende inputstream ");
            conn_response = convertStreamToString(in);
            Log.e(TAG1, "passo5 : RESTITUITA RESPONSE DA CONNECTION" + conn_response);
            //response = "{ \"android\": [ { \"ver\": \"1.5\", \"name\": \"Cupcake\", \"api\": \"API level 3\" }, { \"ver\": \"1.6\", \"name\": \"Donut\", \"api\": \"API level 4\" } ] }";
            //Log.e(TAG1, "passo5 : crea la response" + response);
        }/* catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getClass());
        } */catch (Exception e){
            Log.e(TAG, "Exception: " + e.getClass() + e.getMessage());
        }
        //return response;
        return conn_response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
