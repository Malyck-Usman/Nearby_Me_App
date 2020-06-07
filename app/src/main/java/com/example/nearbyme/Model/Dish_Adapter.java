package com.example.nearbyme.Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dish_Adapter extends RecyclerView.Adapter<Dish_Adapter.MyDishViewHolder> {
    private Context mContext;
    private List<Dish> mDishList;
    private FirebaseFirestore mDBRef;
    private String dish_id=null;
    private String Res_id=null;

    public Dish_Adapter(Context mContext, List<Dish> mDishList) {
        this.mContext = mContext;
        this.mDishList = mDishList;
    }
    public void GetIds(String r_id,String d_id){
        Res_id=r_id;
        dish_id=d_id;
                mDBRef = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public MyDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View RootView = LayoutInflater.from(mContext).inflate(R.layout.item_dishes, parent, false);

        return new MyDishViewHolder(RootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDishViewHolder holder, int position) {
        Dish dish = mDishList.get(position);
        holder.tv_Dish_Name.setText(dish.getDish_name());
        holder.tv_Dish_Price.setText(dish.getDish_price());
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mDishList.remove(position);
                notifyDataSetChanged();
// Remove the 'capital' field from the document
                if(dish_id!=null){
                    Log.d("TAG", "dish id in adapter"+dish_id);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put(dish.getDish_name(), FieldValue.delete());
                    Log.d("TAG", "dish name"+dish.getDish_name());
                    Log.d("TAG", "res_id "+Res_id);


                    mDBRef.collection("restaurants").document(Res_id).collection("dishes")
                            .document(dish_id).update(updates)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "delete successful");
                                    notifyItemRemoved(position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "failed to delete");

                        }
                    });
                }


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDishList != null) {
            return mDishList.size();
        }

        return 0;
    }

    static class MyDishViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Dish_Name, tv_Dish_Price;
        View view;

        public MyDishViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Dish_Name = itemView.findViewById(R.id.tv_dish_name);
            tv_Dish_Price = itemView.findViewById(R.id.tv_dish_price);
            view = itemView.findViewById(R.id.cv_dishes);
        }
    }
}
