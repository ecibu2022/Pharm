package com.example.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateCustomerProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private TextView mNameEditText, mPhoneEditText, mEmailEditText, mAddressEditText;
    private Button editProfile;
    private CircleImageView myProfile;
    String imageUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer_profile);

        myProfile=findViewById(R.id.image);
        mNameEditText = findViewById(R.id.name);
        mPhoneEditText = findViewById(R.id.phone);
        mEmailEditText=findViewById(R.id.email);
        mAddressEditText = findViewById(R.id.address);
        editProfile = findViewById(R.id.edit_profile);

        // Get the current user and database reference
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve the user data from arguments
        Intent retrieve = getIntent();
        String name = retrieve.getStringExtra("name");
        String phone = retrieve.getStringExtra("phone");
        String address = retrieve.getStringExtra("address");
        imageUrl = retrieve.getStringExtra("uri");

        // Set the EditTexts with the user data
        mNameEditText.setText(name);
        mPhoneEditText.setText(phone);
        mAddressEditText.setText(address);
        Picasso.get().load(imageUrl).into(myProfile);

        // Set onClickListener for the update button
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the user data from EditTexts
                String name = mNameEditText.getText().toString();
                String phone = mPhoneEditText.getText().toString();
                String address = mAddressEditText.getText().toString();

                // Update the user data in the Firebase database
                mDatabase.child(currentUser.getUid()).child("name").setValue(name);
                mDatabase.child(currentUser.getUid()).child("phone").setValue(phone);
                mDatabase.child(currentUser.getUid()).child("address").setValue(address);

                // Notify the user that the profile has been updated
                Toast.makeText(UpdateCustomerProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                Navigating Back to Customer Fragment
                onBackPressed();

            }
        });

        // Set onClickListener for the edit profile button
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the image URI from the Intent
            Uri imageUri = data.getData();

            // Upload the image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("users");
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded image
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Update the user's profile image URL in Firebase Database
                            mDatabase.child(currentUser.getUid()).child("uri").setValue(uri.toString());
                            Picasso.get().load(imageUri.toString()).into(myProfile);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle upload failure
                }
            });
        }
    }

    // Get the file extension of an image URI
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


//    Going back to Customer Profile
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}