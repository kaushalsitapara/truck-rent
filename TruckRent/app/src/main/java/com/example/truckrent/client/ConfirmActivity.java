package com.example.truckrent.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.truckrent.MainActivity;
import com.example.truckrent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String pAdd,pPin,pCity,dAdd,dPin,dCity;
    String pLat,pLan,dLat,dLan;
    float fareRate = 0;
    float[] results = new float[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent it = getIntent();
        String[] vehical = {"3 Wheeler", "Ace", "8ft Truck"};
        pLan=Double.toString(it.getDoubleExtra("pickLng",0));
        pLat=Double.toString(it.getDoubleExtra("pickLat",0));
        dLan=Double.toString(it.getDoubleExtra("dropLng",0));
        dLat=Double.toString(it.getDoubleExtra("dropLat",0));
        pAdd=it.getStringExtra("pickAdd").replace(", ",",");
        dAdd=it.getStringExtra("dropAdd").replace(", ",",");
        pPin=it.getStringExtra("pickPin");
        dPin=it.getStringExtra("dropPin");
        pCity=it.getStringExtra("pickCity");
        dCity=it.getStringExtra("dropCity");


        Location.distanceBetween(it.getDoubleExtra("pickLat",0), it.getDoubleExtra("pickLng",0),
                it.getDoubleExtra("dropLat",0), it.getDoubleExtra("dropLng",0), results);
        final TextView txtDist =findViewById(R.id.txtDis);
        final Button btTotal =findViewById(R.id.start_transaction);
        btTotal.setOnClickListener(this);
        Spinner spin = (Spinner) findViewById(R.id.vehicalType);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,vehical);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        txtDist.setText(String.valueOf(results[0])+" meters");
        if (ContextCompat.checkSelfPermission(ConfirmActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConfirmActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final  TextView txtFare = findViewById(R.id.totalFare);
        if(position==0)
        {
            fareRate=((results[0]/1000)*150);
            txtFare.setText(fareRate+" Rs");

        }
        if(position==1)
        {
            fareRate=((results[0]/1000)*250);
            txtFare.setText(fareRate+" Rs");

        }
        if(position==2)
        {
            fareRate=((results[0]/1000)*800);
            txtFare.setText(fareRate+" Rs");

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("pLat",pLat);
        data.put("pLog",pLan);
        data.put("dLat",dLat);
        data.put("dLon",dLan);
        data.put("pCity",pCity);
        data.put("dCity",dCity);
        data.put("pAdd",pAdd);
        data.put("dAdd",dAdd);
        data.put("pPin",pPin);
        data.put("dPin",dPin);
        data.put("dist",results[0]);
        data.put("total",fareRate);
        data.put("status","Requested");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd 'at' HH:mm:ss z");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("Request At "+ sdf.format(new Date()).toString(),data);
        db.collection("client").document(user.getUid())
                .update(data1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully written!");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
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
