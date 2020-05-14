package com.example.nearbyme.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class Dish_Adapter extends RecyclerView.Adapter<Dish_Adapter.MyDishViewHolder> {
private Context mContext;
private List<Dish> mDishList;

    public Dish_Adapter(Context mContext, List<Dish> mDishList) {
        this.mContext = mContext;
        this.mDishList = mDishList;
    }

    @NonNull
    @Override
    public MyDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View RootView= LayoutInflater.from(mContext).inflate(R.layout.item_dishes,parent,false);

        return new MyDishViewHolder(RootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDishViewHolder holder, int position) {
 Dish dish=mDishList.get(position);
holder.tv_Dish_Name.setText(dish.getDish_name());
holder.tv_Dish_Price.setText(dish.getDish_price());
    }

    @Override
    public int getItemCount() {
        if(mDishList!=null){
            return mDishList.size();
        }

        return 0;
    }

    static class MyDishViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Dish_Name,tv_Dish_Price;


        public MyDishViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Dish_Name=itemView.findViewById(R.id.tv_dish_name);
            tv_Dish_Price=itemView.findViewById(R.id.tv_dish_price);
        }
    }
}
