package com.example.electronics;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {
    private ArrayList<Drug> drugs;
    private Context context;
    private ArrayList<CartItem> cartItems;
    private SharedPreferences sharedPreferences;

    public ShoppingCartAdapter(Context context, List<Drug> drugs) {
        this.drugs = (ArrayList<Drug>) drugs;
        this.context = context;
        this.cartItems = new ArrayList<>();

        // Initialize the Shared Preferences
        sharedPreferences = context.getSharedPreferences("CartPreferences", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ShoppingCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Drug Design Layout
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.available_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drug myDrugs=drugs.get(position);
        holder.drug_name.setText(myDrugs.getName());
        holder.price.setText(myDrugs.getPrice());
        holder.description.setText(myDrugs.getDescription());

        String imageUri=myDrugs.getImageUrl();
        Picasso.get().load(imageUri).into(holder.drug_image);

        // Add to cart Button
        Drug drug = drugs.get(position);
        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<CartItem> cartItems = getCartItemsFromSharedPrefs();
                if (cartItems == null) {
                    cartItems = new ArrayList<>(); // Initialize as an empty list
                }
                    // Check if the item already exists in the cart
                    boolean itemExists = false;
                    for (int i = 0; i < cartItems.size(); i++) {
                        if (drug.getName() != null && drug.getName().equals(cartItems.get(i).getName())) {
                            // Item already exists in the cart, increment the quantity
                            CartItem existingCartItem = cartItems.get(i);
                            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                            itemExists = true;
                            Toast.makeText(context, drug.getName() + "quantity has been increased", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (!itemExists) {
                        // Item does not exist in the cart, create a new cart item
                        CartItem cartItem = new CartItem(drug.getName(), 1, Double.parseDouble(drug.getPrice()), drug.getImageUrl());
                        cartItems.add(cartItem);
                        Toast.makeText(context, drug.getName() + " has been added to cart", Toast.LENGTH_SHORT).show();
                    }

                    // Save the updated cart items to Shared Preferences
                    saveCartItemsToSharedPrefs(cartItems);
             }

            });

    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    // Retrieve cart items from Shared Preferences
    private List<CartItem> getCartItemsFromSharedPrefs() {
        String jsonCartItems = sharedPreferences.getString("cart_items", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return gson.fromJson(jsonCartItems, type);
    }

    // Save cart items to Shared Preferences
    private void saveCartItemsToSharedPrefs(List<CartItem> cartItems) {
        Gson gson = new Gson();
        String jsonCartItems = gson.toJson(cartItems);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_items", jsonCartItems);
        editor.apply();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView drug_image;
        private TextView drug_name, price, description;
        private Button add_to_cart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drug_image = itemView.findViewById(R.id.drug_image);
            drug_name = itemView.findViewById(R.id.drug_name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);

            add_to_cart = itemView.findViewById(R.id.order);
        }
    }

}

