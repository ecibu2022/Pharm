package com.example.electronics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyCart extends AppCompatActivity {
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyCartAdapter cartAdapter;
    private TextView total;
    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        checkout=findViewById(R.id.checkout);


// Check Out
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if cart items are empty
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(MyCart.this, "There is nothing to dominate", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        // Get customer details
                        String userId = currentUser.getUid();

                        // Get reference to the "users" node in Firebase Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                        // Retrieve user details from the "users" node
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // User exists in the database, retrieve user details
                                    String customerName = dataSnapshot.child("username").getValue(String.class);
                                    String customerTel = dataSnapshot.child("telephone").getValue(String.class);

                                    for (CartItem cartItem : cartItems) {
                                        Intent ret = getIntent();
                                        // Get product details
                                        String Pharmacy = ret.getStringExtra("pharmacy");
                                        String productName = cartItem.getName();
                                        String productImage = cartItem.getImageUrl();
                                        String price = String.valueOf(cartItem.getPrice());

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders");
                                        String orderId = databaseReference.push().getKey();
                                        Order order = new Order(orderId, customerName, customerTel, Pharmacy, productName, productImage, price);
                                        databaseReference.child(orderId).setValue(order);
                                    }

                                    // Notify the user that the checkout was successful
                                    Toast.makeText(MyCart.this, "Checkout successful", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });


        total = findViewById(R.id.total);

        recyclerView = findViewById(R.id.myCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the cart items from Shared Preferences
        cartItems = getCartItemsFromSharedPrefs();

        // Check if cart items are empty
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(MyCart.this, "Your Shopping Cart is Empty", Toast.LENGTH_SHORT).show();
            checkout.setEnabled(false);
        } else {
            // Set up the CartAdapter with the cart items
            cartAdapter = new MyCartAdapter(this, cartItems, getSharedPreferences("CartPreferences", Context.MODE_PRIVATE), total);
            recyclerView.setAdapter(cartAdapter);

            // Calculate the total cost
            double totalCost = cartAdapter.calculateTotalCost();
            total.setText("Ugx: " + totalCost);
            checkout.setEnabled(true);
        }

//        // Set up the CartAdapter with the cart items
//        cartAdapter = new MyCartAdapter(this, cartItems, getSharedPreferences("CartPreferences", Context.MODE_PRIVATE), total);
//        recyclerView.setAdapter(cartAdapter);
//
//        // Calculate the total cost
//        double totalCost = cartAdapter.calculateTotalCost();
//        total.setText("Ugx: " + totalCost);
    }

    // Retrieve cart items from Shared Preferences
    private ArrayList<CartItem> getCartItemsFromSharedPrefs() {
        // Replace "CartPreferences" with your desired preference name
        SharedPreferences sharedPreferences = getSharedPreferences("CartPreferences", Context.MODE_PRIVATE);
        String jsonCartItems = sharedPreferences.getString("cart_items", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return gson.fromJson(jsonCartItems, type);
    }
}
