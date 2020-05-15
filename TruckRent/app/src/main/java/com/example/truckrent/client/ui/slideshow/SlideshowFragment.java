package com.example.truckrent.client.ui.slideshow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.truckrent.R;
import com.example.truckrent.client.NavigationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    ListView list;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> pickAddress = new ArrayList<String>();
    public static ArrayList<String> dropAddress = new ArrayList<String>();
    public static ArrayList<String> totalDist = new ArrayList<String>();
    public static ArrayList<String> statusReq = new ArrayList<String>();
    public static ArrayList<String> driverDetails = new ArrayList<String>();
    public static ArrayList<String> request1 = new ArrayList<String>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Completed Requests");
        LoadAddressAsyncTask task=new LoadAddressAsyncTask(getContext());
        task.execute();
        list=(ListView)root.findViewById(R.id.list1);


        /*

        */
        return root;
    }

    class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;


        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }


        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseUser user = mAuth.getCurrentUser();
            DocumentReference docRef = db.collection("client").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("User", "DocumentSnapshot data: " + document.getData());
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
                                    if(document.get(fieldPath).toString().equals("Complete"))
                                    {
                                        request1.add(requests.get(i));
                                        statusReq.add(document.get(fieldPath).toString());
                                        pickAddress.add(document.get(FieldPath.of(requests.get(i), "pAdd")).toString());
                                        dropAddress.add(document.get(FieldPath.of(requests.get(i), "dAdd")).toString());
                                        totalDist.add(document.get(FieldPath.of(requests.get(i), "total")).toString());
                                        driverDetails.add(document.get(FieldPath.of(requests.get(i), "driver")).toString());

                                    }
                                }
                            }
                        } else {
                            Log.d("No data", "No such document");
                        }
                        MyListAdapter adapter =new MyListAdapter(getActivity(), pickAddress, dropAddress,statusReq,totalDist,driverDetails );
                        list.setAdapter(adapter);
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }

            });


            Log.e("List","calllllllllllllllllllllllllllllllllllllllllllllled");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }
}
