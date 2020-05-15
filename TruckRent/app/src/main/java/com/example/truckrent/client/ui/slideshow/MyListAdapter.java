package com.example.truckrent.client.ui.slideshow;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.truckrent.R;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> pickAddress;
    private ArrayList<String> dropAddress;
    private ArrayList<String> statusReq;
    private ArrayList<String> totalDist;
    private ArrayList<String> driverDetails;


    public MyListAdapter(Activity context, ArrayList<String> pickAddress, ArrayList<String> dropAddress, ArrayList<String> statusReq, ArrayList<String> totalDist,ArrayList<String> driverDetails) {
        super(context, R.layout.mylist1,pickAddress);
        // TODO Auto-generated constructor stub
        Log.e("Const",pickAddress.toString());
        this.context=context;
        this.pickAddress=pickAddress;
        this.dropAddress=dropAddress;
        this.statusReq=statusReq;
        this.driverDetails=driverDetails;
        this.totalDist=totalDist;

    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.mylist1, parent, false);
        }


        TextView txtFrom = (TextView) view.findViewById(R.id.txtFrom);
        TextView txtdriver = (TextView) view.findViewById(R.id.txtDriver);
        TextView txttotal = (TextView) view.findViewById(R.id.txtTotal);
        TextView txtTo =  view.findViewById(R.id.txtTo);
        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        Log.e("Const",pickAddress.toString());
        txtdriver.setText("Driver Details: "+driverDetails.get(position));
        txttotal.setText("Total Bill: "+ totalDist.get(position));
        txtFrom.setText("From: "+pickAddress.get(position));
        txtTo.setText("To: "+dropAddress.get(position));
        txtStatus.setText(statusReq.get(position));
        return view;

    };
}
