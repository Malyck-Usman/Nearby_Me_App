package com.example.nearbyme.detail_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.Dish;
import com.example.nearbyme.Model.Dish_Adapter;
import com.example.nearbyme.Model.Restaurant_info;
import com.example.nearbyme.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_restaurant extends Fragment  {
   // private Restaurant_info mResInfo;
    private String mResId;
    TextView tv_ResName, tv_OpeningTime, tv_ClosingTime, tv_Description, tv_Home_delivery, tv_TableReservation, tv_Contact_No;
    private RecyclerView mDishRecyclerView;
    private Dish_Adapter_detail mDishAdapter;
    private List<Dish> mDishList;
private FirebaseFirestore mdbref;
    View view;
    private Map<String,Object> dishes;


    public fragment_detail_restaurant() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            view = inflater.inflate(R.layout.fragment_detail_restaurant, container, false);

            InitViews(view);
        mDishList=new ArrayList<>();
        mDishAdapter=new Dish_Adapter_detail(getContext(),mDishList);
        mDishRecyclerView.setAdapter(mDishAdapter);
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mResId = bundle.getString("res_id", null);
                Log.d("TAG", mResId);

            }
            mdbref = FirebaseFirestore.getInstance();

            mdbref.collection("restaurants").document(mResId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Restaurant_info mResInfo = documentSnapshot.toObject(Restaurant_info.class);

                              String ResName=mResInfo.getRes_name();
                                Log.d("TAG","Resttaurant name is"+ResName);
                            tv_ResName.setText(mResInfo.getRes_name());
        tv_OpeningTime.setText(String.valueOf(mResInfo.getOpening_hour())+":"+String.valueOf(mResInfo.getOpening_minute()));
        tv_ClosingTime.setText(String.valueOf(mResInfo.getClosing_hour())+":"+String.valueOf(mResInfo.getClosing_minute()));
        if(mResInfo.getDescription().equals("")){
            tv_Description.setText("N/A");
        }else {

        tv_Description.setText(mResInfo.getDescription());
        }
        boolean homedelivery=mResInfo.isHome_delivery();
        boolean tablereservation=mResInfo.isTable_reservation();
        if(homedelivery){
            tv_Home_delivery.setText("\u2713");
            tv_Home_delivery.setTextColor(getResources().getColor(R.color.app_green));


        }
        else {
            tv_Home_delivery.setText("X");
            tv_Home_delivery.setTextColor(getResources().getColor(R.color.app_red));

        }
        if(tablereservation){
            tv_TableReservation.setText("\u2713");
            tv_TableReservation.setTextColor(getResources().getColor(R.color.app_green));

        }else {
            tv_TableReservation.setText("X");
            tv_TableReservation.setTextColor(getResources().getColor(R.color.app_red));

        }
                            Log.d("TAG", mResInfo.getRes_name());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "failed to get data" + e.getMessage());
                }
            });


            mdbref.collection("restaurants").document(mResId).collection("dishes")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                       dishes=qds.getData();
                    }

                    for(Map.Entry entry:dishes.entrySet()){
                        Dish dish=new Dish((String) entry.getKey(),(String) entry.getValue());
               mDishList.add(dish);
                    }
                    mDishAdapter.notifyDataSetChanged();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "failed to get dish list", Toast.LENGTH_LONG).show();
                }
            });




        return view;
    }
    private void InitViews(View view) {
        tv_ResName=view.findViewById(R.id.tv_res_name);
        tv_OpeningTime=view.findViewById(R.id.tv_res_openingtime);
        tv_ClosingTime=view.findViewById(R.id.tv_res_closingtime);
        tv_Description=view.findViewById(R.id.tv_res_description);
        tv_Home_delivery=view.findViewById(R.id.tv_res_homedelivery);
        tv_TableReservation=view.findViewById(R.id.tv_res_tablereservation);
        tv_Contact_No=view.findViewById(R.id.tv_res_contactno);

        mDishRecyclerView=view.findViewById(R.id.rv_res_dishes);
       mDishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dishes = new HashMap<>();

    }




}
