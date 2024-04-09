package com.example.electronics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private ArrayList<MyItems> items;
    private Context context;
    String key = "";

    public Adapter(Context context, List<MyItems> items) {
        this.items = (ArrayList<MyItems>) items;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Inflating the design of the pharmacy layout
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacylayout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
        holder.pharmacy.setText(items.get(position).getPharmacy());
        holder.email.setText(items.get(position).getEmail());
        holder.phone.setText(items.get(position).getPhone());
        holder.address.setText(items.get(position).getAddress());


        final Intent update = new Intent(context, AdminUpdatePharmacy.class);
//        Setting onclick listener on update button
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.putExtra("pharmacy", holder.pharmacy.getText().toString());
                update.putExtra("phone", holder.phone.getText().toString());
                update.putExtra("email", holder.email.getText().toString());
                update.putExtra("address", holder.address.getText().toString());
                update.putExtra("key", key= items.get(holder.getAdapterPosition()).getKey());
                context.startActivity(update);
            }
        });


//        Delete Button
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder delete = new AlertDialog.Builder(context);
                    delete.setTitle("Are you sure?");
                    delete.setMessage("You will not be able to recover...");
                    delete.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Delete pharmacy from Realtime Database
                            key= items.get(holder.getAdapterPosition()).getKey();
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.child(key).removeValue();
                            Toast.makeText(context, "Pharmacy Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    delete.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                    delete.show();

            }
        });


//End of Delete Button
}



    @Override
    public int getItemCount() {
        return items.size();
    }
    static  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pharmacy;
        TextView email;
        TextView phone;
        TextView address;
        private Button update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            pharmacy=itemView.findViewById(R.id.pharmacy);
            email=itemView.findViewById(R.id.email);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);

            update=itemView.findViewById(R.id.update);
            delete=itemView.findViewById(R.id.delete);

        }
    }
}
