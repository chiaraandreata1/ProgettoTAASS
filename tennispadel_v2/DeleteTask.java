package com.example.tennispadel_v2;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tennispadel_v2.model.Reservation;
import com.example.tennispadel_v2.model.Response;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DeleteTask extends AsyncTask<String, Void, Response> {

    private static final String TAG = "DeleteTask:  ";

    //  Response response = new Response();

 /*   public DeleteTask(MainActivity listener) {

        delListener = listener;
    }
*/

    public interface Listener {

        void onLoaded(List<Reservation> androidList);

        void onError();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "Metodo onPreExecute" );

    }

    @Override
    protected Response doInBackground(String... strings) {
        Log.e(TAG, "Metodo doInbg di LoadJSONTask");
        Response response = new Response();
        int retCode ;
        try {
            Log.e(TAG, "Metodo doInbg : prima di fare loadJson");
            //String stringResponse = deleteresbyid(strings[0]);
            deleteresbyid(strings[0]);
            Log.e(TAG, "Metodo doInbg dopo delete");
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
           // delListener.onLoaded(response.getReserv());
        } else {
            //delListener.onError();
        }
    }

    private void deleteresbyid(String jsonURL) throws IOException {

        Log.e(TAG, "Metodo deleteresbyid con URL " + jsonURL);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(jsonURL);
            conn = (HttpURLConnection) url.openConnection();
            Log.e(TAG, "Metodo deleteresbyid creata connection ");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            if (code !=  200) {
                Log.e(TAG, "Invalid response from server: " + code);
            }
            else Log.e(TAG, "---------------risposta : " + code);

        } catch (Exception e) {
            Log.e(TAG, "Errore in concect " + e.getClass() + " msg=" + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }


}
