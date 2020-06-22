package com.example.nearbyme.detail_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.Model.Dish;
import com.example.nearbyme.Model.Dish_Adapter;
import com.example.nearbyme.R;

import java.util.List;

public class Dish_Adapter_detail extends RecyclerView.Adapter<Dish_Adapter_detail.MyDishHolder> {
    private Context mContext;
    private List<Dish> mDishList;

    public Dish_Adapter_detail(Context mContext, List<Dish> mDishList) {
        this.mContext = mContext;
        this.mDishList = mDishList;
    }

    @NonNull
    @Override
    public MyDishHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View RootView = LayoutInflater.from(mContext).inflate(R.layout.item_dishes, parent, false);

        return new Dish_Adapter_detail.MyDishHolder(RootView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyDishHolder holder, int position) {
        Dish dish = mDishList.get(position);
        holder.tv_Dish_Name.setText(dish.getDish_name());
        holder.tv_Dish_Price.setText(dish.getDish_price());
    }

    @Override
    public int getItemCount() {
        return mDishList.size();
    }

    static class MyDishHolder extends RecyclerView.ViewHolder {
        TextView tv_Dish_Name, tv_Dish_Price;

        public MyDishHolder(@NonNull View itemView) {
            super(itemView);
            tv_Dish_Name = itemView.findViewById(R.id.tv_dish_name);
            tv_Dish_Price = itemView.findViewById(R.id.tv_dish_price);

        }
    }
}
