package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Customer extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);

//        Makes Home active when back button is clicked either in settings or profile it goes back to home
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    CustomerHomeFragment homeFragment=new CustomerHomeFragment();
    CustomerPharmaciesFragment pharmaciesFragment=new CustomerPharmaciesFragment();
    CustomerProfileFragment customerFragment=new CustomerProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, homeFragment)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.pharmacies:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, pharmaciesFragment)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, customerFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
        }

        return false;
    }
}