package com.example.nearbyme.User_Dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.Model.Home_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_announcement;
import com.example.nearbyme.detail_fragments.fragment_detail_home;
import com.example.nearbyme.fragment_add_home_shop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Home_Adapter_Dashboard extends RecyclerView.Adapter<Home_Adapter_Dashboard.MyHomeDashboard> {
    private Context mContext;
    private List<Home_info> mHomeList;
    private FirebaseFirestore mDBRef;

    public Home_Adapter_Dashboard(Context mContext, List<Home_info> mHomeList) {
        this.mContext = mContext;
        this.mHomeList = mHomeList;
    }

    @NonNull
    @Override
    public MyHomeDashboard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_home, parent, false);
        return new MyHomeDashboard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHomeDashboard holder, int position) {
        Home_info home = mHomeList.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_rent.setText(String.valueOf(home.getRent_amount()));
        holder.tv_rooms.setText(String.valueOf(home.getRooms()));
        holder.tv_bathrooms.setText(String.valueOf(home.getBathrooms()));
        holder.tv_description.setText(home.getDescription());
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragment_detail_home fd = new fragment_detail_home();
                Bundle bundle = new Bundle();
                bundle.putString("home_id", home.getHome_id());

                fd.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fd).addToBackStack(null).commit();

            }
        });
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "update clicked");
                AppCompatActivity activity2 = (AppCompatActivity) v.getContext();
                fragment_add_home_shop fhs = new fragment_add_home_shop();
                Bundle bundle2 = new Bundle();
                bundle2.putString("home_id", home.getHome_id());

                fhs.setArguments(bundle2);
                activity2.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fhs).addToBackStack(null).commit();
                notifyItemChanged(position);

            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBRef = FirebaseFirestore.getInstance();
                Log.d("TAG", "delete clicked");
                mDBRef.collection("homes").document(home.getHome_id())
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
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    static class MyHomeDashboard extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_rent, tv_rooms, tv_bathrooms, tv_description, tv_view, tv_update,tv_delete;

        public MyHomeDashboard(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno_item_home);
            tv_rent = itemView.findViewById(R.id.tv_rent_item_home);
            tv_rooms = itemView.findViewById(R.id.tv_rooms_item_home);
            tv_bathrooms = itemView.findViewById(R.id.tv_bathroom_item_home);
            tv_description = itemView.findViewById(R.id.tv_description_item_home);
            tv_view = itemView.findViewById(R.id.tv_view_item_home);
            tv_delete = itemView.findViewById(R.id.tv_delete_item_home);
            tv_update = itemView.findViewById(R.id.tv_update_item_home);
        }
    }
}
