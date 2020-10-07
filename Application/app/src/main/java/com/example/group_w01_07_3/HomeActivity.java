package com.example.group_w01_07_3;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.group_w01_07_3.ui.account.AccountFragment;
import com.example.group_w01_07_3.ui.create.CreateCapsuleFragment;
import com.example.group_w01_07_3.ui.discover.DiscoverCapsuleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //loading the default fragment
        loadFragment(new DiscoverCapsuleFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_discover:
                fragment = new DiscoverCapsuleFragment();
                break;

            case R.id.navigation_capsule:
                fragment = new CreateCapsuleFragment();
                break;

            case R.id.navigation_account:
                fragment = new AccountFragment();
                break;
        }

        return loadFragment(fragment);
    }


    //help us to switch between fragments
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}