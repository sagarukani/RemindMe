package com.example.remindme;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.remindme.home.HomeFragment;
import com.example.remindme.home.ProfileFragment;
import com.example.remindme.home.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemReselectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bNav);
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.search){
            loadFragment(new SearchFragment());
        }else if(item.getItemId()==R.id.home){
            loadFragment(new HomeFragment());
        }else if(item.getItemId()==R.id.profile){
            loadFragment(new ProfileFragment());
        }else{
            loadFragment(new HomeFragment());
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.search){
            loadFragment(new SearchFragment());
        }else if(item.getItemId()==R.id.home){
            loadFragment(new HomeFragment());
        }else if(item.getItemId()==R.id.profile){
            loadFragment(new ProfileFragment());
        }else{
            loadFragment(new HomeFragment());
        }
    }
}