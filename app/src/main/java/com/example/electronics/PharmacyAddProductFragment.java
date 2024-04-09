package com.example.electronics;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class PharmacyAddProductFragment extends Fragment {
    private EditText drug_name, description, price;
    private ImageView drug_image;
    private Button upload_drug;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    public static final int RESULT_OK = -1;

    public PharmacyAddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy_add_product, container, false);

        drug_name = view.findViewById(R.id.drug_name);
        description = view.findViewById(R.id.drug_description);
        price = view.findViewById(R.id.price);
        drug_image = view.findViewById(R.id.drug_image);
        upload_drug=view.findViewById(R.id.upload);
        progressDialog= new ProgressDialog(getContext());
        storageReference = FirebaseStorage.getInstance().getReference("products");
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

//        Choosing Image
        drug_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
            });

//        Upload Drug Button
        upload_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    uploadDrug();
                } else {
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


//    Choosing Image
    private void chooseImage() {
        // Open gallery to select image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            drug_image.setImageURI(imageUri);
        }
    }

//    Upload Drug Button
    public void uploadDrug() {
        if(imageUri!=null) {
            // Upload drug details to database
            String name = drug_name.getText().toString();
            String desc = description.getText().toString();
            String drugPrice = price.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc)
                            && !TextUtils.isEmpty(drugPrice) && imageUri != null) {
                        uploadDrugToFirebase(imageUri);
                    }else {
                        Toast.makeText(getContext(), "Select Product Image", Toast.LENGTH_SHORT).show();
                    }

                }

        }

    public void uploadDrugToFirebase(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(getContext(), "Please Select Product Image", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Product Upload in Progress");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Get the file extension from the imageUri
            ContentResolver contentResolver = getContext().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + extension);
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String name = drug_name.getText().toString();
                            String desc = description.getText().toString();
                            String drugPrice = price.getText().toString();
                            String drugID=databaseReference.push().getKey();

                            // Retrieving Pharmacy Name
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String pharmacyName=snapshot.child("pharmacy").getValue(String.class);
                                    Drug drug=new Drug(drugID,pharmacyName,name,desc,drugPrice,uri.toString());

                                    // Get a reference to the "users" node
                                    FirebaseDatabase.getInstance().getReference("products").child(drugID).setValue(drug).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                drug_image.setImageURI(null);
                                                drug_name.setText(null);
                                                description.setText(null);
                                                price.setText(null);
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Product Upload Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}