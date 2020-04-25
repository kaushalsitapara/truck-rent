package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.manager.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button sOut = findViewById(R.id.signout);
        sOut.setOnClickListener(this);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, NavigationActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, NavigationActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
