package com.example.provatennispadel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.example.provatennispadel.model.Reservation;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    //public static final String URL = "http://172.27.3.8:8083/reservations/";
    public static final String URL = "http://172.27.3.62:8080/api/v1/reservations/";

    private List<HashMap<String, String>> mReservList = new ArrayList<>();

    private static final String TAG = "DEBUG:  ";

    private static final String KEY_DATE = "date";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_RESID = "resid";
    private static final String KEY_SPORT = "sport";
    private static final String KEY_TYPERES = "type_res";
    private static final String KEY_COURTID = "courtid";

    Button b1  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "Metodo onCreate di MainActivity ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);


        new LoadJSONTask(this).execute(URL);
  //      Toast.makeText(this, "onCreate ", Toast.LENGTH_SHORT).show();

        b1 = findViewById(R.id.btn1);
        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,Activity2.class);
                        startActivity(i);
                    }
                }
        );

    }

    @Override
    public void onLoaded(List<Reservation> androidList) {

        Log.e(TAG, "Metodo onLoaded di MainActivity "+androidList.size());
 //       Toast.makeText(this, "onloaded" + androidList.size(), Toast.LENGTH_SHORT).show();
        for (Reservation android : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_DATE, android.getDate());
            map.put(KEY_HOUR, android.getHour());
            map.put(KEY_RESID, android.getResid());
            map.put(KEY_SPORT, android.getSport());
            map.put(KEY_TYPERES, android.getType_res());
            map.put(KEY_COURTID, android.getCourtid());


            mReservList.add(map);
  //          Toast.makeText(this, android.getName(), Toast.LENGTH_SHORT).show();

        }

        loadListView(androidList);
    }

    @Override
    public void onError() {

        Log.e(TAG, "Metodo onerror di MainActivity ");

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //Toast.makeText(this,"type res <"+ mReservList.get(i).get(KEY_HOUR)+">", Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Elemento selezionato numero:  " + i, Toast.LENGTH_LONG).show();
        Log.e(TAG, "metodo onitemclick");

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.testoDate);
        //text.setText("selezionata: "+i);
        text.setText(mReservList.get(i).get(KEY_DATE));

        TextView text1 = (TextView) dialog.findViewById(R.id.testoHour);
        //text.setText("selezionata: "+i);
        text1.setText(mReservList.get(i).get(KEY_HOUR));

        TextView text2 = (TextView) dialog.findViewById(R.id.testoIdRes);
        //text.setText("selezionata: "+i);
        text2.setText(mReservList.get(i).get(KEY_RESID));

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonBack);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Btn BACK");
                dialog.dismiss();
            }
        });

        Log.e(TAG, "metodo onitemclick Dialog con bottone delete  "+ mReservList.get(i).get(KEY_TYPERES) );

        //bottone per eliminare la reservation
        Button dialogButtonDelete = (Button) dialog.findViewById(R.id.dialogButtonDel);
        if (mReservList.get(i).get(KEY_TYPERES).equals("private")){
            dialogButtonDelete.setVisibility(View.VISIBLE); //To set visible
        }

        dialogButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Toast.makeText(MainActivity.this, "Delete reservation :  " + mReservList.get(i).get(KEY_RESID), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Btn DEL: corrispondente all'indice  " +i);
                Log.e(TAG, "Btn DEL: fare post per eliminare reservation id " + mReservList.get(i).get(KEY_RESID));
            }
        });

        dialog.show();
    }

    private void loadListView(List<Reservation> androidList)  {

        Log.e(TAG, "Metodo loadlistview di MainActivity ");

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, mReservList, R.layout.list_item,
                new String[]{KEY_DATE, KEY_HOUR, KEY_RESID, KEY_SPORT, KEY_TYPERES, KEY_COURTID},
                new int[]{R.id.date, R.id.hour, R.id.resid, R.id.sport, R.id.typeres, R.id.courtid});

        Log.e(TAG, "Metodo loadlistview Fine ");

        mListView.setAdapter(adapter);

    }
}



 //   ----------------------------------------------
//    -------------------------------------------------
/*
public class MainActivity extends AppCompatActivity {

    String jsonFromServer;
    ListView listView;
    ArrayList<String> tutorialList = new ArrayList<String>();
    public final static String URL = "http://10.0.2.2:8083/reservations/";
    //public final static String URL = "http://localhost:8083/reservations/";
    //public final static String URL = "http://192.168.1.7:8083/reservations/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchDataTask().execute(URL);

    }


    private class FetchDataTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "con";
        private static final String TAG1 = "DEBUG doInBg:  ";
        //private static final String TAG2 = "DEBUG altro:  ";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Json Data is downloading " );
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();

            String url = URL;

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
         //   Log.e(TAG1, "dopo makeServiceCall(): " + url);

            jsonFromServer = jsonStr;
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                Log.e(TAG1, "Json not null: ");
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG1, "creato jsonobject: "+jsonObj.get("android").toString());
                    //String books = jsonObj.toString();
                } catch (final JSONException e) {
                    Log.e(TAG1, "catch di new JSONobject");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Log.e(TAG1, "return di jsonFromServer" + jsonFromServer);
                return jsonFromServer;
            } else {
                Log.e(TAG1, "Json string null: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display
            //parseJSON(dataFetched);
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
/*
    private void parseJSON(String data){

        try{
            JSONArray jsonMainNode = new JSONArray(data);

            int jsonArrLength = jsonMainNode.length();

            for(int i=0; i < jsonArrLength; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.getString("name");
                tutorialList.add(name);
                String age = jsonChildNode.getString("age");
                tutorialList.add(age);

            }

            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list_view);

            // Define a new Adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, tutorialList);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());

        }
    }
}
*/


