package com.example.nearbyme.detail_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearbyme.Model.Home_Store_info;
import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_homestore extends Fragment {
    TextView tv_Name, tv_Type, tv_OpeningTime, tv_ClosingTime, tv_Description, tv_OwnerName, tv_OwnerNumber;
    private FirebaseFirestore mDBRef;

    public fragment_detail_homestore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_homestore, container, false);


        InitViews(view);


        mDBRef = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String mHSid = bundle.getString("homestore_id", null);
            Log.d("TAG", mHSid);

        }


        mDBRef.collection("store").document(bundle.getString("homestore_id")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Home_Store_info homestore = documentSnapshot.toObject(Home_Store_info.class);
tv_Name.setText(homestore.getStore_name());
tv_Type.setText(homestore.getStore_type());
tv_OpeningTime.setText(homestore.getOpening_hour()+" : "+homestore.getOpening_minute());
tv_ClosingTime.setText(homestore.getClosing_hour()+" : "+homestore.getClosing_minute());
String description=homestore.getDescription();
if(description.equals("")){

tv_Description.setText("N/A");
}else {

tv_Description.setText(description);
}
mDBRef.collection("users").document(homestore.getUser_id()).get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user=documentSnapshot.toObject(User.class);
                tv_OwnerNumber.setText(user.getPhone_no());
                tv_OwnerName.setText(user.getUser_name());
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
        tv_Name = view.findViewById(R.id.tv_homestoredetail_name);
        tv_Type = view.findViewById(R.id.tv_homestoredetail_type);
        tv_OpeningTime = view.findViewById(R.id.tv_homestoredetail_openingtime);
        tv_ClosingTime = view.findViewById(R.id.tv_homestoredetail_closingtime);
        tv_Description = view.findViewById(R.id.tv_homestoredetail_description);
        tv_OwnerName = view.findViewById(R.id.tv_homestoredetail_ownername);
        tv_OwnerNumber = view.findViewById(R.id.tv_homestoredetail_ownerphoneno);

    }
}
