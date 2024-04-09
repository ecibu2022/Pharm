package com.example.electronics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {
    private CircleImageView image;
    private TextView name,email,phone,address;
    private Button edit_profile;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    //    It takes a user from here to Admin home when back button is pressed
    public  void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminProfile.this, Admin.class));
        finish();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        mAuth=FirebaseAuth.getInstance();

        image=findViewById(R.id.profilePicture);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        edit_profile=findViewById(R.id.update);

            // Initialize the Firebase reference
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

            // Attach a ValueEventListener to retrieve the user's data
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get the user's data and set it to the UI elements
                    String Name = dataSnapshot.child("name").getValue(String.class);
                    String Email = dataSnapshot.child("email").getValue(String.class);
                    String Phone = dataSnapshot.child("phone").getValue(String.class);
                    String Address = dataSnapshot.child("address").getValue(String.class);
                    String photoUrl = dataSnapshot.child("uri").getValue(String.class);

                    name.setText(Name);
                    email.setText(Email);
                    phone.setText(Phone);
                    address.setText(Address);

                    Picasso.get().load(photoUrl).into(image);


                    //        Edit Profile Button
                    edit_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent profile=new Intent(AdminProfile.this, AdminEditProfile.class);
                            profile.putExtra("name", Name);
                            profile.putExtra("email",Email);
                            profile.putExtra("phone", Phone);
                            profile.putExtra("address", Address);
                            profile.putExtra("photoUrl", photoUrl);
                            startActivity(profile);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                }
            });


        }


}