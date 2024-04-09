package com.example.electronics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerHomeFragment extends Fragment {
    private DatabaseReference databaseReference;
    private List<Drug> myDrugs;
    private RecyclerView drugs;
    private ValueEventListener eventListener;
    private CustomerHomeAdapter adapter;
    private AlertDialog dialog;
    private ViewPager viewPager;
    private int[] images = {R.drawable.tvs, R.drawable.banner, R.drawable.img_2, R.drawable.logo};
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 1500;
    private final long PERIOD_MS = 3500;
    private ImageView exit;
    private FirebaseAuth mAuth;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_customer_home, container, false);

        mAuth=FirebaseAuth.getInstance();
        exit=view.findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logout= new AlertDialog.Builder(getContext());
                logout.setTitle("Logging Out");
                logout.setMessage("Are you sure you want to log out");
                logout.setCancelable(false);
                logout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        Toast.makeText(getContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), Login.class));
                    }
                });
                logout.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Logout Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                logout.show();

            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(imageSliderAdapter);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, DELAY_MS, PERIOD_MS);

        drugs = view.findViewById(R.id.drugs);
        drugs.setHasFixedSize(true);
        drugs.setLayoutManager(new LinearLayoutManager(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Loading Data");
        builder.setMessage("Please wait......");
        dialog = builder.create();
        dialog.show();

        myDrugs = new ArrayList<>();

        adapter = new CustomerHomeAdapter(getContext(), myDrugs);
        drugs.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDrugs.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Drug drug = itemSnapshot.getValue(Drug.class);
                    myDrugs.add(drug);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        };
        databaseReference.addValueEventListener(eventListener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }

        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
