package com.example.truckrent.client.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.truckrent.R;
import com.example.truckrent.client.MapsActivity;
import com.example.truckrent.client.NavigationActivity;
import com.example.truckrent.client.ui.DetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment  {


    ListView list;
    MyListAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        ((NavigationActivity)getActivity()).getSupportActionBar().setTitle("Recent Requests");
        list=(ListView)root.findViewById(R.id.list);
        adapter=new MyListAdapter(getActivity(), NavigationActivity.pickAddress, NavigationActivity.dropAddress,NavigationActivity.statusReq);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsFragment ldf = new DetailsFragment ();
                Bundle args = new Bundle();
                args.putInt("pos", position);
                ldf.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ldf)
                        .commit();
            }
        });




        return root;
    }



}
