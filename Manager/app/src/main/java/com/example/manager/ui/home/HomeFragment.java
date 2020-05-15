package com.example.manager.ui.home;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ListView list;
    public static ArrayList<String> request1 = new ArrayList<String>();
    public static ArrayList<String> status = new ArrayList<String>();
    public static ArrayList<String> fname = new ArrayList<String>();
    public static ArrayList<String> uid = new ArrayList<String>();
    public static ArrayList<String> pincode = new ArrayList<String>();
    public static ArrayList<String> vType = new ArrayList<String>();
    public static ArrayList<String> pickupLat = new ArrayList<String>();
    public static ArrayList<String> pickupLag = new ArrayList<String>();
    public static ArrayList<String> dropupLat = new ArrayList<String>();
    public static ArrayList<String> dropupLag = new ArrayList<String>();
    public static ArrayList<String> otp = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadAddressAsyncTask task=new LoadAddressAsyncTask(getContext());
        task.execute();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        list=(ListView)root.findViewById(R.id.list);
        status.clear();
        request1.clear();
        fname.clear();
        uid.clear();
        pincode.clear();
        vType.clear();
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
    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {

        Context context;


        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }



        @Override
        protected Void doInBackground(Void... voids) {
            db.collection("client")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //FieldPath fieldPath = FieldPath.of("","pLog");
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    String data = document.getData().toString();
                                    String[] data1=data
                                            .replace("{","")
                                            .replace("}","")
                                            .split(", ");
                                    ArrayList<String> requests = new ArrayList<String>();
                                    for (int i = 0; i < data1.length; i++)
                                    {
                                        data1[i]=data1[i].replaceAll("=.+", "");
                                        if(data1[i].contains("Request"))
                                        {
                                            requests.add(data1[i]);
                                        }
                                    }
                                    //for (int i = 0; i < data1.length; i++)

                                    Log.e("Data",requests.toString());
                                    if(!requests.isEmpty()){
                                        for (int i = 0; i < requests.size(); i++) {
                                            FieldPath fieldPath = FieldPath.of(requests.get(i), "status");
                                            Log.d("TAG", document.getId() + " => " +requests.get(i)+ document.get(fieldPath));
                                            if(document.get(fieldPath).toString().equals("Requested"))
                                            {
                                                request1.add(requests.get(i));
                                                uid.add(document.getId());
                                                pincode.add(document.get(FieldPath.of(requests.get(i), "pPin")).toString());
                                                vType.add(document.get(FieldPath.of(requests.get(i), "vType")).toString());
                                                fname.add(document.get("fname").toString());
                                                status.add(document.get(fieldPath).toString());
                                                pickupLat.add(document.get(FieldPath.of(requests.get(i), "pLat")).toString());
                                                dropupLat.add(document.get(FieldPath.of(requests.get(i), "dLat")).toString());
                                                pickupLag.add(document.get(FieldPath.of(requests.get(i), "pLog")).toString());
                                                dropupLag.add(document.get(FieldPath.of(requests.get(i), "dLon")).toString());
                                                otp.add(document.get(FieldPath.of(requests.get(i), "otp")).toString());

                                            }
                                        }
                                    }



                                }
                                MyListAdapter myListAdapter = new MyListAdapter(getActivity(),fname,uid,status,pincode,vType);
                                list.setAdapter(myListAdapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
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
}
