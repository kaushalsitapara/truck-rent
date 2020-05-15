package com.example.manager.ui.slideshow;

import android.app.Activity;
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
    private ArrayList<String> licNo;
    public MyListAdapter(Activity context, ArrayList<String> fname, ArrayList<String> licNo) {
        super(context, R.layout.mylist1,fname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.fname=fname;
        this.licNo=licNo;

    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.mylist1, parent, false);
        }


        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtUid =  view.findViewById(R.id.txtTotal);

        txtName.setText("Name: "+fname.get(position));
        txtUid.setText("LicNo: "+licNo.get(position));


        return view;

    };
}
