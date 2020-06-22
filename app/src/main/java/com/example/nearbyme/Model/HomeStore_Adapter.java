package com.example.nearbyme.Model;

import android.content.Context;
import android.graphics.Color;
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
import com.example.nearbyme.detail_fragments.fragment_detail_homestore;
import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class HomeStore_Adapter extends RecyclerView.Adapter<HomeStore_Adapter.MyHSViewHolder> {
    private Context mContext;
    private List<Home_Store_info> mHome_Store_List;
    private double mLat;
    private double mLng;

    public HomeStore_Adapter(Context mContext, List<Home_Store_info> mHome_Store_List) {
        this.mContext = mContext;
        this.mHome_Store_List = mHome_Store_List;
    }
    public void getHomeStoreLatLong(double user_lat, double user_lon) {
        mLat=user_lat;
        mLng=user_lon;
    }

    @NonNull
    @Override
    public MyHSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(mContext).inflate(R.layout.item_list_homestore,parent,false);

        return new MyHSViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHSViewHolder holder, int position) {
Home_Store_info homestore=mHome_Store_List.get(position);
holder.tv_s_no.setText(String.valueOf(position+1));
holder.tv_name.setText(homestore.getStore_name());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, homestore.getLatitude(), homestore.getLongitude(), distance);
        int displacement =(int) distance[0];
holder.tv_distance.setText(displacement+" m away");
float openTime=homestore.getOpening_hour()+(homestore.getOpening_minute()/60f);
float closeTime=homestore.getClosing_hour()+(homestore.getClosing_minute()/60f);
        Calendar now=Calendar.getInstance();
        float currentTime=now.get(Calendar.HOUR_OF_DAY)+(now.get(Calendar.MINUTE)/60f);
        if(openTime>closeTime){
            if(currentTime>openTime || currentTime<closeTime){
                holder.tv_status.setText("Open Now");
                holder.tv_status.setTextColor(Color.GREEN);
            }
            else{
                holder.tv_status.setText("Closed Now");
                holder.tv_status.setTextColor(Color.RED);

            }
        }
        if(openTime<closeTime){
            if(currentTime>openTime && currentTime<closeTime){
                holder.tv_status.setText("Open Now");
                holder.tv_status.setTextColor(Color.GREEN);

            }
            else {
                holder.tv_status.setText("Closed Now");
                holder.tv_status.setTextColor(Color.RED);
            }
        }

        holder.tv_openingtime.setText("Open at   "+String.valueOf(homestore.getOpening_hour())+":"+String.valueOf(homestore.getOpening_minute()));
holder.tv_closingtime.setText("Closes at "+String.valueOf(homestore.getClosing_hour())+":"+String.valueOf(homestore.getClosing_minute()));
holder.mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        fragment_detail_homestore fd=new fragment_detail_homestore();

        Bundle bundle=new Bundle();
        bundle.putString("homestore_id",homestore.getHomestore_id());
        fd.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

    }
});
    }

    @Override
    public int getItemCount() {
        return mHome_Store_List.size();
    }


    static class MyHSViewHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no,tv_name,tv_distance,tv_status,tv_openingtime,tv_closingtime;
        View mView;
        public MyHSViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_homestore_sno);
            tv_name=itemView.findViewById(R.id.tv_homestore_name);
            tv_distance=itemView.findViewById(R.id.tv_homestore_distance);
            tv_status=itemView.findViewById(R.id.tv_homestore_status);
            tv_openingtime=itemView.findViewById(R.id.tv_homestore_openingtime);
            tv_closingtime=itemView.findViewById(R.id.tv_homestore_closingtime);
            mView=itemView;
        }
    }
}
