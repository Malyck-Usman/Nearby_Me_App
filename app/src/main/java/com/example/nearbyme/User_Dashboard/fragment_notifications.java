package com.example.nearbyme.User_Dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nearbyme.Model.Notification_info;
import com.example.nearbyme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_notifications extends Fragment {
    String user_id = null;
    private RecyclerView rv_dashboard_notifications;
    private ArrayList<Notification_info> mNotificationList;
    private Notification_Adapter_Dashboard mNotificationAdapter;
    private FirebaseFirestore mDBRef;

    public fragment_notifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        InitViews(view);
        Bundle bundle = this.getArguments();
        Log.d("TAG", "User id in fragment Notifications is " + bundle.getString("u_id"));
        user_id = bundle.getString("u_id", null);
        if (user_id != null) {

            mDBRef = FirebaseFirestore.getInstance();
            mDBRef.collection("users").document(user_id).collection("notifications")
                    .orderBy("time", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {

                                Notification_info notification = qds.toObject(Notification_info.class);
                                notification.setN_id(qds.getId());
                                mNotificationList.add(notification);
                            }
                            mNotificationAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "failed to get notification info" + e.getMessage());
                }
            });

        }
        return view;
    }

    private void InitViews(View view) {
        rv_dashboard_notifications = view.findViewById(R.id.rv_dashboard_notifications);
        mNotificationList = new ArrayList<>();
        mNotificationAdapter = new Notification_Adapter_Dashboard(getActivity(), mNotificationList);
        rv_dashboard_notifications.setAdapter(mNotificationAdapter);
        rv_dashboard_notifications.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
