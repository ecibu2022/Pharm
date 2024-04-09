package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddPharmacies extends AppCompatActivity {
    private EditText pharmacy, email, phone, address, password;
    private Button create;
    private FirebaseAuth mAuth;

    //    It takes a user from here to Admin home when back button is pressed
    public  void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminAddPharmacies.this, Admin.class));
        finish();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_pharmacies);
        
        pharmacy=findViewById(R.id.pharmacy);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        password=findViewById(R.id.password);
        create=findViewById(R.id.create);
        mAuth=FirebaseAuth.getInstance();
        
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Pharmacy=pharmacy.getText().toString().trim();
                String Email=email.getText().toString().trim();
                String Phone=phone.getText().toString().trim();
                String Address=address.getText().toString().trim();
                String Password=password.getText().toString().trim();
                String Role="pharmacy";
                
                if(Pharmacy.isEmpty()){
                    pharmacy.setError("Shop Name Required*");
                    pharmacy.requestFocus();
                    return;
                }else if(Email.isEmpty()){
                    email.setError("Email Required*");
                    email.requestFocus();
                    return;
                } else if (Phone.isEmpty()) {
                    phone.setError("Phone Number Required*");
                    phone.requestFocus();
                    return;
                } else if (Address.isEmpty()) {
                    address.setError("Shop Address Required*");
                    address.requestFocus();
                    return;
                } else if (Password.isEmpty()) {
                    password.setError("Password Required*");
                    password.requestFocus();
                    return;
                }else{
                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful()){
                                      PharmacyRegistration pharmacies=new PharmacyRegistration(Pharmacy,Email, Phone, Address, Role);
                                      FirebaseDatabase.getInstance().getReference("users")
                                              .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                              .setValue(pharmacies)
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      if(task.isSuccessful()){
                                                          //Add Details to Database
                                                          Toast.makeText(AdminAddPharmacies.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();
                                                          pharmacy.setText(null);
                                                          email.setText(null);
                                                          phone.setText(null);
                                                          address.setText(null);
                                                          password.setText(null);
                                                          startActivity(new Intent(AdminAddPharmacies.this, Admin.class));
                                                          finish();
                                                      }else{
                                                          Toast.makeText(AdminAddPharmacies.this, "Failed to Create Shop", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              });
                                  }else{
                                      Toast.makeText(AdminAddPharmacies.this, "Error try again", Toast.LENGTH_SHORT).show();
                                  }
                                }
                            });

                }
            }
        });
        
    }
}