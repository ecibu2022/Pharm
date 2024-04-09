package com.example.electronics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerPharmaciesFragment extends Fragment {
    private DatabaseReference databaseReference;
    private List<MyItems> myItemsList;
    private RecyclerView pharmacies;
    private ValueEventListener eventListener;
    private GetPharmacyAdapter adapter;
    private AlertDialog dialog; // moved dialog variable to class level to dismiss in onCancel
    public CustomerPharmaciesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_customer_pharmacies, container, false);
        // Initializing RecyclerView
        // add view.findViewById to find view in inflated layout
        pharmacies=view.findViewById(R.id.pharmacies);
        pharmacies.setHasFixedSize(true);
        // Setting its Layout
        pharmacies.setLayoutManager(new LinearLayoutManager(getContext())); // pass context instead of this

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // pass context instead of this
        builder.setCancelable(false);
        builder.setTitle("Loading Data");
        builder.setMessage("Please wait......");
        dialog = builder.create(); // assign to class level variable
        dialog.show();

        myItemsList = new ArrayList<>();

        adapter = new GetPharmacyAdapter(getContext(), myItemsList); // pass context instead of this
        pharmacies.setAdapter(adapter);

        // Retrieving all users with role pharmacy
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query pharmaciesQuery = databaseReference.orderByChild("role").equalTo("pharmacy");
        eventListener = pharmaciesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myItemsList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    MyItems myItems = itemSnapshot.getValue(MyItems.class);

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

        return view;
    }
}