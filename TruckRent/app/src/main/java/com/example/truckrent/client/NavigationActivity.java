package com.example.truckrent.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.truckrent.MainActivity;
import com.example.truckrent.R;
import com.example.truckrent.client.ui.gallery.GalleryFragment;
import com.example.truckrent.client.ui.home.HomeFragment;
import com.example.truckrent.client.ui.slideshow.SlideshowFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    public static ArrayList<String> pickAddress = new ArrayList<String>();
    public static ArrayList<String> dropAddress = new ArrayList<String>();
    public static ArrayList<String> pickCity = new ArrayList<String>();
    public static ArrayList<String> dropCity = new ArrayList<String>();
    public static ArrayList<String> pickPin = new ArrayList<String>();
    public static ArrayList<String> dropPin = new ArrayList<String>();
    public static ArrayList<String> pickLog = new ArrayList<String>();
    public static ArrayList<String> dropLog = new ArrayList<String>();
    public static ArrayList<String> pickLat = new ArrayList<String>();
    public static ArrayList<String> dropLat = new ArrayList<String>();
    public static ArrayList<String> totalFare = new ArrayList<String>();
    public static ArrayList<String> totalDist = new ArrayList<String>();
    public static ArrayList<String> statusReq = new ArrayList<String>();
    public static ArrayList<String> otp = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,new HomeFragment()).commit();
                break;
            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,new GalleryFragment()).commit();
                break;
            case R.id.nav_slideshow:
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,new SlideshowFragment()).commit();
                break;
        }
        return true;
    }

}
