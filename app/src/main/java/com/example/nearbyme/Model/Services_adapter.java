package com.example.nearbyme.Model;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;
import com.example.nearbyme.detail_fragments.fragment_detail_services;

import java.util.List;

public class Services_adapter extends RecyclerView.Adapter<Services_adapter.MyServicesHolder> {
    private Context mContext;
    private List<Service_info> mServices_list;
    private double mLat;
    private double mLng;

    public void getHomeStoreLatLong(double user_lat, double user_lon) {
        mLat=user_lat;
        mLng=user_lon;
    }

    public Services_adapter(Context mContext, List<Service_info> mServices_list) {
        this.mContext = mContext;
        this.mServices_list = mServices_list;
    }

    @NonNull
    @Override
    public MyServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_sercive,parent,false);
        return new MyServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServicesHolder holder, int position) {
        Service_info service=mServices_list.get(position);
        holder.tv_s_no.setText(String.valueOf(position+1));
        holder.tv_speciality.setText(service.getSpeciality());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, service.getLatitude(), service.getLongitude(), distance);
        int displacement =(int) distance[0];
        holder.tv_distance.setText(displacement+" m away");
        holder.tv_experience.setText(String.valueOf(service.getExperience())+" year Experience");
        holder.tv_charges.setText("Charges "+String.valueOf(service.getCharge_amount())+" "+String.valueOf(service.getCharge_type()));

   holder.mView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           AppCompatActivity activity = (AppCompatActivity) v.getContext();
           fragment_detail_services fd=new fragment_detail_services();
           Bundle bundle=new Bundle();
           bundle.putString("service_id",service.getService_id());
           fd.setArguments(bundle);
           activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

       }
   });
    }

    @Override
    public int getItemCount() {
        return mServices_list.size();
    }


    static class MyServicesHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no,tv_speciality,tv_distance,tv_experience,tv_charges;
View mView;
        public MyServicesHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_service_sno);
            tv_speciality=itemView.findViewById(R.id.tv_service_speciality);
            tv_distance=itemView.findViewById(R.id.tv_service_distance);
            tv_experience=itemView.findViewById(R.id.tv_service_experience);
            tv_charges=itemView.findViewById(R.id.tv_service_charges);
            mView=itemView;

        }
    }
}
