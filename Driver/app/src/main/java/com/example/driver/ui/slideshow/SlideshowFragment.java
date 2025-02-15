package com.example.driver.ui.slideshow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    String fname,licNo;
    TextView txtName, txtlicNo, txtTotal;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> clientDocumentId = new ArrayList<String>();
    double total = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        txtName= root.findViewById(R.id.txtName);
        txtlicNo= root.findViewById(R.id.txtlicNo);
        txtTotal= root.findViewById(R.id.tctTotal);
        LoadAddressAsyncTask task = new LoadAddressAsyncTask(getContext());
        task.execute();

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
            final DocumentReference docRef = db.collection("driver").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            fname=document.get("fname").toString();
                            licNo=document.get("licNo").toString();
                            txtName.setText(fname);
                            txtlicNo.setText(licNo);
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
            db.collection("client")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    clientDocumentId.add(document.getId());
                                }
                                for (int i = 0; i < clientDocumentId.size(); i++) {
                                    DocumentReference docRef = db.collection("client").document(clientDocumentId.get(i));
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d("User", "DocumentSnapshot data: " + document.getData());
                                                    String data = document.getData().toString();
                                                    String[] data1 = data
                                                            .replace("{", "")
                                                            .replace("}", "")
                                                            .split(", ");
                                                    ArrayList<String> requests = new ArrayList<String>();
                                                    for (int i = 0; i < data1.length; i++) {
                                                        data1[i] = data1[i].replaceAll("=.+", "");
                                                        if (data1[i].contains("Request")) {
                                                            requests.add(data1[i]);
                                                        }
                                                    }
                                                    //for (int i = 0; i < data1.length; i++)

                                                    Log.e("Data", requests.toString());
                                                    if (!requests.isEmpty()) {
                                                        for (int i = 0; i < requests.size(); i++) {
                                                            FieldPath fieldPath = FieldPath.of(requests.get(i), "status");
                                                            Log.d("TAG", document.getId() + " => " + requests.get(i) + document.get(fieldPath));
                                                            if (document.get(fieldPath).toString().equals("Complete")) {
                                                                String temp = document.get(FieldPath.of(requests.get(i), "driver")).toString();
                                                                Log.e("Total from db", temp);

                                                                if (temp.equals(fname + "      " + licNo)) {
                                                                    Log.e("Total from db", document.get(FieldPath.of(requests.get(i), "total")).toString());
                                                                    total = total + Double.valueOf(document.get(FieldPath.of(requests.get(i), "total")).toString());
                                                                }
                                                                Log.e("Total", total + "");
                                                                txtTotal.setText(total + "");

                                                               /* request1.add(requests.get(i));
                                                                statusReq.add(document.get(fieldPath).toString());
                                                                pickAddress.add(document.get(FieldPath.of(requests.get(i), "pAdd")).toString());
                                                                dropAddress.add(document.get(FieldPath.of(requests.get(i), "dAdd")).toString());
                                                                totalDist.add(document.get(FieldPath.of(requests.get(i), "total")).toString());
                                                                driverDetails.add(document.get(FieldPath.of(requests.get(i), "driver")).toString());*/

                                                            }
                                                        }
                                                    }
                                                } else {
                                                    Log.d("No data", "No such document");
                                                }
                                            }
                                        }

                                    });
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });


            Log.e("List", "calllllllllllllllllllllllllllllllllllllllllllllled");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }
}

