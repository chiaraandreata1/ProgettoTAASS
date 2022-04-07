package com.example.provatennispadel;

import com.example.provatennispadel.model.Reservation;
import com.example.provatennispadel.model.Response;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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

    private static final String TAG = "DEBUG loadJSONTask:  ";

    //  Response response = new Response();

    public LoadJSONTask(Listener listener) {

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
       //     Log.e(TAG, "Metodo loadjson setRead Tiemout ");
            conn.setConnectTimeout(15000);
        //    Log.e(TAG, "Metodo loadjson setConnTimeout ");
            conn.setRequestMethod("GET");
        //    Log.e(TAG, "Metodo loadjson setRequestMethod GET ");
      //      conn.setDoInput(true);
      //      Log.e(TAG, "Metodo loadjson setDoInput True ");
      //      conn.connect();
      //      Log.e(TAG, "Metodo loadjson eseguito conn.conect ");

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
/*
       String resp = loadJSONprova();
       Log.e(TAG, "Metodo loadJjon2 " + resp );

        return  resp;*/
    }



    private String loadJSONprova() {
        Log.e(TAG, "Metodo loadJsonProva" );
        String risp = "{ \"reservation_list\": [ { \"date\": \"18-03-2022\", \"hour\": \"12:00\", \"resid\": \"1234\" }, { \"date\": \"18-03-2022\", \"hour\": \"13:00\", \"resid\": \"1235\" }, { \"date\": \"21-03-2022\", \"hour\": \"18:00\", \"resid\": \"1236\" },{ \"date\": \"18-03-2022\", \"hour\": \"12:00\", \"resid\": \"1237\" },{ \"date\": \"18-03-2022\", \"hour\": \"12:00\", \"resid\": \"1238\" },{ \"date\": \"18-03-2022\", \"hour\": \"12:00\", \"resid\": \"1239\" } ] }";
        return risp;
    }



    /*private List<Reservation>  parsejson(String data) {

        List<Reservation> lista = new ArrayList<Reservation>() ;
        Log.e(TAG, "Metodo parsejson" );
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray jsonMainNode = obj.getJSONArray("reservation_list");

            Log.e(TAG, "jsonArrLength = "+jsonMainNode.length() );
            for (int i = 0; i < jsonMainNode.length(); i++) {

                Reservation res = new Reservation();

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                res.setDate( jsonChildNode.getString("date"));
                res.setHour(jsonChildNode.getString("hour"));
                res.setId(jsonChildNode.getString("resid"));
                Log.e(TAG, "res name di "+i+ " = "+res.getHour());
                lista.add(res);
            }
        }catch(
                Exception e)
        {
            Log.i(TAG, "Error parsing data" + e.getMessage());
        }
        return lista;
    }*/

    private List<Reservation>  parsejson(String data) {

        List<Reservation> lista = new ArrayList<Reservation>() ;
        Log.e(TAG, "Metodo parsejson 000000" );
        try {
          //  JSONObject obj = new JSONObject(data);
            JSONArray jsonMainNode = new JSONArray(data);
            Log.e(TAG, "Metodo parsejson 1111111111111" );
     //       JSONObject primo = obj.getJSONObject("_embedded");
      //      JSONObject jsonMainNode = obj.getJSONObject("reservation");
         //   JSONArray jsonMainNode = obj.getJSONArray("");
            Log.e(TAG, "Metodo parsejson    222222222222222" );
            Log.e(TAG, "jsonArrLength = "+jsonMainNode.length() );
            for (int i = 0; i < jsonMainNode.length(); i++) {

                Reservation res = new Reservation();

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                res.setResid(jsonChildNode.getString("id"));
                res.setDate( jsonChildNode.getString("dateReservation"));
                res.setHour(jsonChildNode.getString("hourReservation"));
                res.setSport(jsonChildNode.getString("sportReservation"));
                res.setType_res(jsonChildNode.getString("typeReservation"));
                JSONObject jcourt = jsonChildNode.getJSONObject("courtReservation");
                res.setCourtid(jcourt.getString("id"));

                Log.e(TAG, "res name di "+i+ " = "+res.getHour());
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
