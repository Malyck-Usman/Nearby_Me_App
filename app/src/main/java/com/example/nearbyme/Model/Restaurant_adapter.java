package com.example.nearbyme.Model;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.MapDialog;
import com.example.nearbyme.R;

import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;
import com.example.nearbyme.fragment_restaurant_hotel;


import java.util.Calendar;
import java.util.List;

public class Restaurant_adapter extends RecyclerView.Adapter<Restaurant_adapter.MyResViewHolder>  {
private List<Restaurant_info> mResList;
private Context mContext;
    private double mLat;
    private double mLng;


    public Restaurant_adapter(List<Restaurant_info> mResList, Context mContext) {
        this.mResList = mResList;
        this.mContext = mContext;

    }
    public void getResLatLng(double user_lat, double user_lon) {
    mLat=user_lat;
    mLng=user_lon;
    }

    @NonNull
    @Override
    public MyResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_restaurant,parent,false);
        return new MyResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyResViewHolder holder, int position) {
        Restaurant_info restaurant=mResList.get(position);
        holder.tv_s_no.setText(String.valueOf(position+1));
        holder.tv_name.setText(restaurant.getRes_name());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, restaurant.getLatitude(), restaurant.getLongitude(), distance);
        int displacement =(int) distance[0];
        holder.tv_distance.setText(displacement+" m away");
        float openTime=restaurant.getOpening_hour()+(restaurant.getOpening_minute()/60f);
        float closeTime=restaurant.getClosing_hour()+(restaurant.getClosing_minute()/60f);
        Calendar now=Calendar.getInstance();
        float currentTime=now.get(Calendar.HOUR_OF_DAY)+(now.get(Calendar.MINUTE)/60f);
        if(openTime>closeTime){
            if(currentTime>openTime || currentTime<closeTime){
                holder.tv_status.setText("Open Now");
                holder.tv_status.setTextColor(Color.GREEN);
            }
            else{
                holder.tv_status.setText("Closed");
                holder.tv_status.setTextColor(Color.RED);

            }
        }
        if(openTime<closeTime){
            if(currentTime>openTime && currentTime<closeTime){
                holder.tv_status.setText("Open Now");
                holder.tv_status.setTextColor(Color.GREEN);

            }
            else {
                holder.tv_status.setText("Closed");
                holder.tv_status.setTextColor(Color.RED);
            }
        }

        holder.tv_openingtime.setText("Open at   "+String.valueOf(restaurant.getOpening_hour())+":"+String.valueOf(restaurant.getOpening_minute()));
        holder.tv_closingtime.setText("Closes at "+String.valueOf(restaurant.getClosing_hour())+":"+String.valueOf(restaurant.getClosing_minute()));


holder.mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        fragment_detail_restaurant fd=new fragment_detail_restaurant();
        Bundle bundle=new Bundle();
        bundle.putString("res_id",restaurant.getRes_id());
        fd.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

    }
});
    }

    @Override
    public int getItemCount() {
        return mResList.size();
    }

//    @Override
//    public void onClick(View v) {
//        AppCompatActivity activity = (AppCompatActivity) v.getContext();
//     //  activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_detail_restaurant()).commit();
//       fragment_detail_restaurant fd=new fragment_detail_restaurant("asdfg Restaurent");
//        fd.show(activity.getSupportFragmentManager(), "MapDialog");
//
//
//
////        MapDialog mapDialog = new MapDialog();
////       // mapDialog.setTargetFragment(fragment_add_restaurants.this, 1);
////        mapDialog.setCancelable(false);
////        mapDialog.show(activity.getSupportFragmentManager(), "MapDialog");
//    }


    static class MyResViewHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no,tv_name,tv_distance,tv_status,tv_openingtime,tv_closingtime;
View mView;
        public MyResViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_restaurant_sno);
            tv_name=itemView.findViewById(R.id.tv_restaurant_name);
            tv_distance=itemView.findViewById(R.id.tv_restaurant_distance);
            tv_status=itemView.findViewById(R.id.tv_restaurant_status);
            tv_openingtime=itemView.findViewById(R.id.tv_restaurant_openingtime);
            tv_closingtime=itemView.findViewById(R.id.tv_restaurant_closingtime);
            mView=itemView;
        }
    }



}
