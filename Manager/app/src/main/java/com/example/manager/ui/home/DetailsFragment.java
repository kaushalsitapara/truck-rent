package com.example.manager.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailsFragment extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int pos=0;
    Spinner spinner;
    ArrayList<String> drivers = new ArrayList<String>();
    ArrayList<String> driversUid = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    TextView txtData;
    String selectedDriver,selectedUid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_details, container, false);
        pos=getArguments().getInt("pos");
        LoadAddressAsyncTask task=new LoadAddressAsyncTask(getContext());
        task.execute();
        LoadAddressAsyncTask1 task1=new LoadAddressAsyncTask1(getContext());
        task1.execute();
        txtData = root.findViewById(R.id.txtData);
        spinner = (Spinner) root.findViewById(R.id.vType);
        Button bt = root.findViewById(R.id.btAssign);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDriver!=null)
                {
                    Map<String, Object> data = new HashMap<>();
                    data.put("status","Processing");
                    data.put("driver",selectedDriver);
                    Map<String, Object> data1 = new HashMap<>();
                    data1.put(HomeFragment.request1.get(pos),data);
                    DocumentReference docRef = db.collection("client").document(HomeFragment.uid.get(pos));
                    docRef
                            .update(

                                    HomeFragment.request1.get(pos)+".status","Processing",
                                    HomeFragment.request1.get(pos)+".driver",selectedDriver

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


                    DocumentReference docRef1 = db.collection("driver").document(selectedUid);
                    docRef1
                            .update(

                                    "delivery.pickupLat", HomeFragment.pickupLat.get(pos),
                                    "delivery.pickupLag",HomeFragment.pickupLag.get(pos),
                                    "delivery.dropupLat", HomeFragment.dropupLat.get(pos),
                                    "delivery.dropupLag",HomeFragment.dropupLag.get(pos),
                                    "delivery.otp",HomeFragment.otp.get(pos),
                                    "delivery.clientUid",HomeFragment.uid.get(pos),
                                    "delivery.request",HomeFragment.request1.get(pos)
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




                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,new HomeFragment()).commit();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDriver=parent.getItemAtPosition(position).toString();
                    selectedUid=driversUid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(parent.getItemAtPosition(0).toString()!=null)
                {  selectedDriver=parent.getItemAtPosition(0).toString();
                    selectedUid=driversUid.get(0);}
            }
        });
        return root;
    }
    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {

        Context context;


        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }



        @Override
        protected Void doInBackground(Void... voids) {
            DocumentReference docRef = db.collection("client").document(HomeFragment.uid.get(pos));

// Source can be CACHE, SERVER, or DEFAULT.
            Source source = Source.CACHE;

// Get the document, forcing the SDK to use the offline cache
            docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Document found in the offline cache
                        DocumentSnapshot document = task.getResult();
                        Log.d("TAG", "Cached document data: " + document.get(HomeFragment.request1.get(pos)));
                        String data = document.get(HomeFragment.request1.get(pos)).toString();
                        String[] data1=data
                                .replace("{","")
                                .replace("}","")
                                .split(", ");
                        data=String.join("\n",data1);
                        Log.e("Final Data",data);
                        txtData.setText(data);
                    } else {
                        Log.d("TAG", "Cached get failed: ", task.getException());
                    }
                }
            });
            Log.d("Background Task", "Complete");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("TAG", "onPostExecute");

        }
    }
    class LoadAddressAsyncTask1 extends AsyncTask<Void,Void,Void> {

        Context context;


        public LoadAddressAsyncTask1(Context context) {
            this.context = context;

        }



        @Override
        protected Void doInBackground(Void... voids) {
            db.collection("driver")
                    .whereEqualTo("vType",HomeFragment.vType.get(pos))
                    .whereEqualTo(FieldPath.of("location","pincode"), HomeFragment.pincode.get(pos))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.e("Driver Search", document.getId() + " => " + document.getData());
                                    drivers.add(document.get("fname")+"      "+document.get("licNo").toString());
                                    driversUid.add(document.getId());
                                    Log.e("Driver Details",drivers.toString());
                                }
                            } else {
                                Log.d("Driver Search", "Error getting documents: ", task.getException());
                            }
                            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, drivers);
                            spinner.setAdapter(adapter);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        }
                    });

            Log.d("Driver Search", "Complete");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("Driver Search", "onPostExecute");

        }
    }
}
