package com.example.electronics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GetPharmacyAdapter extends RecyclerView.Adapter<GetPharmacyAdapter.MyViewHolder> {
    private ArrayList<MyItems> items;
    private Context context;

    public GetPharmacyAdapter(Context context, List<MyItems> items) {
        this.items = (ArrayList<MyItems>) items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.get_pharmacies, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyItems currentItem = items.get(position);
        if (currentItem != null) {
            holder.pharmacy.setText(currentItem.getPharmacy());
            holder.email.setText(currentItem.getEmail());
            holder.phone.setText(currentItem.getPhone());
            holder.address.setText(currentItem.getAddress());

            holder.products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pharmacyName = currentItem.getPharmacy();
                    if (pharmacyName != null && !pharmacyName.isEmpty()) {
                        Intent pharmacyProducts = new Intent(context, CustomerProductCart.class);
                        pharmacyProducts.putExtra("pharmacy", pharmacyName);
                        context.startActivity(pharmacyProducts);
                    } else {
                        Toast.makeText(context, "Pharmacy name is invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacy, email, phone, address;
        Button products;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pharmacy = itemView.findViewById(R.id.pharmacy);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.my_phone);
            address = itemView.findViewById(R.id.address);
            products = itemView.findViewById(R.id.products);

        }
    }
}
