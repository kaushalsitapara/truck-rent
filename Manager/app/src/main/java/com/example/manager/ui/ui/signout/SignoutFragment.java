package com.example.manager.ui.ui.signout;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manager.MainActivity;
import com.example.manager.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignoutFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signout_fragment, container, false);
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
        return root;
    }



}
