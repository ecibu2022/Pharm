package com.example.electronics;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private TextView mNameTextView, mEmailTextView, mPhoneTextView, mAddressTextView;
    private Button mUpdateButton, editProfile;
    private CircleImageView myProfile;
    String imageUrl;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        currentUser = mAuth.getCurrentUser();


        // Initialize the TextViews and Button
        mNameTextView = view.findViewById(R.id.name);
        mEmailTextView = view.findViewById(R.id.email);
        mPhoneTextView = view.findViewById(R.id.phone);
        mAddressTextView = view.findViewById(R.id.address);
        mUpdateButton = view.findViewById(R.id.update);
        editProfile=view.findViewById(R.id.edit_profile);
        myProfile=view.findViewById(R.id.image);

                // Retrieve the user's profile data from Firebase
        mDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the user data from the snapshot
                String name = snapshot.child("name").getValue(String.class);
                String email=snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                imageUrl=snapshot.child("uri").getValue(String.class);


                // Set the TextViews with the user data
                mNameTextView.setText(name);
                mEmailTextView.setText(email);
                mPhoneTextView.setText(phone);
                mAddressTextView.setText(address);
                Picasso.get().load(imageUrl).into(myProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });


        // Set onClickListener for the update button
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the user data from TextViews
                String name = mNameTextView.getText().toString();
                String phone = mPhoneTextView.getText().toString();
                String address = mAddressTextView.getText().toString();


//                Passing User Data to an Intent
                Intent send=new Intent(getContext(), UpdateCustomerProfile.class);
                send.putExtra("name", name);
                send.putExtra("phone", phone);
                send.putExtra("address", address);
                send.putExtra("uri", imageUrl);
                startActivity(send);
            }
        });

        return view;
    }

}


