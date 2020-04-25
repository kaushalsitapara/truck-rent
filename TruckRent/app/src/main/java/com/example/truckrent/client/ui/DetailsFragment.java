package com.example.truckrent.client.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.truckrent.R;
import com.example.truckrent.client.NavigationActivity;

import javax.xml.namespace.NamespaceContext;


public class DetailsFragment extends Fragment {
    int pos=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        pos=getArguments().getInt("pos");
        Log.e("Position",pos+"");
        TextView txtFrom = root.findViewById(R.id.txtFrom);
        TextView txtTo = root.findViewById(R.id.txtTo);
        TextView txtDist = root.findViewById(R.id.txtDist);
        TextView txtFare = root.findViewById(R.id.txtFare);
        TextView txtStatus = root.findViewById(R.id.txtStatus);
        txtFrom.setText("From: "+ NavigationActivity.pickAddress.get(pos));
        txtTo.setText("To: "+ NavigationActivity.dropAddress.get(pos));
        txtDist.setText("Distance: "+ NavigationActivity.totalDist.get(pos));
        txtFare.setText("Total Fare: "+ NavigationActivity.totalFare.get(pos));
        txtStatus.setText("Status: "+NavigationActivity.statusReq.get(pos));
        return root;

    }
}
