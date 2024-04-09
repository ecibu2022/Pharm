package com.example.electronics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminEditProfile extends AppCompatActivity {
    private CircleImageView image;
    private EditText name,email,phone,address;
    private Button update;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

        mAuth=FirebaseAuth.getInstance();

        image=findViewById(R.id.profilePicture);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        email.setEnabled(false);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        update=findViewById(R.id.edit_profile);

//        Getting text from intent
        Intent retrieve=getIntent();
        String Name=retrieve.getStringExtra("name");
        String Email=retrieve.getStringExtra("email");
        String Phone= retrieve.getStringExtra("phone");
        String Address= retrieve.getStringExtra("address");
        String ImageURL= retrieve.getStringExtra("photoUrl");

//        Loading them to text fields
        name.setText(Name);
        email.setText(Email);
        phone.setText(Phone);
        address.setText(Address);
        Picasso.get().load(ImageURL).into(image);

//        Changing Photo
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

//        Update user data
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the unique identifier for the current user
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Create a Map with the updated user data
                Map<String, Object> map = new HashMap<>();
                map.put("name", name.getText().toString());
                map.put("email", email.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("address", address.getText().toString());

                if (mImageUri != null) {
                    // Create a reference to the image file in Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users");
                    UploadTask uploadTask = storageRef.putFile(mImageUri);

                    // Monitor the upload task to check if it is successful
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the image file
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update the user data in the Firebase Realtime Database
                                    map.put("photoUrl", uri.toString());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AdminEditProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AdminEditProfile.this, Admin.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AdminEditProfile.this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminEditProfile.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Update the user data in the Firebase Realtime Database without updating the image
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminEditProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AdminEditProfile.this, Admin.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminEditProfile.this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }


        // Open the device's gallery to select an image
        private void openFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(image);
        }
    }


        }