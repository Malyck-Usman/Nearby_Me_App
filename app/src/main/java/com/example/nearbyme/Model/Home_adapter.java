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
import com.example.nearbyme.detail_fragments.fragment_detail_home;
import com.example.nearbyme.detail_fragments.fragment_detail_restaurant;

import java.util.List;

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.MyViewHolder> {
private Context mContext;
private List<Home_info> mHomeList;
private double mHomeLat;
private double mHomeLong;

    public Home_adapter(Context mContext, List<Home_info> mHomeList) {
        this.mContext = mContext;
        this.mHomeList = mHomeList;
    }
public void getHomeLatLong(double lat,double lng){
        mHomeLat=lat;
        mHomeLong=lng;
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_home,parent,false);
return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

Home_info home=mHomeList.get(position);
holder.tv_area.setText(String.valueOf(home.getTotal_area())+" M");
holder.tv_rent.setText("RS : "+home.getRent_amount());
holder.tv_beds.setText("Bedrooms : "+String.valueOf(home.getRooms()));
holder.tv_baths.setText("Baths : "+String.valueOf(home.getBathrooms()));
        float[] distance = new float[2];
        Location.distanceBetween(mHomeLat, mHomeLong, home.getLatitude(), home.getLongitude(), distance);
        int displacement =(int) distance[0];

holder.tv_description.setText(displacement+" meters away");
holder.tv_home_s_no.setText(String.valueOf(position+1));
holder.mHomeView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        fragment_detail_home fd=new fragment_detail_home();
        Bundle bundle=new Bundle();
        bundle.putString("home_id",home.getHome_id());
        fd.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

    }
});
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
TextView tv_area,tv_rent,tv_description,tv_baths,tv_beds,tv_home_s_no;
View mHomeView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
tv_area=itemView.findViewById(R.id.tv_home_area);
tv_rent=itemView.findViewById(R.id.tv_home_rent);
tv_description=itemView.findViewById(R.id.tv_home_description);
tv_baths=itemView.findViewById(R.id.tv_home_bathroom);
tv_beds=itemView.findViewById(R.id.tv_home_rooms);
tv_home_s_no=itemView.findViewById(R.id.tv_home_sno);
mHomeView=itemView;



        }
    }
}
