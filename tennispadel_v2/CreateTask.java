package com.example.tennispadel_v2;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "Create Task:  ";

    public CreateTask(Activity2 activity2) {
        Log.e(TAG, "Metodo costruttore" );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "Metodo onPreExecute" );

    }

    @Override
    protected String doInBackground(String... strings) {
        Log.e(TAG, "Metodo doInBg" );
        HttpURLConnection conn = null;
        try {
            Log.e(TAG, "URL" + strings[0]);
            // Creo l'oggetto JSON con i parametri che dobbiamo inviare al Controller
            JSONObject postDataParams = new JSONObject();
            JSONObject courtParam = new JSONObject();
            List<Integer> players = new ArrayList<Integer>();
            //Log.e(TAG, "provaplayer  " + strings[5]);
            if(strings[5] != null   && strings[5].length()>0) {
                players.add(new Integer(strings[5]));
                Log.e(TAG, "provaplayer  1 <" + strings[5]+">");
            }
            if(strings[6] != null && strings[6].length()>0)
                players.add(new Integer(strings[6]));
            if(strings[7] != null && strings[7].length()>0)
                players.add(new Integer(strings[7]) );
            if(strings[8] != null && strings[8].length()>0)
                players.add(new Integer(strings[8]));

            postDataParams.put("date", strings[1] + " " + strings[2]+ ":00");
            postDataParams.put("sportReservation", strings[3]);
            postDataParams.put("typeReservation", "private");
            postDataParams.put("players",new JSONArray(players));
            courtParam.put("id", Integer.parseInt(strings[4]));
            courtParam.put("type", strings[3]);
            postDataParams.put("courtReservation",courtParam);

            Log.e(TAG, "JSON  " + postDataParams.toString());
            Log.e(TAG, "URL create reserv  " + strings[0]);
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            Log.e(TAG, "Metodo deleteresbyid creata connection ");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");

            // Inserito nella request HTTP l'oggetto JSON creato
            Log.e(TAG,"JSON "+ postDataParams.toString());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write((postDataParams).toString());
            writer.flush();
            writer.close();

            int code = conn.getResponseCode();
            if (code !=  200) {
                Log.e(TAG, "Invalid response from server: " + code);
            }
            else Log.e(TAG, "---------------risposta : " + code);

        } catch (Exception e) {
            Log.e(TAG, "Errore in connect " + e.getClass() + " msg=" + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

}

