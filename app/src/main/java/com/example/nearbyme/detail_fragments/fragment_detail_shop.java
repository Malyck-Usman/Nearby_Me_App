package com.example.nearbyme.detail_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearbyme.Model.Shop_info;
import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_shop extends Fragment {
    private TextView tv_Dimension,  tv_Rent, tv_Security, tv_Floor, tv_Finishing, tv_Parking, tv_Store,  tv_Description, tv_OwnerName, tv_OwnerNo;

    private FirebaseFirestore mDBRef;
    private String mShopID;

    public fragment_detail_shop() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail_shop, container, false);
   InitViews(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mShopID = bundle.getString("shop_id", null);
            Log.d("TAG", mShopID);

        }
        mDBRef= FirebaseFirestore.getInstance();
mDBRef.collection("shop").document(bundle.getString("shop_id")).get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Shop_info shop=documentSnapshot.toObject(Shop_info.class);

                assert shop != null;
                tv_Dimension.setText(shop.getDimension()+" ft");
                tv_Rent.setText(String.valueOf(shop.getRent_amount()));
                String security=String.valueOf(shop.getSecurity_amount());
                if(security.equals("")){
                    tv_Security.setText("N/A");
                }else {
                    tv_Security.setText(security);
                }
                String floor=shop.getFloor();
                if(floor.equals("")){
                    tv_Floor.setText("N/A");
                }else {
                    tv_Floor.setText(floor);
                }

                String description=String.valueOf(shop.getDescription());
                if(description.equals("")){
                    tv_Description.setText("N/A");
                }else {
                    tv_Description.setText(description);
                }
                boolean finishing=shop.isFinishing();
                if(finishing){
                    tv_Finishing.setText("\u2713");
                    tv_Finishing.setTextColor(getResources().getColor(R.color.app_green));
                }
                else {
                    tv_Finishing.setText("X");
                    tv_Finishing.setTextColor(getResources().getColor(R.color.app_red));
                }

                boolean parking=shop.isParking();
                if(parking){
                    tv_Parking.setText("\u2713");
                    tv_Parking.setTextColor(getResources().getColor(R.color.app_green));
                }
                else {
                    tv_Parking.setText("X");
                    tv_Parking.setTextColor(getResources().getColor(R.color.app_red));
                }
                boolean store=shop.isStore();
                if(store){
                    tv_Store.setText("\u2713");
                    tv_Store.setTextColor(getResources().getColor(R.color.app_green));
                }
                else {
                    tv_Store.setText("X");
                    tv_Store.setTextColor(getResources().getColor(R.color.app_red));
                }
                mDBRef.collection("users").document(shop.getUser_id()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user=documentSnapshot.toObject(User.class);
                                assert user != null;
                                tv_OwnerName.setText(user.getUser_name());
                                tv_OwnerNo.setText(String.valueOf(user.getPhone_no()));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","failed to get owner data"+e.getMessage());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("TAG","failed to get data"+e.getMessage());
    }
});


   return view;
    }

    private void InitViews(View view) {
         tv_Dimension=view.findViewById(R.id.tv_shopdetail_dimension);
                 tv_Rent=view.findViewById(R.id.tv_shopdetail_rent);
        tv_Security=view.findViewById(R.id.tv_shopdetail_security);
                tv_Floor=view.findViewById(R.id.tv_shopdetail_floor);
        tv_Finishing=view.findViewById(R.id.tv_shopdetail_finishing);
                tv_Parking=view.findViewById(R.id.tv_shopdetail_parking);
        tv_Store=view.findViewById(R.id.tv_shopdetail_store);
                tv_Description=view.findViewById(R.id.tv_shopdetail_description);
        tv_OwnerName=view.findViewById(R.id.tv_shopdetail_ownername);
                tv_OwnerNo=view.findViewById(R.id.tv_shopdetail_ownerphoneno);


    }
}
