package com.example.manager.ui.slideshow;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    ListView list;
    String driverName;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> fname = new ArrayList<String>();
    public static ArrayList<String> licNo = new ArrayList<String>();

    public static ArrayList<String> documentId = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        LoadAddressAsyncTask task=new LoadAddressAsyncTask(getContext());
        task.execute();
        list=(ListView)root.findViewById(R.id.list);
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
    class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;


        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }


        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseUser user = mAuth.getCurrentUser();

            db.collection("driver")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    fname.add(document.get("fname").toString());
                                    licNo.add(document.get("licNo").toString());
                                    documentId.add(document.getId());
                                    MyListAdapter adapptet = new MyListAdapter(getActivity(),fname,licNo);
                                    list.setAdapter(adapptet);
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
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
