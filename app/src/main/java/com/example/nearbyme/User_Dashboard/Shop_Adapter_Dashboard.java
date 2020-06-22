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

import com.example.nearbyme.Model.Shop_info;
import com.example.nearbyme.R;
import com.example.nearbyme.detail_fragments.fragment_detail_shop;
import com.example.nearbyme.fragment_add_home_shop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Shop_Adapter_Dashboard extends RecyclerView.Adapter<Shop_Adapter_Dashboard.MyShopHolder> {
    private Context mContext;
    private List<Shop_info> mShopList;
    private FirebaseFirestore mDBRef;

    public Shop_Adapter_Dashboard(Context mContext, List<Shop_info> mShopList) {
        this.mContext = mContext;
        this.mShopList = mShopList;
    }

    @NonNull
    @Override
    public MyShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dashboard_shop, parent, false);
        return new MyShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShopHolder holder, int position) {
        Shop_info shop = mShopList.get(position);
        holder.tv_sno.setText(String.valueOf(position+1));
        holder.tv_rent.setText(String.valueOf(shop.getRent_amount()));
        holder.tv_dimension.setText(String.valueOf(shop.getDimension()));
      holder.tv_view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              AppCompatActivity activity = (AppCompatActivity) v.getContext();
              fragment_detail_shop fd = new fragment_detail_shop();
              Bundle bundle = new Bundle();
              bundle.putString("shop_id", shop.getShop_id());

              fd.setArguments(bundle);
              activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fd).addToBackStack(null).commit();

          }
      });
      holder.tv_update.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.d("TAG", "update clicked");
              AppCompatActivity activity2 = (AppCompatActivity) v.getContext();
              fragment_add_home_shop fs = new fragment_add_home_shop();
              Bundle bundle2 = new Bundle();
              bundle2.putString("shop_id", shop.getShop_id());

              fs.setArguments(bundle2);
              activity2.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fs).addToBackStack(null).commit();

          }
      });
      holder.tv_delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.d("TAG", "delete clicked");
              mDBRef = FirebaseFirestore.getInstance();
              mDBRef.collection("shop").document(shop.getShop_id())
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
        return mShopList.size();
    }

    static class MyShopHolder extends RecyclerView.ViewHolder {
        TextView tv_sno, tv_rent, tv_dimension, tv_description, tv_view, tv_delete, tv_update;

        public MyShopHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno_item_shop);
            tv_rent = itemView.findViewById(R.id.tv_rent_item_shop);
            tv_dimension = itemView.findViewById(R.id.tv_dimension_item_shop);
            tv_description = itemView.findViewById(R.id.tv_description_item_shop);
            tv_view = itemView.findViewById(R.id.tv_view_item_shop);
            tv_delete = itemView.findViewById(R.id.tv_delete_item_shop);
            tv_update = itemView.findViewById(R.id.tv_update_item_shop);
        }
    }
}
