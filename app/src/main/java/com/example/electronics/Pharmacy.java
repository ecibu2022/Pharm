package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Pharmacy extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);

//        Makes Home active when back button is clicked either in settings or profile it goes back to home
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    PharmacyHomeFragment homeFragment=new PharmacyHomeFragment();
    PharmacyAddProductFragment pharmacyAddProductFragment=new PharmacyAddProductFragment();
    ProductOrdersFragment pharmacyOrdersFragment=new ProductOrdersFragment();

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

            case R.id.add_product:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, pharmacyAddProductFragment)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.orders:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, pharmacyOrdersFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
        }

        return false;
    }


}
