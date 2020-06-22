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
import com.example.nearbyme.detail_fragments.fragment_detail_shop;

import java.util.List;

public class Shop_adapter extends RecyclerView.Adapter<Shop_adapter.MyShopHolder> {
private Context mContext;
private List<Shop_info> mShopList;
    private double mLat;
    private double mLng;

    public Shop_adapter(Context mContext, List<Shop_info> mShopList) {
        this.mContext = mContext;
        this.mShopList = mShopList;
    }
    public void getResLatLng(double user_lat, double user_lon) {
        mLat=user_lat;
        mLng=user_lon;
    }
    @NonNull
    @Override
    public MyShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_shop,parent,false);

        return new MyShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShopHolder holder, int position) {
        Shop_info shop=mShopList.get(position);
        holder.tv_s_no.setText(String.valueOf(position+1));
        holder.tv_rent.setText("RS : "+shop.getRent_amount());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, shop.getLatitude(), shop.getLongitude(), distance);
        int displacement =(int) distance[0];
        if(displacement>=1000){
            holder.tv_distance.setText(displacement/1000+" km away");
        }else{

        holder.tv_distance.setText(displacement+" m away");
        }
        holder.tv_description.setText(shop.getDescription());
        holder.tv_dimension.setText(shop.getDimension()+" ft");
holder.mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        fragment_detail_shop fd=new fragment_detail_shop();
        Bundle bundle=new Bundle();
        bundle.putString("shop_id",shop.getShop_id());
        fd.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

    }
});
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

    static class MyShopHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no, tv_rent,tv_distance,tv_dimension,tv_description;
        View mView;
        public MyShopHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_shop_sno);
            tv_rent=itemView.findViewById(R.id.tv_shop_rent);
            tv_distance=itemView.findViewById(R.id.tv_shop_distance);
            tv_dimension=itemView.findViewById(R.id.tv_shop_dimension);
            tv_description=itemView.findViewById(R.id.tv_shop_description);
mView=itemView;
        }
    }
}
