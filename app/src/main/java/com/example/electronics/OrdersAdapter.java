package com.example.electronics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    private ArrayList<Order> orders;
    private Context context;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.orders = (ArrayList<Order>) orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        This makes the layout to fit the screen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_design, parent, false);
        return new OrdersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.drug_name.setText(orders.get(position).getProductName());
        holder.price.setText(orders.get(position).getPrice());

    }

        @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView drug_name,price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drug_name=itemView.findViewById(R.id.drug_name);
            price=itemView.findViewById(R.id.price);

        }
    }
}
