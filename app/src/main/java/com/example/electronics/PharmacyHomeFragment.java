package com.example.electronics;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PharmacyHomeFragment extends Fragment {
    private RecyclerView myDrugs;
    private DatabaseReference databaseReference;
    private List<Drug> drugs;
    private ValueEventListener eventListener;
    private DrugAdapter adapter;
    private ImageView out;
    private  FirebaseAuth mAuth;

    public PharmacyHomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pharmacy_home, container, false);
        out=view.findViewById(R.id.out);
        mAuth=FirebaseAuth.getInstance();
        myDrugs=view.findViewById(R.id.drugs);
        myDrugs.setHasFixedSize(true);
        // Setting its Layout to Grid Layout with 2 Columns
        myDrugs.setLayoutManager(new GridLayoutManager(getContext(), 2));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Loading Data");
        builder.setMessage("Please wait......");
        AlertDialog dialog = builder.create();
        dialog.show();

        drugs = new ArrayList<>();

        adapter = new DrugAdapter(getContext(), drugs);
        myDrugs.setAdapter(adapter);

        //        Retrieving all drugs in the pharmacy
        // Retrieving current user's pharmacy name
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pharmacyName = snapshot.child("pharmacy").getValue(String.class);

                    // Querying drugs node for drugs belonging to the current pharmacy
                    Query query = FirebaseDatabase.getInstance().getReference("products").orderByChild("pharmacy").equalTo(pharmacyName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            drugs.clear();
                            for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                                Drug myItems = itemSnapshot.getValue(Drug.class);
                                myItems.setKey(itemSnapshot.getKey());
                                drugs.add(myItems);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    // Handle case where user data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


//Logout
    out.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle("Logging Out");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "Logged    Out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), Login.class));
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "Logout Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    });

        return view;
    }
}