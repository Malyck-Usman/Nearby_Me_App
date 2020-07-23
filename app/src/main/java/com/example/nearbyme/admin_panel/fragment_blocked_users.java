package com.example.nearbyme.admin_panel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nearbyme.Model.User;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_blocked_users extends Fragment {
    private FirebaseFirestore mDBRef;
    private RecyclerView mRecyclerView;
    private ArrayList<User> mUserList;
    private allowed_block_adapter mUserAdapter;

    public fragment_blocked_users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blocked_users, container, false);

        mDBRef = FirebaseFirestore.getInstance();

        InitViews(view);
        mDBRef.collection("users").whereEqualTo("status",false).get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {

                            User user = qds.toObject(User.class);
                            user.setUser_id(qds.getId());
                            mUserList.add(user);


                        }
                        mUserAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return view;
    }

    private void InitViews(View view) {
        mRecyclerView=view.findViewById(R.id.rv_admin_blocked_users);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserList = new ArrayList<>();
        mUserAdapter = new allowed_block_adapter(getContext(), mUserList);
        mRecyclerView.setAdapter(mUserAdapter);
    }
}
