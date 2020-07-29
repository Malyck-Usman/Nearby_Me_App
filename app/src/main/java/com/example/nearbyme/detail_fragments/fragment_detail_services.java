package com.example.nearbyme.detail_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.Service_info;
import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_services extends Fragment {
    TextView tv_Type, tv_Speciality, tv_Experience, tv_ChargeType, tv_Charges, tv_Description, tv_OwnerName, tv_OwnerNumber;
    private FirebaseFirestore mDBRef;
    private String mServiceId;

    public fragment_detail_services() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_services, container, false);

        InitViews(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mServiceId = bundle.getString("service_id", null);
//            Log.d("TAG", mServiceId);

        }
        mDBRef = FirebaseFirestore.getInstance();
        mDBRef.collection("services").document(mServiceId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Service_info service = documentSnapshot.toObject(Service_info.class);
                        tv_Type.setText(service.getService_type());
                        String speciality = service.getSpeciality();
                        if (speciality.equals("")) {
                            tv_Speciality.setText("N/A");
                        } else {

                            tv_Speciality.setText(speciality);
                        }
                        tv_Experience.setText(service.getExperience() + " Years");
                        tv_ChargeType.setText(service.getCharge_type());
                        tv_Charges.setText(String.valueOf(service.getCharge_amount()));
                        String description = service.getDescription();
                        if (description.equals("")) {
                            tv_Description.setText("N/A");
                        } else {

                            tv_Description.setText(description);
                        }
                        tv_Description.setText(service.getDescription());
                        mDBRef.collection("users").document(service.getUser_id()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        assert user != null;
                                        Log.d("TAG","user id is "+service.getUser_id());
                                        tv_OwnerName.setText(user.getUser_name());
                                        tv_OwnerNumber.setText(user.getPhone_no());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Failed to get data" + e.getMessage());
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
        tv_Type = view.findViewById(R.id.tv_servicedetail_type);
        tv_Speciality = view.findViewById(R.id.tv_servicedetail_speciality);
        tv_Experience = view.findViewById(R.id.tv_servicedetail_experience);
        tv_ChargeType = view.findViewById(R.id.tv_servicedetail_chargetype);
        tv_Charges = view.findViewById(R.id.tv_servicedetail_charges);
        tv_Description = view.findViewById(R.id.tv_servicedetail_description);
        tv_OwnerName = view.findViewById(R.id.tv_servicedetail_ownername);
        tv_OwnerNumber = view.findViewById(R.id.tv_servicedetail_ownerphoneno);

    }
}
