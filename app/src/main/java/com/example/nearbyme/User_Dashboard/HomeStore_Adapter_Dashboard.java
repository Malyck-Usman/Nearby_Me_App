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

import com.example.nearbyme.Model.Home_Store_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_homestore;
import com.example.nearbyme.fragment_add_homestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeStore_Adapter_Dashboard extends RecyclerView.Adapter<HomeStore_Adapter_Dashboard.MyHSViewHolder> {
    private Context mContext;
    private List<Home_Store_info> mHSList;
    private FirebaseFirestore mDBRef;

    public HomeStore_Adapter_Dashboard(Context mContext, List<Home_Store_info> mHSList) {
        this.mContext = mContext;
        this.mHSList = mHSList;
    }

    @NonNull
    @Override
    public MyHSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_homestore, parent, false);

        return new MyHSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHSViewHolder holder, int position) {
        Home_Store_info HS=mHSList.get(position);
        holder.tv_sno.setText(String.valueOf(position));
        holder.tv_name.setText(HS.getStore_name());
        holder.tv_description.setText(HS.getDescription());
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragment_detail_homestore fd = new fragment_detail_homestore();
                Bundle bundle = new Bundle();
                bundle.putString("homestore_id", HS.getHomestore_id());

                fd.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fd).addToBackStack(null).commit();


            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBRef = FirebaseFirestore.getInstance();
                Log.d("TAG", "delete clicked");
                mDBRef.collection("store").document(HS.getHomestore_id())
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
                Log.d("TAG", "update clicked");
                AppCompatActivity activity2 = (AppCompatActivity) v.getContext();
                fragment_add_homestore fhs = new fragment_add_homestore();
                Bundle bundle2 = new Bundle();
                bundle2.putString("homestore_id", HS.getHomestore_id());

                fhs.setArguments(bundle2);
                activity2.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fhs).addToBackStack(null).commit();
                notifyItemChanged(position);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mHSList.size();
    }

    static class MyHSViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_name, tv_description,tv_update,tv_delete,tv_view;

        public MyHSViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno=itemView.findViewById(R.id.tv_sno_item_homestore);
            tv_name = itemView.findViewById(R.id.tv_name_item_homestore);
            tv_description = itemView.findViewById(R.id.tv_description_item_homestore);
            tv_update=itemView.findViewById(R.id.tv_update_item_homestore);
            tv_delete=itemView.findViewById(R.id.tv_delete_item_homestore);
            tv_view=itemView.findViewById(R.id.tv_view_item_homestore);

        }
    }
}
