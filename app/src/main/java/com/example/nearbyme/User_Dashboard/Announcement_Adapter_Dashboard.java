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

import com.example.nearbyme.Model.Announcement_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_announcement;
import com.example.nearbyme.fragment_add_announcement;
import com.example.nearbyme.fragment_add_home_shop;
import com.example.nearbyme.fragment_announcement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Announcement_Adapter_Dashboard extends RecyclerView.Adapter<Announcement_Adapter_Dashboard.MyAnnHolder> {
    private Context mContext;
    private List<Announcement_info> mAnnList;
    private FirebaseFirestore mDBRef;

    public Announcement_Adapter_Dashboard(Context mContext, List<Announcement_info> mAnnList) {
        this.mContext = mContext;
        this.mAnnList = mAnnList;
    }

    @NonNull
    @Override
    public MyAnnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_announcement, parent, false);

        return new MyAnnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnnHolder holder, int position) {
        Announcement_info announcement = mAnnList.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_subject.setText(announcement.getSubject());
        holder.tv_description.setText(announcement.getDescription());
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity ActivityView = (AppCompatActivity) v.getContext();
                fragment_detail_announcement FAI = new fragment_detail_announcement();
                Bundle bundleAnn = new Bundle();
                bundleAnn.putString("ann_id", announcement.getAnn_id());
                FAI.setArguments(bundleAnn);
                ActivityView.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FAI).addToBackStack(null).commit();
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDBRef = FirebaseFirestore.getInstance();
                Log.d("TAG", "delete clicked");
                mDBRef.collection("announcements").document(announcement.getAnn_id())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                // notifyDataSetChanged();
                                notifyItemRemoved(position);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
            }
        });
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                fragment_add_announcement fhs = new fragment_add_announcement();
                Bundle bundle = new Bundle();
                bundle.putString("ann_id", announcement.getAnn_id());

                fhs.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fhs).addToBackStack(null).commit();
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnnList.size();
    }

    public class MyAnnHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_subject, tv_description, tv_view, tv_update, tv_delete;

        public MyAnnHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno_item_ann);
            tv_subject = itemView.findViewById(R.id.tv_subject_item_ann);
            tv_description = itemView.findViewById(R.id.tv_description_item_ann);
            tv_view = itemView.findViewById(R.id.tv_view_item_ann);
            tv_update = itemView.findViewById(R.id.tv_update_item_ann);
            tv_delete = itemView.findViewById(R.id.tv_delete_item_ann);


        }
    }
}
