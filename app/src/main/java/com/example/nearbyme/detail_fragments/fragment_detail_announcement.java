package com.example.nearbyme.detail_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearbyme.Model.Announcement_info;
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
public class fragment_detail_announcement extends Fragment {
    private String mAnnId;
    private FirebaseFirestore mDBRef;
    TextView tv_subject, tv_description, tv_UName, tv_UNo;

    public fragment_detail_announcement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_announcement, container, false);


        tv_subject = view.findViewById(R.id.tv_ann_subject);
        tv_description = view.findViewById(R.id.tv_announcement_description);
        tv_UName = view.findViewById(R.id.tv_ann_uname);
        tv_UNo = view.findViewById(R.id.tv_ann_userno);
        mDBRef = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String mAnnId = bundle.getString("ann_id", null);
            Log.d("TAG", mAnnId);

        }
        assert bundle != null;
        mDBRef.collection("announcements")
                .document(Objects.requireNonNull(bundle.getString("ann_id")))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Announcement_info ann_info = documentSnapshot.toObject(Announcement_info.class);
                        assert ann_info != null;

                        tv_subject.setText(ann_info.getSubject());
                        tv_description.setText(ann_info.getDescription());

                        mDBRef.collection("users").document(ann_info.getUser_id())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        assert user != null;
                                        tv_UName.setText(user.getUser_name());
                                        tv_UNo.setText(user.getPhone_no());
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
                Log.d("TAG", "failed to get data" + e.getMessage());
            }
        });


        return view;
    }
}
