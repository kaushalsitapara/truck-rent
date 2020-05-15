package com.example.manager.ui.home;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.manager.R;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> fname;
    private ArrayList<String> uid;
    private ArrayList<String> status ;
    private ArrayList<String> pincode;
    private ArrayList<String> vType;

    public MyListAdapter(Activity context, ArrayList<String> fname, ArrayList<String> uid, ArrayList<String> status,ArrayList<String> pincode,ArrayList<String> vType) {
        super(context, R.layout.mylist,fname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.fname=fname;
        this.uid=uid;
        this.status=status;
        this.pincode=pincode;
        this.vType=vType;

    }

    public View getView(int position,View view,ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.mylist, parent, false);
        }


        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtUid =  view.findViewById(R.id.txtUid);
        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        TextView txtPin = (TextView) view.findViewById(R.id.txtPin);
        TextView txtvType = (TextView) view.findViewById(R.id.txtvType);


        txtName.setText("Name: "+fname.get(position));
        txtUid.setText("User Id: "+uid.get(position));
        txtPin.setText("Pincode: "+pincode.get(position));
        txtvType.setText("Vehical Type: "+vType.get(position));
        txtStatus.setText(status.get(position));

        return view;

    };
}
