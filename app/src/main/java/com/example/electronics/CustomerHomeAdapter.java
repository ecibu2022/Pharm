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

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.MyViewHolder> {
    private ArrayList<Drug> drugs;
    private Context context;

    public CustomerHomeAdapter(Context context, List<Drug> drugs) {
        this.drugs = (ArrayList<Drug>) drugs;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        This makes the layout to fit the screen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_drugs, parent, false);
        return new CustomerHomeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(drugs.get(position).getName());
        holder.description.setText(drugs.get(position).getDescription());

        Picasso.get().load(drugs.get(position).getImageUrl()).into(holder.drug_image);

    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView drug_image;
        private TextView name,description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drug_image=itemView.findViewById(R.id.drug_image);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);

        }
    }
}
