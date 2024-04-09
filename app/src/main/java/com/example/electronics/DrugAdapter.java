package com.example.electronics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.MyViewHolder> {
    private ArrayList<Drug> drugs;
    private Context context;
    String key="";

    public DrugAdapter(Context context, List<Drug> drugs) {
        this.drugs = (ArrayList<Drug>) drugs;
        this.context = context;
    }

    @NonNull
    @Override
    public DrugAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Inflating the Drug Design Layout
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_design, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.drug_name.setText(drugs.get(position).getName());
        holder.price.setText(drugs.get(position).getPrice());
        holder.description.setText(drugs.get(position).getDescription());

        Picasso.get().load(drugs.get(position).getImageUrl()).into(holder.drug_image);

////        Update Drug Button
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Drug drug = drugs.get(holder.getAdapterPosition());
//
//                Intent updateIntent = new Intent(context, PharmacyUpdateProduct.class);
//                updateIntent.putExtra("drug", drug.getName());
//                updateIntent.putExtra("price", drug.getPrice());
//                updateIntent.putExtra("description", drug.getDescription());
//                updateIntent.putExtra("imageUrl", drug.getImageUrl());
//                updateIntent.putExtra("key", drug.getKey());
//
//                context.startActivity(updateIntent);
//            }
//        });
    }

        @Override
    public int getItemCount() {
        return drugs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView drug_image;
        private TextView drug_name,price,description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drug_image=itemView.findViewById(R.id.drug_image);
            drug_name=itemView.findViewById(R.id.drug_name);
            price=itemView.findViewById(R.id.price);
            description=itemView.findViewById(R.id.description);

        }
    }
}
