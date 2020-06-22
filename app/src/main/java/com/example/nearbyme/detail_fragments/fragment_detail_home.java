package com.example.nearbyme.detail_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearbyme.Model.Home_info;
import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_home extends Fragment {
    private TextView tv_Area, tv_CoveredAre, tv_Rent, tv_Security, tv_Floor, tv_Rooms, tv_Bathrooms, tv_Kitchens, tv_Furnished, tv_Gas, tv_Water, tv_Lawn, tv_Garage, tv_Description, tv_OwnerName, tv_OwnerNo;
private FirebaseFirestore mDBRef;
private String mHomeId;

    public fragment_detail_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_home, container, false);
        InitViews(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mHomeId = bundle.getString("home_id", null);
//            Log.d("TAG", mHomeId);

        }
        mDBRef=FirebaseFirestore.getInstance();
        mDBRef.collection("homes").document(bundle.getString("home_id")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Home_info home=documentSnapshot.toObject(Home_info.class);
                        assert home != null;
                        tv_Area.setText(String.valueOf(home.getTotal_area())+" Marla");
                        tv_CoveredAre.setText(String.valueOf(home.getCovered_area())+" Marla");
                        tv_Rent.setText(String.valueOf(home.getRent_amount()));
                        String security=String.valueOf(home.getSecurity_amount());
                        if(security.equals("")){
                            tv_Security.setText("N/A");
                        }else {
                            tv_Security.setText(security);
                        }
                        String floor=home.getFloor();
                        if(floor.equals("")){
                            tv_Floor.setText("N/A");
                        }else {
                            tv_Floor.setText(floor);
                        }
                        tv_Rooms.setText(String.valueOf(home.getRooms()));
                        tv_Bathrooms.setText(String.valueOf(home.getBathrooms()));
                        String kitchen=String.valueOf(home.getKitchens());
                        if(kitchen.equals("")){
                            tv_Kitchens.setText("N/A");
                        }else {
                            tv_Kitchens.setText(kitchen);
                        }
                        String description=String.valueOf(home.getDescription());
                        if(description.equals("")){
                            tv_Description.setText("N/A");
                        }else {
                            tv_Description.setText(description);
                        }
                        boolean furnished=home.isFurnished();
                        if(furnished){
                            tv_Furnished.setText("\u2713");
                            tv_Furnished.setTextColor(getResources().getColor(R.color.app_green));
                        }
                        else {
                            tv_Furnished.setText("X");
                            tv_Furnished.setTextColor(getResources().getColor(R.color.app_red));
                        }

                        boolean gas=home.isGas();
                        if(gas){
                            tv_Gas.setText("\u2713");
                            tv_Gas.setTextColor(getResources().getColor(R.color.app_green));
                        }
                        else {
                            tv_Gas.setText("X");
                            tv_Gas.setTextColor(getResources().getColor(R.color.app_red));
                        }
                        boolean water=home.isWater();
                        if(water){
                            tv_Water.setText("\u2713");
                            tv_Water.setTextColor(getResources().getColor(R.color.app_green));
                        }
                        else {
                            tv_Water.setText("X");
                            tv_Water.setTextColor(getResources().getColor(R.color.app_red));
                        }
                        boolean garage=home.isGarage();
                        if(garage){
                            tv_Garage.setText("\u2713");
                            tv_Garage.setTextColor(getResources().getColor(R.color.app_green));
                        }
                        else {
                            tv_Garage.setText("X");
                            tv_Garage.setTextColor(getResources().getColor(R.color.app_red));
                        }
                        boolean lawn=home.isFurnished();
                        if(lawn){
                            tv_Lawn.setText("\u2713");
                            tv_Lawn.setTextColor(getResources().getColor(R.color.app_green));
                        }
                        else {
                            tv_Lawn.setText("X");
                            tv_Lawn.setTextColor(getResources().getColor(R.color.app_red));
                        }

mDBRef.collection("users").document(home.getUser_id()).get()
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

    }
});

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




        return view;
    }

    private void InitViews(View view) {
        tv_Area = view.findViewById(R.id.tv_homedetail_totalarea);
        tv_CoveredAre = view.findViewById(R.id.tv_homedetail_coveredarea);
        tv_Rent = view.findViewById(R.id.tv_homedetail_rent);
        tv_Security = view.findViewById(R.id.tv_homedetail_security);
        tv_Floor = view.findViewById(R.id.tv_homedetail_floor);
        tv_Rooms = view.findViewById(R.id.tv_homedetail_rooms);
        tv_Bathrooms = view.findViewById(R.id.tv_homedetail_bathrooms);
        tv_Kitchens = view.findViewById(R.id.tv_homedetail_kitchens);
        tv_Furnished = view.findViewById(R.id.tv_homedetail_furnished);
        tv_Gas = view.findViewById(R.id.tv_homedetail_gas);
        tv_Water = view.findViewById(R.id.tv_homedetail_water);
        tv_Lawn = view.findViewById(R.id.tv_homedetail_lawn);
        tv_Garage = view.findViewById(R.id.tv_homedetail_garage);
        tv_Description = view.findViewById(R.id.tv_homedetail_description);
        tv_OwnerName = view.findViewById(R.id.tv_homedetail_ownername);
        tv_OwnerNo = view.findViewById(R.id.tv_homedetail_ownerphoneno);

    }
}
