package com.example.electronics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminUpdatePharmacy extends AppCompatActivity {
    EditText Name,Email,Phone,Address;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_pharmacy);

        Intent intent = getIntent();
        String pharmacy = intent.getStringExtra("pharmacy");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String key = intent.getStringExtra("key");

        Name = findViewById(R.id.pharmacy);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        Address = findViewById(R.id.address);
        edit=findViewById(R.id.create);

        Name.setText(pharmacy);
        Email.setText(email);
        Phone.setText(phone);
        Address.setText(address);

       edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map=new HashMap<>();
                map.put("pharmacy", Name.getText().toString());
                map.put("email", Email.getText().toString());
                map.put("phone", Phone.getText().toString());
                map.put("address", Address.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("users").child(key)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminUpdatePharmacy.this, "Shop Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminUpdatePharmacy.this, AdminViewPharmacies.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminUpdatePharmacy.this, "Failed to Update Shop", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

            }
}