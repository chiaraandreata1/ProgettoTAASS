package com.example.provatennispadel;
/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Customadapter extends BaseAdapter{

    ArrayList<HashMap<String, String>> oslist;
    Context context;
    private Button btnDelete;
    private Button btnEdit;
    LayoutInflater lif;

    public Customadapter(ArrayList<HashMap<String, String>> oslist, Context context) {
        context = context;
   //     oslist = oslist;
        this.oslist = oslist;
        lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("oslist.size() = "+oslist.size());
        return oslist.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        // TODO Auto-generated method stub
        return (Map.Entry) oslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    //    LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = lif.inflate(R.layout.list_item, null);

        TextView txt_Name = (TextView) convertView.findViewById(R.id.api);
        System.out.println("TXT NAME already works!!"+txt_Name.toString());
        Button btnEdit = (Button) convertView.findViewById(R.id.edit_btn);

        btnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("EDIT already works!!"+v.getId());
                //Here perform the action you want
            }

        });
        Button btnDel = (Button) convertView.findViewById(R.id.delete_btn);

        btnDel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("DELETE already works!!"+v.getId());
           //     System.out.println("DELETE"+v.getId());
                //Here perform the action you want
            }

        });

        return convertView;
    }

}


 */