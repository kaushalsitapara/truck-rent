package com.example.manager.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.manager.MainActivity;
import com.example.manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextView txt1 = findViewById(R.id.txt1);
        final Button button = findViewById(R.id.login);
        txt1.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt1:
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
                break;
            case R.id.login:
                mAuth = FirebaseAuth.getInstance();
                final EditText email = findViewById(R.id.username);
                final EditText password = findViewById(R.id.password);
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LoginActivity", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("uid", user.getUid().toString());
                                    startActivity(i);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                    // ...
                                }

                                // ...
                            }
                        });
                break;
        }

    }
}