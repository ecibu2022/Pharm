package com.example.electronics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter orderAdapter;
    private ArrayList<Order> ordersList;

    public ProductOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_orders, container, false);
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersList = new ArrayList<>();
        orderAdapter = new OrdersAdapter(getActivity(), ordersList);
        recyclerView.setAdapter(orderAdapter);

        // Retrieve orders from Firebase Realtime Database
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        // Get the current pharmacy name
        // Retrieving current user's pharmacy name
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pharmacyName = snapshot.child("pharmacy").getValue(String.class);

                    // Querying drugs node for drugs belonging to the current pharmacy
                    Query query = FirebaseDatabase.getInstance().getReference("orders").orderByChild("pharmacy").equalTo(pharmacyName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ordersList.clear();
                            for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                                Order myItems = itemSnapshot.getValue(Order.class);
                                ordersList.add(myItems);
                            }
                            orderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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

    }

}