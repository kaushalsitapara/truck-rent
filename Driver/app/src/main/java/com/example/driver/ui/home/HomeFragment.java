package com.example.driver.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.driver.MapsActivity;
import com.example.driver.NavigationActivity;
import com.example.driver.R;
import com.example.driver.utils.PermissionsUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView;
    double lng ,lat;
    String address,pincode,city;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        latTextView = root.findViewById(R.id.lat);
        lonTextView = root.findViewById(R.id.lon);
        enableMyLocation(true);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        return root;
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();

                                } else {
                                    lat=location.getLatitude();
                                    lng=location.getLongitude();
                                    latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");
                                    LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
                                    task1.execute();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }



    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat=mLastLocation.getLatitude();
            lng=mLastLocation.getLongitude();
            latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");
            LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
            task1.execute();
        }
    };
    private void enableMyLocation(boolean b) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionsUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {

        Context context;
        Geocoder geocoder;
        Address myLoc;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        public LoadAddressAsyncTask(Context context) {
            this.context = context;
            geocoder=new Geocoder(context);
            Log.d("cars", "LoadAddressAsyncTask: "+ Geocoder.isPresent());
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("cars", "LoadAddressAsyncTask: ");
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

            if(address!=null) {
                lonTextView.setText(address);
                latTextView.setText(city + "," + pincode);
                FirebaseUser user = mAuth.getCurrentUser();
                Map<String, Object> data = new HashMap<>();
                data.put("lat",lat+"");
                data.put("lng",lng+"");
                data.put("pincode",pincode+"");
                data.put("city",city+"");
                data.put("address",address+"");
                Map<String, Object> data1 = new HashMap<>();
                data1.put("location",data);
                db.collection("driver").document(user.getUid())
                        .update(data1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Success", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error", "Error writing document", e);
                            }
                        });
            }
        }
    }
}
