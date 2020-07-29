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

import java.util.List;
import com.example.nearbyme.detail_fragments.fragment_detail_announcement;

import io.opencensus.internal.StringUtils;

public class Announcement_adapter extends RecyclerView.Adapter<Announcement_adapter.MyAnnouncementViewHolder> {
private Context mContext;
private List<Announcement_info> mAnnouncementList;
    private double mLat;
    private double mLng;

    public Announcement_adapter(Context mContext, List<Announcement_info> mAnnouncementList) {
        this.mContext = mContext;
        this.mAnnouncementList = mAnnouncementList;
    }
    public void getItemLatLng(double user_lat, double user_lon) {
        mLat=user_lat;
        mLng=user_lon;
    }

    @NonNull
    @Override
    public MyAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_announcements,parent,false);

        return new MyAnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnnouncementViewHolder holder, int position) {
        Announcement_info annInfo=mAnnouncementList.get(position);
        holder.tv_s_no.setText(String.valueOf(position+1));
        holder.tv_subject.setText(annInfo.getSubject());
        float[] distance = new float[2];
        Location.distanceBetween(mLat, mLng, annInfo.getLatitude(), annInfo.getLongitude(), distance);
        int displacement =(int) distance[0];

        holder.tv_distance.setText(displacement+" m away");
        holder.tv_description.setText(annInfo.getDescription());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragment_detail_announcement fd=new fragment_detail_announcement();
                Bundle bundle=new Bundle();
                bundle.putString("ann_id",annInfo.getAnn_id());
                fd.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fd).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnnouncementList.size();
    }


    static class MyAnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView tv_s_no,tv_subject,tv_description,tv_distance;
        View mView;
        public MyAnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_s_no=itemView.findViewById(R.id.tv_ann_sno);
            tv_subject=itemView.findViewById(R.id.tv_ann_title);
            tv_distance=itemView.findViewById(R.id.tv_ann_distance);
            tv_description=itemView.findViewById(R.id.tv_ann_description);
            mView=itemView;

        }
    }
}
