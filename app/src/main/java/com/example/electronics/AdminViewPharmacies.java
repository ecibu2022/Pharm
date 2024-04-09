package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewPharmacies extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<MyItems> myItemsList;
    private RecyclerView pharmacies;
    private ValueEventListener eventListener;
    private Adapter adapter;

//    It takes a user from here to Admin home when back button is pressed
    public  void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminViewPharmacies.this, Admin.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_pharmacies);

        // Initializing RecyclerView
        pharmacies=findViewById(R.id.PharmacyRecyclerView);
        pharmacies.setHasFixedSize(true);
        // Setting its Layout
        pharmacies.setLayoutManager(new LinearLayoutManager(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Loading Data");
        builder.setMessage("Please wait......");
        AlertDialog dialog = builder.create();
        dialog.show();

        myItemsList = new ArrayList<>();

        adapter = new Adapter(this, myItemsList);
        pharmacies.setAdapter(adapter);


//        Retrieving all users with role pharmacy
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query pharmaciesQuery = databaseReference.orderByChild("role").equalTo("pharmacy");
        eventListener = pharmaciesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myItemsList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    MyItems myItems = itemSnapshot.getValue(MyItems.class);

                    myItems.setKey(itemSnapshot.getKey());

                    myItemsList.add(myItems);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

    }


}
