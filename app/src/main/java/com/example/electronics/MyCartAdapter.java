package com.example.electronics;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {
    private ArrayList<CartItem> cartItems;
    private Context context;
    private SharedPreferences sharedPreferences;
    TextView totalTextView;

    public MyCartAdapter(Context context, List<CartItem> cartItems, SharedPreferences sharedPreferences, TextView totalTextView) {
        this.context = context;
        this.cartItems = (ArrayList<CartItem>) cartItems;
        this.sharedPreferences = sharedPreferences;
        this.totalTextView = totalTextView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.cartItemName.setText(cartItem.getName());
        holder.cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.cartItemPrice.setText("Ugx: " + String.valueOf(cartItem.getPrice()));

        String imageUri = cartItem.getImageUrl();
        if (imageUri != null) {
            Picasso.get().load(imageUri).into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.out); // Set a placeholder image
        }

        // Remove item button
        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position);
                }
            }
        });

        // Reduce Quantity
        // Reduce Quantity
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    int currentQuantity = cartItem.getQuantity();
                    if (currentQuantity > 1) {
                        cartItem.setQuantity(currentQuantity - 1);
                        holder.cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
                        updateCartItems(); // Update the cart items in Shared Preferences

                        // Calculate the total cost based on the updated cart items
                        double totalCost = calculateTotalCost();
                        totalTextView.setText("Ugx: " + totalCost);
                    } else {
                        Toast.makeText(context, "Minimum quantity reached", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

// Increase Quantity
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    int currentQuantity = cartItem.getQuantity();
                    if (currentQuantity < 10) {
                        cartItem.setQuantity(currentQuantity + 1);
                        holder.cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
                        updateCartItems(); // Update the cart items in Shared Preferences

                        // Calculate the total cost based on the updated cart items
                        double totalCost = calculateTotalCost();
                        totalTextView.setText("Ugx: " + totalCost);
                    } else {
                        Toast.makeText(context, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cartItemName;
        public TextView cartItemQuantity;
        public TextView cartItemPrice;
        public ImageView itemImage;
        public ImageView remove_item;
        public Button reduce, increase;

        public MyViewHolder(View view) {
            super(view);
            cartItemName = view.findViewById(R.id.cart_item_name);
            cartItemQuantity = view.findViewById(R.id.cart_item_quantity);
            cartItemPrice = view.findViewById(R.id.cart_item_price);
            itemImage = view.findViewById(R.id.cart_item_image);
            remove_item = view.findViewById(R.id.remove_item);
            reduce = view.findViewById(R.id.reduce);
            increase = view.findViewById(R.id.increase);
        }
    }

    // Remove item from cart
    private void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size()); // Update indices of remaining items

            // Update the cart items in Shared Preferences
            updateCartItems();
            // Calculate the total cost based on the updated cart items
            double totalCost = calculateTotalCost();
            totalTextView.setText("Ugx: " + totalCost);
        }
    }

    // Update the cart items in Shared Preferences
    private void updateCartItems() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonCartItems = gson.toJson(cartItems);
        editor.putString("cart_items", jsonCartItems);
        editor.apply();
    }

    // Calculate the total cost of all items in the cart
    public double calculateTotalCost() {
        double totalCost = 0;

        for (CartItem cartItem : cartItems) {
            totalCost += cartItem.getQuantity() * cartItem.getPrice();
        }

        return totalCost;
    }
}
