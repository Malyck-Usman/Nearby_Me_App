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

import com.example.nearbyme.Model.Item_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_buy;
import com.example.nearbyme.fragment_add_Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Item_Adapter_Dashboard extends RecyclerView.Adapter<Item_Adapter_Dashboard.MyItemViewHolder> {
    private Context mContext;
    private List<Item_info> mItemList;
    private FirebaseFirestore mDBRef;

    public Item_Adapter_Dashboard(Context mContext, List<Item_info> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_items, parent, false);

        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
      Item_info item=mItemList.get(position);
        holder.tv_sno.setText(String.valueOf(position+1));
        holder.tv_name.setText(item.getItem_name());
        holder.tv_price.setText(String.valueOf(item.getPrice()));
        holder.tv_description.setText(item.getDescription());
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
                Bundle viewBundle=new Bundle();
                viewBundle.putString("item_id",item.getItem_id());
                fragment_detail_buy fdb=new fragment_detail_buy();
                fdb.setArguments(viewBundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fdb).addToBackStack(null).commit();
            }
        });
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
                Bundle updateBundle=new Bundle();
                updateBundle.putString("item_id",item.getItem_id());
                fragment_add_Item fai=new fragment_add_Item();
                fai.setArguments(updateBundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fai).addToBackStack(null).commit();
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBRef=FirebaseFirestore.getInstance();
                mDBRef.collection("items").document(item.getItem_id()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG","delete successful");
                                mItemList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mItemList.size());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_name, tv_price, tv_description, tv_view, tv_update,tv_delete;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno=itemView.findViewById(R.id.tv_sno_item_sell);
            tv_name = itemView.findViewById(R.id.tv_name_item_sell);
            tv_price = itemView.findViewById(R.id.tv_price_item_sell);
            tv_description = itemView.findViewById(R.id.tv_description_item_sell);
            tv_view = itemView.findViewById(R.id.tv_view_item_sell);
            tv_update = itemView.findViewById(R.id.tv_update_item_sell);
            tv_delete=itemView.findViewById(R.id.tv_delete_item_sell);
        }
    }
}
