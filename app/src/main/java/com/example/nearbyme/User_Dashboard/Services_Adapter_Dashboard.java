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

import com.example.nearbyme.Model.Service_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_services;
import com.example.nearbyme.fragment_add_services;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.List;

public class Services_Adapter_Dashboard extends RecyclerView.Adapter<Services_Adapter_Dashboard.MyServiceViewHolder> {
    private List<Service_info> mServiceList;
    private Context mContext;
    private FirebaseFirestore mDBRef;

    public Services_Adapter_Dashboard(List<Service_info> mServiceList, Context mContext) {
        this.mServiceList = mServiceList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_service, parent, false);

        return new MyServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceViewHolder holder, int position) {
        Service_info service = mServiceList.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_type.setText(service.getService_type());
        holder.tv_speciality.setText(service.getSpeciality());
        holder.tv_description.setText(service.getDescription());
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle su = new Bundle();
                su.putString("service_id", service.getService_id());
                fragment_add_services fds = new fragment_add_services();
                fds.setArguments(su);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fds).addToBackStack(null).commit();

            }
        });
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle sv = new Bundle();
                sv.putString("service_id", service.getService_id());
                fragment_detail_services fds = new fragment_detail_services();
                fds.setArguments(sv);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fds).addToBackStack(null).commit();

            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBRef = FirebaseFirestore.getInstance();
                mDBRef.collection("services").document(service.getService_id()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "delete successful");
                                mServiceList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mServiceList.size());
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
        return mServiceList.size();
    }

    static class MyServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_type, tv_speciality, tv_description, tv_view, tv_update, tv_delete;

        public MyServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno_item_service);
            tv_type = itemView.findViewById(R.id.tv_type_item_services);
            tv_speciality = itemView.findViewById(R.id.tv_speciality_item_services);
            tv_description = itemView.findViewById(R.id.tv_description_item_services);
            tv_view = itemView.findViewById(R.id.tv_view_item_services);
            tv_update = itemView.findViewById(R.id.tv_update_item_services);
            tv_delete = itemView.findViewById(R.id.tv_delete_item_services);

        }
    }
}
