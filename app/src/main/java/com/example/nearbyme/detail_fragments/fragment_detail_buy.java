package com.example.nearbyme.detail_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearbyme.Model.Item_info;
import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_detail_buy extends Fragment {
    TextView tv_Name, tv_Brand, tv_Condition, tv_price,tv_Description, tv_OwnerName, tv_OwnerNumber;
    private FirebaseFirestore mDBRef;

    public fragment_detail_buy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_buy, container, false);
        InitViews(view);
        mDBRef = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String mItemId = bundle.getString("item_id", null);
            Log.d("TAG", mItemId);

        }
        assert bundle != null;
        mDBRef.collection("items").document(Objects.requireNonNull(bundle.getString("item_id"))).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Item_info item = documentSnapshot.toObject(Item_info.class);
                        tv_Name.setText(item.getItem_name());
                      tv_Name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                        String brand = item.getBrand_name();
                        if (brand.equals("")) {
                            tv_Brand.setText("N/A");
                        } else {
                            tv_Brand.setText(brand);
                        }
                        tv_Condition.setText(item.getCondition());
                        tv_price.setText(String.valueOf(item.getPrice()));
                        String description=item.getDescription();
                        if(description.equals("")){
                            tv_Description.setText("N/A");
                        }else {
                            tv_Description.setText(description);
                        }
                        mDBRef.collection("users").document(item.getUser_id()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user=documentSnapshot.toObject(User.class);
                                        tv_OwnerName.setText(user.getUser_name());
                                        tv_OwnerNumber.setText(user.getPhone_no());
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
        tv_Name = view.findViewById(R.id.tv_itemdetail_name);
        tv_Brand = view.findViewById(R.id.tv_itemdetail_brand);
        tv_Condition = view.findViewById(R.id.tv_itemdetail_condition);
        tv_price = view.findViewById(R.id.tv_itemdetail_price);
        tv_Description= view.findViewById(R.id.tv_itemdetail_description);
        tv_OwnerName = view.findViewById(R.id.tv_itemdetail_ownername);
        tv_OwnerNumber = view.findViewById(R.id.tv_itemdetail_ownerphoneno);
    }
}
