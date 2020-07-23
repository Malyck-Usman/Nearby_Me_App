package com.example.nearbyme.User_Dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.Model.Restaurant_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;
import com.example.nearbyme.fragment_add_restaurants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Restaurant_Adapter_Dashboard extends RecyclerView.Adapter<Restaurant_Adapter_Dashboard.MyResViewHolder> {
    private List<Restaurant_info> mResList;
    private Context mContext;
    private FirebaseFirestore mDBRef;

    public Restaurant_Adapter_Dashboard(List<Restaurant_info> mResList, Context mContext) {
        this.mResList = mResList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_restaurant, parent, false);
        return new MyResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyResViewHolder holder, int position) {
        Restaurant_info restaurant = mResList.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_name.setText(restaurant.getRes_name());
        holder.tv_description.setText(restaurant.getDescription());
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle rv = new Bundle();
                rv.putString("res_id", restaurant.getRes_id());
                fragment_detail_restaurant fdr = new fragment_detail_restaurant();
                fdr.setArguments(rv);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fdr).addToBackStack(null).commit();

            }
        });
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bu = new Bundle();
                bu.putString("res_id", restaurant.getRes_id());
                fragment_add_restaurants far = new fragment_add_restaurants();
                far.setArguments(bu);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, far).addToBackStack(null).commit();


            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", " delete selectd!");

                mDBRef = FirebaseFirestore.getInstance();
                mDBRef.collection("restaurants").document(restaurant.getRes_id()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");

                                mResList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mResList.size());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResList.size();
    }

    static class MyResViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_name, tv_description, tv_view, tv_update, tv_delete;

        public MyResViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno_item_restaurant);
            tv_name = itemView.findViewById(R.id.tv_name_item_restaurant);
            tv_description = itemView.findViewById(R.id.tv_description_item_restaurant);
            tv_view = itemView.findViewById(R.id.tv_view_item_restaurant);
            tv_update = itemView.findViewById(R.id.tv_update_item_restaurant);
            tv_delete = itemView.findViewById(R.id.tv_delete_item_restaurant);

        }
    }
}
