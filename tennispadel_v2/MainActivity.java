package com.example.tennispadel_v2;

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

import com.example.tennispadel_v2.model.Reservation;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoadJSONTask.Listener, DeleteTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;


    public static final String URL = "http://10.0.2.2:8080/api/v1/reservations/"; //TODO: controllare che l'IP funzioni


    private List<HashMap<String, String>> mReservList = new ArrayList<>();

    private static final String TAG = "MainActivity:  ";

    private static final String KEY_DATE = "date";
    //private static final String KEY_HOUR = "hour";
    private static final String KEY_RESID = "resid";
    private static final String KEY_SPORT = "sport";
    private static final String KEY_TYPERES = "type_res";
    private static final String KEY_COURTID = "courtid";

    Button b1 ; //bottone per aprire activity per creare una nuova reservation

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
    public void onResume(){
        super.onResume();
        Log.e(TAG, "Metodo onResume di MainActivity ");
        // chiamare metodo asincrono per ricaricare la pagina
        new LoadJSONTask(MainActivity.this).execute(URL);

    }

    @Override
    public void onLoaded(List<Reservation> reservationsList) {

        Log.e(TAG, "Metodo onLoaded di MainActivity "+reservationsList.size());
        //       Toast.makeText(this, "onloaded" + reservationsList.size(), Toast.LENGTH_SHORT).show();
        mReservList.clear();
        for (Reservation reserv : reservationsList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_DATE, reserv.getDate());
            //map.put(KEY_HOUR, reserv.getHour());
            map.put(KEY_RESID, reserv.getResid());
            map.put(KEY_SPORT, reserv.getSport());
            map.put(KEY_TYPERES, reserv.getType_res());
            map.put(KEY_COURTID, reserv.getCourtid());

            mReservList.add(map);
            //          Toast.makeText(this, reserv.getName(), Toast.LENGTH_SHORT).show();
        }
        loadListView(reservationsList);
    }

    @Override
    public void onError() {

        Log.e(TAG, "Metodo onerror di MainActivity ");

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //Toast.makeText(this, "Elemento selezionato numero:  " + i, Toast.LENGTH_LONG).show();
        Log.e(TAG, "metodo onitemclick");

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.testoDate);
        text.setText(mReservList.get(i).get(KEY_DATE));

        TextView text1 = (TextView) dialog.findViewById(R.id.testoSport);
        //text.setText("selezionata: "+i);
        text1.setText(mReservList.get(i).get(KEY_SPORT));

        TextView text2 = (TextView) dialog.findViewById(R.id.testoIdRes);
        text2.setText(mReservList.get(i).get(KEY_RESID));

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonBack);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Btn BACK");
                dialog.dismiss();

                // chiamare metodo asincrono per ricaricare la pagina
                new LoadJSONTask(MainActivity.this).execute(URL);
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

                String delreservURL = URL + mReservList.get(i).get(KEY_RESID);
                Log.e(TAG, "url delete reserv  " + delreservURL);
                new DeleteTask().execute(delreservURL);
            }
        });

        dialog.show();
    }

    private void loadListView(List<Reservation> reservationsList)  {

        Log.e(TAG, "Metodo loadlistview di MainActivity ");

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, mReservList, R.layout.list_item,
                new String[]{KEY_DATE, KEY_RESID, KEY_SPORT, KEY_TYPERES, KEY_COURTID},
                new int[]{R.id.date, R.id.resid, R.id.sport, R.id.typeres, R.id.courtid});

        Log.e(TAG, "Metodo loadlistview Fine ");

        mListView.setAdapter(adapter);

    }
}





