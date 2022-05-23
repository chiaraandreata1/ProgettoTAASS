package com.example.tennispadel_v2;

import com.example.tennispadel_v2.model.Reservation;
import com.example.tennispadel_v2.model.Response;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadJSONTask extends AsyncTask<String, Void, Response> {

    private static final String TAG = "loadJSONTask:  ";

    //  Response response = new Response();

    public LoadJSONTask(MainActivity listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(List<Reservation> androidList);

        void onError();
    }

    private Listener mListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "Metodo onPreExecute" );

    }

    @Override
    protected Response doInBackground(String... strings) {
        Log.e(TAG, "Metodo doInbg di LoadJSONTask");
        Response response = new Response();
        try {
            Log.e(TAG, "Metodo doInbg di LoadJSONTask: prima di fare loadJson");
            String stringResponse = loadJSON(strings[0]);

            Log.e(TAG, "Metodo doInBg dopo loadJASON: stringa di risposta " + stringResponse);
            response.setReserv(parsejson(stringResponse));
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
    protected void onPostExecute(Response response) {

        Log.e(TAG, "Metodo onpostexecute " + response.getReserv().size());
        if (response != null) {
            mListener.onLoaded(response.getReserv());
        } else {
            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        Log.e(TAG, "Metodo loadjson con URL " + jsonURL);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(jsonURL);
            conn = (HttpURLConnection) url.openConnection();
            Log.e(TAG, "Metodo loadjson creata connection ");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

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

    private List<Reservation>  parsejson(String data) {

        List<Reservation> lista = new ArrayList<Reservation>() ;
        try {
            //  JSONObject obj = new JSONObject(data);
            JSONArray jsonMainNode = new JSONArray(data);
            Log.e(TAG, "jsonArrLength = "+jsonMainNode.length() );
            for (int i = 0; i < jsonMainNode.length(); i++) {

                Reservation res = new Reservation();

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                res.setResid(jsonChildNode.getString("id"));
                res.setDate( jsonChildNode.getString("date"));
                res.setSport(jsonChildNode.getString("sportReservation"));
                res.setType_res(jsonChildNode.getString("typeReservation"));
                JSONObject jcourt = jsonChildNode.getJSONObject("courtReservation");
                res.setCourtid(jcourt.getString("id"));

                lista.add(res);
            }
        }catch(
                Exception e)
        {
            Log.i(TAG, "Error parsing data" + e.getMessage());
        }
        return lista;
    }

}
