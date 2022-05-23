package com.example.tennispadel_v2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tennispadel_v2.model.FreeCourt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Activity2 extends AppCompatActivity implements FreeCourtsTask.Listener1,  AdapterView.OnItemClickListener{
        //implements AdapterView.OnItemClickListener {
    private static final String TAG = "ACTIVITY2:  ";
    public static final String URL = "http://10.0.2.2:8080/api/v1/reservations/";

    private ListView mListView1;

    private List<HashMap<String, String>> mFreeCourtsList = new ArrayList<>();
    private static final String KEY_COU_ID = "courtid";
    private static final String KEY_COU_SPORT = "courtsport";


    Button btnClose;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    DatePicker picker;
    EditText hourInput;
    EditText playerInput1;
    EditText playerInput2;
    EditText playerInput3;
    EditText playerInput4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "Metodo onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        addListenerButton();

        picker=(DatePicker)findViewById(R.id.datePicker);
        btnClose = findViewById(R.id.btnClose);
        hourInput= (EditText)findViewById(R.id.txtHour);
        playerInput1= (EditText)findViewById(R.id.txtPlayer1);
        playerInput2= (EditText)findViewById(R.id.txtPlayer2);
        playerInput3= (EditText)findViewById(R.id.txtPlayer3);
        playerInput4= (EditText)findViewById(R.id.txtPlayer4);


        mListView1 = (ListView) findViewById(R.id.list_view1);
        mListView1.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        btnClose.setOnClickListener(  //bottone per chiudere l'activity
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void addListenerButton() {
         Log.e(TAG, "Metodo addListenerButton");
        radioGroup = findViewById(R.id.radioSport);
        button = findViewById(R.id.btnDisplay);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Metodo onClick  di addListenerButton");
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                Boolean paramOk = checkHour(hourInput.getText());
                if (paramOk) {
                    //Toast.makeText(Activity2.this, "Sport: " + radioButton.getText() + "  Date: " + getCurrentDate() + " Hour: " + hourInput.getText() + " OK: " + paramOk.toString(), Toast.LENGTH_SHORT).show();
                    String freecourtsURL = URL +"courts/date/"+ getCurrentDate() +"/hour/"+hourInput.getText() +"/sport/"+ radioButton.getText() ;
                    Log.e(TAG, "freecourtsURL=  " + freecourtsURL);
                    new FreeCourtsTask(Activity2.this).execute(freecourtsURL);
                                    }
                else{
                    Toast.makeText(Activity2.this, "Ora non corretta! L'orario di apertura è 9:00-21:00 ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
/*
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }*/

    public String getCurrentDate(){
         Log.e(TAG, "Metodo getCurrentDate");
        StringBuilder builder=new StringBuilder();
        builder.append(picker.getDayOfMonth()+"-");
        builder.append((picker.getMonth() + 1)+"-");//month is 0 based
        builder.append(picker.getYear());
        return builder.toString();
    }

    public Boolean checkHour(Editable hourInput){
         Log.e(TAG, "Metodo checkHour");
        int i = Integer. parseInt(String.valueOf(hourInput));
        if (i>8 && i<22){
            //Toast.makeText(Activity2.this,"Giusto: " + i,Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            //Toast.makeText(Activity2.this,"Sbagliato: " + i,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
        public void onLoaded(List<FreeCourt> courtList) {
        Log.e(TAG, "Metodo onLoaded  ");
         //       Toast.makeText(this, "onloaded" + androidList.size(), Toast.LENGTH_SHORT).show();
        mFreeCourtsList.clear();
        for (FreeCourt campo : courtList) {
             HashMap<String, String> courtmap = new HashMap<>();
             courtmap.put(KEY_COU_ID, campo.getCourtid());
             courtmap.put(KEY_COU_SPORT, campo.getCourtsport());
             mFreeCourtsList.add(courtmap);
        }
        loadListView(courtList);
    }

    @Override
    public void onError() {
         Log.e(TAG, "Metodo onerror");
         Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    private void loadListView(List<FreeCourt> courtList)  {
         Log.e(TAG, "Metodo loadListView");
        SimpleAdapter adapter1 = new SimpleAdapter(Activity2.this, mFreeCourtsList, R.layout.court_item,
                   new String[]{KEY_COU_ID, KEY_COU_SPORT},
                   new int[]{R.id.id_court, R.id.id_sport});

        mListView1.setAdapter(adapter1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG, "metodo onitemclick");
        //Toast.makeText(this, "Selezionato campo" + mFreeCourtsList.get(i).get(KEY_COU_ID) + " da " + mFreeCourtsList.get(i).get(KEY_COU_SPORT), Toast.LENGTH_LONG).show();
        //TODO da implementare la post per confermare la prenotazione
        //aprire dialog per completare reservation
            final Dialog dialog = new Dialog(Activity2.this);
            dialog.setContentView(R.layout.create_dialog);
            Log.e(TAG, "aperto dialog per fare post new reservation");

            TextView text = (TextView) dialog.findViewById(R.id.createDate);
            text.setText(getCurrentDate());
            TextView text1 = (TextView) dialog.findViewById(R.id.createHour);
            text1.setText(hourInput.getText());
            TextView text2 = (TextView) dialog.findViewById(R.id.createSport);
            text2.setText(mFreeCourtsList.get(i).get(KEY_COU_SPORT));
            TextView text3 = (TextView) dialog.findViewById(R.id.createCourtNumber);
            text3.setText(mFreeCourtsList.get(i).get(KEY_COU_ID));

           Button dialogButtonBack = (Button) dialog.findViewById(R.id.createButtonBack);
            // if button is clicked, close the custom dialog
            dialogButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button dialogButtonCreate = (Button) dialog.findViewById(R.id.createReservButton);
            // if button is clicked, close the custom dialog
            dialogButtonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "POST "+getCurrentDate()+" - "+hourInput.getText()+" - "+mFreeCourtsList.get(i).get(KEY_COU_SPORT) +" - "+mFreeCourtsList.get(i).get(KEY_COU_ID));
                    Log.e(TAG, "Giocatori " + playerInput1.getText() +" - "+playerInput2.getText()+" - "+playerInput3.getText()+" - "+playerInput4.getText());
                    String createURL = URL+"create";
                    new CreateTask(Activity2.this).execute(createURL,getCurrentDate(),hourInput.getText().toString(),mFreeCourtsList.get(i).get(KEY_COU_SPORT),mFreeCourtsList.get(i).get(KEY_COU_ID).toString(),playerInput1.getText().toString(),playerInput2.getText().toString(),playerInput3.getText().toString(),playerInput4.getText().toString() );

                    dialog.dismiss(); //per chiudere dialog
                    finish(); //per chiudere l'activity
                }
            });

            dialog.show();
    }

}
         