package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerProductCart extends AppCompatActivity {
    private RecyclerView myDrugs;
    private TextView pharmacy;
    private List<Drug> drugs;
    private ShoppingCartAdapter adapter;
    private FirebaseAuth mAuth;
    private ImageView check_cart;
    String pharmacyName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_prouct_cart);

        Intent retrieve = getIntent();
        pharmacyName = retrieve.getStringExtra("pharmacy");

//        Check Cart Icon ImageView
        check_cart=findViewById(R.id.check_cart);
        check_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart=new Intent(CustomerProductCart.this, MyCart.class);
                cart.putExtra("pharmacy", pharmacyName);
                startActivity(cart);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        pharmacy=findViewById(R.id.pharmacy);
        myDrugs = findViewById(R.id.products);
        myDrugs.setHasFixedSize(true);
        // Setting its Layout
        myDrugs.setLayoutManager(new LinearLayoutManager(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Loading Data");
        builder.setMessage("Please wait......");
        AlertDialog dialog = builder.create();
        dialog.show();

        drugs = new ArrayList<>();

        adapter = new ShoppingCartAdapter(this, drugs);
        myDrugs.setAdapter(adapter);

//        Setting Pharmacy Name
        pharmacy.setText(pharmacyName);
        Query query = FirebaseDatabase.getInstance().getReference("products").orderByChild("pharmacy").equalTo(pharmacyName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugs.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
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

    }

    }