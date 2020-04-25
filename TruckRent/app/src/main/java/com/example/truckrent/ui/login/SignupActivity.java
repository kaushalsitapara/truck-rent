package com.example.truckrent.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truckrent.MainActivity;
import com.example.truckrent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final Button regBt = findViewById(R.id.login);
        final TextView logBt = findViewById(R.id.txt1);
        logBt.setOnClickListener(this);
        regBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt1:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.login:
                mAuth = FirebaseAuth.getInstance();
                final EditText email = findViewById(R.id.username);
                final EditText password = findViewById(R.id.password);
                EditText cpassworf = findViewById(R.id.cpassword);
                final EditText mobile = findViewById(R.id.mobile);
                final EditText fname = findViewById(R.id.fname);
                final EditText lname = findViewById(R.id.lname);
                CollectionReference mobileCheck = db.collection("client");

                Query query = mobileCheck.whereEqualTo("mobile",mobile.getText().toString());
                if(cpassworf.getText().toString().equals(password.getText().toString()) && !query.equals(mobile.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SignUpActivity", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("fname",fname.getText().toString());
                                        data.put("lname",lname.getText().toString());
                                        data.put("mobile",mobile.getText().toString());
                                        data.put("password",password.getText().toString());
                                        data.put("email",email.getText().toString());
                                        db.collection("client").document(user.getUid())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Success", "DocumentSnapshot successfully written!");
                                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                                        startActivity(i);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Error", "Error writing document", e);
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }
                break;




        }

    }
}
