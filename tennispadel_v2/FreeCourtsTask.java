package com.example.tennispadel_v2;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.tennispadel_v2.model.FreeCourt;
import com.example.tennispadel_v2.model.FreeCourtsResponse;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FreeCourtsTask extends AsyncTask<String, Void, FreeCourtsResponse> {
    private static final String TAG = "FreeCourtsTask:  ";

    /*public FreeCourtsTask(Activity2 activity2) {
        Log.e(TAG, "Metodo costruttore" );
    }*/

    //  Response response = new Response();

    public FreeCourtsTask(Activity2 listener) {
        mListener = (Listener1) listener;
    }

    public interface Listener1 {

        void onLoaded(List<FreeCourt> courtList);

        void onError();

        void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
    }

    private Listener1 mListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "Metodo onPreExecute" );

    }

    @Override
    protected FreeCourtsResponse doInBackground(String... strings) {
        Log.e(TAG, "Metodo doInbg ");
        FreeCourtsResponse response = new FreeCourtsResponse();
        try {
            Log.e(TAG, "Metodo doInbg : prima di fare loadJson");
            String stringResponse = loadJSON(strings[0]);

            Log.e(TAG, "Metodo doInBg dopo loadJASON: stringa di risposta " + stringResponse);
            response.setFreecourts(parsejson(stringResponse));
            return response;

        } catch (IOException e) {
            Log.e(TAG, "Metodo doInBg IOException! ");
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Metodo doInBg JsonSyntaxException! ");
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(FreeCourtsResponse response) {

        Log.e(TAG, "Metodo onpostexecute " + response.getFreecourts().size());
        if (response != null) {
            mListener.onLoaded(response.getFreecourts());
        } else {
            mListener.onError();
        }
    }

   // ERRORE:     Process: com.example.provatennispadel, PID: 8698
    //java.lang.NullPointerException: Attempt to invoke virtual method 'java.util.List com.example.provatennispadel.model.FreeCourtsResponse.getFreecourts()' on a null object reference
   //     at com.example.provatennispadel.FreeCourtsTask.onPostExecute(FreeCourtsTask.java:75)
  //      at com.example.provatennispadel.FreeCourtsTask.onPostExecute(FreeCourtsTask.java:21)
  //      at android.os.AsyncTask.finish(AsyncTask.java:771)


    private String loadJSON(String jsonURL) throws IOException {

        Log.e(TAG, "Metodo loadjson con URL " + jsonURL);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(jsonURL);
            conn = (HttpURLConnection) url.openConnection();
            Log.e(TAG, "Metodo loadjson creata connection ");
            conn.setReadTimeout(10000);

            int code = conn.getResponseCode();
            if (code !=  200) {
                Log.e(TAG, "Invalid response from server: " + code);
            }
            else Log.e(TAG, "---------------risposta : " + code);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {

                response.append(line);
            }

            in.close();
            Log.e(TAG, "Metodo loadJjon2 " + response.toString());
            return response.toString();
        } catch (Exception e) {
            Log.e(TAG, "Errore in concect " + e.getClass() + " msg=" + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    private List<FreeCourt>  parsejson(String data) {
        List<FreeCourt> lista = new ArrayList<FreeCourt>();
        Log.e(TAG, "PARSE JSON");
        try{
            JSONArray jsonMainNode = new JSONArray(data);
            Log.e(TAG, "jsonArrLength = "+jsonMainNode.length() );
            for (int i = 0; i < jsonMainNode.length(); i++) {

                FreeCourt court = new FreeCourt();

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                court.setCourtid(jsonChildNode.getString("id"));
                court.setCourtsport(jsonChildNode.getString("type"));

                lista.add(court);
            }
            Log.e(TAG, "Lista.lenght = "+ lista.size() );
        }catch(
                Exception e)
        {
            Log.i(TAG, "Error parsing data" + e.getMessage());
        }
        return lista;
    }
}
