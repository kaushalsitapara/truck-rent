package com.example.driver.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.driver.NavigationActivity;
import com.example.driver.R;
import com.example.driver.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment implements View.OnClickListener {
    double pLat,pLag,dLat,dLag;
    TextView pAdd,pCity,dAdd,dCity;
    EditText eOtp;
    Button pBt,dBt,completeBt;
    String otp, enteredOtp,requestName,clientUid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        pAdd = root.findViewById(R.id.pAddress);
        pCity = root.findViewById(R.id.pCity);
        dAdd = root.findViewById(R.id.dAddress);
        dCity = root.findViewById(R.id.dCity);
        pBt = root.findViewById(R.id.pBt);
        dBt = root.findViewById(R.id.dBt);
        completeBt = root.findViewById(R.id.completeButton);
        eOtp = root.findViewById(R.id.eOtp);
        pBt.setOnClickListener(this);
        dBt.setOnClickListener(this);
        completeBt.setOnClickListener(this);

        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pBt:
                Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+pLat+","+pLag));
                startActivity(intent1);
                break;
            case  R.id.dBt:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+pLat+","+pLag+"&daddr="+dLat+","+dLag));
                startActivity(intent);
                break;
            case  R.id.completeButton:
                enteredOtp=eOtp.getText().toString();
                Log.e("otp"+otp,"Entered Otp"+enteredOtp);
                if(otp.equals(enteredOtp)){
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("client").document(clientUid);
                    docRef
                            .update(

                                    requestName+".status","Complete"


                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error updating document", e);
                                }
                            });
                    DocumentReference docRef1 = db.collection("driver").document(user.getUid());
                    docRef1
                            .update(

                                    "delivery.pickupLat","0",
                                    "delivery.pickupLag","0",
                                    "delivery.dropupLat", "0",
                                    "delivery.dropupLag","0",
                                    "delivery.otp","",
                                    "delivery.clientUid","",
                                    "delivery.request",""
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG1", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG1", "Error updating document", e);
                                }
                            });
                    Intent intent2 = new Intent(getContext(), NavigationActivity.class);
                    startActivity(intent2);

                }
                break;
        }
    }

    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {

        Context context;
        Geocoder geocoder,geocoder1;
        Address myLoc;
        String paddress,pcity,ppincode,daddress,dcity,dpincode;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        public LoadAddressAsyncTask(Context context) {
            this.context = context;
            geocoder=new Geocoder(context);
            geocoder1=new Geocoder(context);
            Log.d("cars", "LoadAddressAsyncTask: "+ Geocoder.isPresent());
        }


        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseUser user = mAuth.getCurrentUser();
            DocumentReference docRef = db.collection("driver").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.get(FieldPath.of("delivery","pickupLat")));
                            pLat= Double.valueOf(document.get(FieldPath.of("delivery","pickupLat")).toString());
                            pLag= Double.valueOf(document.get(FieldPath.of("delivery","pickupLag")).toString());
                            dLat= Double.valueOf(document.get(FieldPath.of("delivery","dropupLat")).toString());
                            dLag= Double.valueOf(document.get(FieldPath.of("delivery","dropupLag")).toString());
                            clientUid= document.get(FieldPath.of("delivery","clientUid")).toString();
                            requestName= document.get(FieldPath.of("delivery","request")).toString();
                            otp=document.get(FieldPath.of("delivery","otp")).toString();
                            Log.e("Coordinates",pLag+" "+pLat+" "+dLag+" "+dLat);
                        } else {
                            Log.d("TAG", "No such document");
                        }
                        try {
                            Log.d("cars", "LoadAddressAsyncTask: ");
                            List<Address> al= geocoder.getFromLocation(pLat,pLag,5);
                            myLoc=al.get(0);
                            /*for(Address a:al){
                                Log.d("cars", "doInBackground: "+a.toString());
                            }*/
                            paddress=myLoc.getAddressLine(0);
                            Log.e("paddress",paddress);
                            pcity=myLoc.getLocality();
                            Log.e("paddress",pcity);
                            ppincode=myLoc.getPostalCode();
                            Log.e("paddress",ppincode);
                            Log.d("cars", "LoadAddressAsyncTask: ");
                            List<Address> al1= geocoder1.getFromLocation(dLat,dLag,5);
                            myLoc=al1.get(0);
                            /*for(Address a:al1){
                                Log.d("cars", "doInBackground: "+a.toString());
                            }*/
                            daddress=myLoc.getAddressLine(0);
                            Log.e("paddress",daddress);
                            dcity=myLoc.getLocality();
                            Log.e("paddress",dcity);
                            dpincode=myLoc.getPostalCode();
                            Log.e("paddress",dpincode);
                            if(paddress!=null && daddress!=null) {
                                Log.e("onPost","Executed ");
                                pAdd.setText(paddress);
                                pCity.setText(pcity + "," + ppincode);
                                dAdd.setText(daddress);
                                dCity.setText(dcity + "," + dpincode);

                            }
                            pBt.setEnabled(true);
                            dBt.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }

}
