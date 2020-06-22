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
import com.example.nearbyme.detail_fragments.fragment_detail_buy;
import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;

import java.util.List;
import java.util.zip.Inflater;

public class Item_adapter extends RecyclerView.Adapter<Item_adapter.MyItemHolder> {
private Context mContext;
private List<Item_info> mItemList;
    private double mLat;
    private double mLng;

    public Item_adapter(Context mContext, List<Item_info> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }
    public void getItemLatLng(double user_lat, double user_lon) {
        mLat=user_lat;
        mLng=user_lon;
    }

    @NonNull
    @Override
    public MyItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_buy,parent,false);
        return new MyItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemHolder holder, int position) {
        Item_info item=mItemList.get(position);
        holder.tv_s_no.setText(String.valueOf(position+1));
        holder.tv_name.setText(item.getItem_name());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, item.getLatitude(), item.getLongitude(), distance);
        int displacement =(int) distance[0];
        if(displacement<1000){

        holder.tv_distance.setText(displacement+" m away");
        }
        else {
            holder.tv_distance.setText((displacement/1000)+" km away");
        }
        holder.tv_price.setText("Rs : "+item.getPrice());
        holder.tv_status.setText("Condition :  "+item.getCondition());
        holder.tv_brand.setText("Make : "+item.getBrand_name());
        holder.tv_description.setText(item.getDescription());
holder.mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        fragment_detail_buy fd=new fragment_detail_buy();
        Bundle bundle=new Bundle();
        bundle.putString("item_id",item.getItem_id());
        fd.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

    }
});

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


    static class MyItemHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no,tv_price,tv_name,tv_distance,tv_status,tv_brand,tv_description;
        View mView;

        public MyItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_item_sno);
            tv_name=itemView.findViewById(R.id.tv_item_name);
            tv_distance=itemView.findViewById(R.id.tv_item_distance);
            tv_status=itemView.findViewById(R.id.tv_item_condition);
            tv_brand=itemView.findViewById(R.id.tv_item_brand);
            tv_price=itemView.findViewById(R.id.tv_item_price);
            tv_description=itemView.findViewById(R.id.tv_item_description);
            mView=itemView;
        }
    }
}
