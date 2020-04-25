package com.example.truckrent.client.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.truckrent.R;
import com.example.truckrent.client.ConfirmActivity;
import com.example.truckrent.client.MapsActivity;
import com.example.truckrent.client.NavigationActivity;
import com.example.truckrent.client.ui.gallery.MyListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements View.OnClickListener {
    double lng,lng1;
    double lat,lat1;
    ImageView mapSnapshot,mapSnapshot1;
    String address,address1;
    String city,city1;
    String pincode,pincode1;
    ProgressBar pb_addr,pb_addr1;
    TextView mCity;
    TextView mLocation;
    TextView mCity1;
    TextView mLocation1;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> pickAddress = new ArrayList<String>();
    ArrayList<String> dropAddress = new ArrayList<String>();
    ArrayList<String> pickCity = new ArrayList<String>();
    ArrayList<String> dropCity = new ArrayList<String>();
    ArrayList<String> pickPin = new ArrayList<String>();
    ArrayList<String> dropPin = new ArrayList<String>();
    ArrayList<String> pickLog = new ArrayList<String>();
    ArrayList<String> dropLog = new ArrayList<String>();
    ArrayList<String> pickLat = new ArrayList<String>();
    ArrayList<String> dropLat = new ArrayList<String>();
    ArrayList<String> totalFare = new ArrayList<String>();
    ArrayList<String> totalDist = new ArrayList<String>();
    ArrayList<String> statusReq = new ArrayList<String>();
    String[] splitString={};

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10&&resultCode==1){
            if(data.getStringExtra("button").equals("1")){
            lng=data.getDoubleExtra("longitude",0);
            lat=data.getDoubleExtra("latitude",0);
            Log.d("cars", "onActivityResult: result recieved from map");
            byte arr[]=data.getByteArrayExtra("byteArray");
            Bitmap bitmap = BitmapFactory.decodeByteArray(arr,0,arr.length);
            mapSnapshot.setImageBitmap(bitmap);
            LoadAddressAsyncTask task=new LoadAddressAsyncTask(getContext());
            task.execute();}
            if(data.getStringExtra("button").equals("2")){
                lng1=data.getDoubleExtra("longitude",0);
                lat1=data.getDoubleExtra("latitude",0);
                Log.d("cars", "onActivityResult: result recieved from map");
                byte arr[]=data.getByteArrayExtra("byteArray");
                Bitmap bitmap = BitmapFactory.decodeByteArray(arr,0,arr.length);
                mapSnapshot1.setImageBitmap(bitmap);
                LoadAddressAsyncTask1 task1=new LoadAddressAsyncTask1(getContext());
                task1.execute();}
        }

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Button bt1 = root.findViewById(R.id.selectLocation);
        final Button bt2 = root.findViewById(R.id.selectDropLocation);
        final Button bt3 = root.findViewById(R.id.sendData);
        ((NavigationActivity)getActivity()).getSupportActionBar().setTitle("Book Truck");
        mCity=root.findViewById(R.id.city);
        mLocation=root.findViewById(R.id.location);
        pb_addr=root.findViewById(R.id.pb_address);
        mapSnapshot=root.findViewById(R.id.map_snap);

        mCity1=root.findViewById(R.id.city1);
        mLocation1=root.findViewById(R.id.location1);
        pb_addr1=root.findViewById(R.id.pb_address1);
        mapSnapshot1=root.findViewById(R.id.map_snap1);


        bt1.setOnClickListener(this);

        bt2.setOnClickListener(this);

        bt3.setOnClickListener(this);
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("client").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Data fetch", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = new HashMap<>();
                        data=document.getData();
                        for(Object value: data.values()){
                            if(value.toString().contains("{")){

                                splitString=value.toString()
                                        .replace("{","")
                                        .replace("}","")

                                        .split(", ");

                                for(int i = splitString.length-1;i>=0;i--)
                                {
                                    System.out.println(i);
                                    if(splitString[i].contains("pLog")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        pickLog.add(splitString[i]);
                                        Log.d("pLog",pickLog.toString());
                                    }
                                    else if(splitString[i].contains("pAdd")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        pickAddress.add(splitString[i]);
                                        Log.d("pickAdd",pickAddress.toString());
                                    }
                                    else if(splitString[i].contains("dLon")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        dropLog.add(splitString[i]);
                                        Log.d("dLog",dropLog.toString());
                                    }
                                    else if(splitString[i].contains("dist")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        totalDist.add(splitString[i]);
                                        Log.d("dist",totalDist.toString());
                                    }
                                    else if(splitString[i].contains("dCity")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        dropCity.add(splitString[i]);
                                        Log.d("dCity",dropCity.toString());
                                    }
                                    else if(splitString[i].contains("pPin")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        pickPin.add(splitString[i]);
                                        Log.d("ppin",pickPin.toString());
                                    }
                                    else if(splitString[i].contains("pCity")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        pickCity.add(splitString[i]);
                                        Log.d("pcity",pickCity.toString());
                                    }
                                    else if(splitString[i].contains("total")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        totalFare.add(splitString[i]);
                                        Log.d("total",totalFare.toString());
                                    }
                                    else if(splitString[i].contains("dAdd")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        dropAddress.add(splitString[i]);
                                        Log.d("dAdd",dropAddress.toString());
                                    }
                                    else if(splitString[i].contains("dLat")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        dropLat.add(splitString[i]);
                                        Log.d("dlat",dropLat.toString());
                                    }
                                    else if(splitString[i].contains("pLat")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        pickLat.add(splitString[i]);
                                        Log.d("plat",pickLat.toString());
                                    }
                                    else if(splitString[i].contains("status")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        statusReq.add(splitString[i]);
                                        Log.d("status",statusReq.toString());
                                    }
                                    else if(splitString[i].contains("dPin")) {
                                        splitString[i] = splitString[i].replaceAll(".+=", "");
                                        dropPin.add(splitString[i]);
                                        Log.d("dPin",dropPin.toString());
                                    }

                                }
                                splitString=null;


                            }

                        }
                        NavigationActivity.pickAddress = pickAddress;
                        NavigationActivity.pickCity = pickCity;
                        NavigationActivity.pickLat = pickLat;
                        NavigationActivity.pickLog = pickLog;
                        NavigationActivity.pickPin = pickPin;
                        NavigationActivity.dropAddress = dropAddress;
                        NavigationActivity.dropCity = dropCity;
                        NavigationActivity.dropLat = dropLat;
                        NavigationActivity.dropLog = dropLog;
                        NavigationActivity.dropPin = dropPin;
                        NavigationActivity.statusReq = statusReq ;
                        NavigationActivity.totalDist =totalDist ;
                        NavigationActivity.totalFare = totalFare;
                    } else {
                        Log.d("Data fetch", "No such document");
                    }
                } else {
                    Log.d("Data not fetch", "get failed with ", task.getException());
                }
            }
        });

        return root;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.selectLocation:
                Intent item = new Intent(getActivity(),MapsActivity.class);
                item.putExtra("button","1");
                startActivityForResult(item,10);
                break;
            case R.id.selectDropLocation:
                Intent i = new Intent(getActivity(),MapsActivity.class);
                i.putExtra("button","2");
                startActivityForResult(i,10);
                break;
            case R.id.sendData:
                Intent it = new Intent(getActivity(), ConfirmActivity.class);
                it.putExtra("pickLng",lng);
                it.putExtra("pickLat",lat);
                it.putExtra("dropLng",lng1);
                it.putExtra("dropLat",lat1);
                it.putExtra("pickAdd",address);
                it.putExtra("dropAdd",address1);
                it.putExtra("pickPin",pincode);
                it.putExtra("dropPin",pincode1);
                it.putExtra("pickCity",city);
                it.putExtra("dropCity",city1);
                startActivity(it);

        }
    }
    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {

        Context context;
        Geocoder geocoder;
        Address myLoc;

        public LoadAddressAsyncTask(Context context) {
            this.context = context;
            geocoder=new Geocoder(context);
            Log.d("cars", "LoadAddressAsyncTask: "+ Geocoder.isPresent());
        }

        @Override
        protected void onPreExecute() {
            pb_addr.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("cars", "LoadAddressAsyncTask: "+ lat+" "+lng);
                List<Address> al= geocoder.getFromLocation(lat,lng,5);
                myLoc=al.get(0);
                for(Address a:al){
                    Log.d("cars", "doInBackground: "+a.toString());
                }
                address=myLoc.getAddressLine(0);
                city=myLoc.getLocality();
                pincode=myLoc.getPostalCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pb_addr.setVisibility(View.GONE);
            if(address!=null) {
                mLocation.setText(address);
                mCity.setText(city + "," + pincode);
                mapSnapshot.setVisibility(View.VISIBLE);
            }
        }
    }
    class LoadAddressAsyncTask1 extends AsyncTask<Void,Void,Void> {

        Context context;
        Geocoder geocoder;
        Address myLoc;

        public LoadAddressAsyncTask1(Context context) {
            this.context = context;
            geocoder=new Geocoder(context);
            Log.d("cars", "LoadAddressAsyncTask: "+ Geocoder.isPresent());
        }

        @Override
        protected void onPreExecute() {
            pb_addr1.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("cars", "LoadAddressAsyncTask: "+ lat1+" "+lng1);
                List<Address> al= geocoder.getFromLocation(lat1,lng1,5);
                myLoc=al.get(0);
                for(Address a:al){
                    Log.d("cars", "doInBackground: "+a.toString());
                }
                address1=myLoc.getAddressLine(0);
                city1=myLoc.getLocality();
                pincode1=myLoc.getPostalCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pb_addr1.setVisibility(View.GONE);
            if(address1!=null) {
                mLocation1.setText(address1);
                mCity1.setText(city1 + "," + pincode1);
                mapSnapshot1.setVisibility(View.VISIBLE);
            }
        }
    }
}
