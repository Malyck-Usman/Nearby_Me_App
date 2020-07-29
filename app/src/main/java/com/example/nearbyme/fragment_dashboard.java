package com.example.nearbyme;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.nearbyme.Model.Announcement_info;
import com.example.nearbyme.Model.Home_Store_info;
import com.example.nearbyme.Model.Home_info;
import com.example.nearbyme.Model.Item_info;
import com.example.nearbyme.Model.Restaurant_info;
import com.example.nearbyme.Model.Service_info;
import com.example.nearbyme.Model.Shop_info;
import com.example.nearbyme.User_Dashboard.Announcement_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.HomeStore_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.Home_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.Item_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.Restaurant_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.Services_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.Shop_Adapter_Dashboard;
import com.example.nearbyme.User_Dashboard.fragment_notifications;
import com.example.nearbyme.admin_panel.admin_panel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_dashboard extends Fragment implements fragment_login.GetUserIdInterface, View.OnClickListener {
    private ToggleButton sw_Home_Shop;
    private FloatingActionButton fab_notification;
    private TextView tv_notification_badge;
    private TabLayout tab_dashboard;
    private RecyclerView mRecyclerView;
    private List<Home_info> mHomeList;
    private List<Shop_info> mShopList;
    private List<Restaurant_info> mResList;
    private List<Home_Store_info> mHSList;
    private List<Announcement_info> mAnnList;
    private List<Item_info> mItemList;
    private List<Service_info> mServiceList;

    private Home_Adapter_Dashboard mHomeAdapter;
    private Shop_Adapter_Dashboard mShopAdapter;
    private Restaurant_Adapter_Dashboard mResAdapter;
    private HomeStore_Adapter_Dashboard mHSAdapter;
    private Services_Adapter_Dashboard mServiceAdapter;
    private Item_Adapter_Dashboard mItemAdapter;
    private Announcement_Adapter_Dashboard mAnnAdapter;
    private FirebaseFirestore mDBRef;
    private String user_id = null;
    private boolean isHome = true;
    private int new_notification=0;


    public fragment_dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Log.d("TAG", "before check login");
        mDBRef = FirebaseFirestore.getInstance();
        checkLogin();
        ((MainActivity) requireActivity()).setActionBarTitle("Dashboard");
        InitViews(view);
        //    tv_notification_badge = view.findViewById(R.id.tv_notification_badge);
        Log.d("TAG", "Before GetNotification");
   //     getNotificationCount();
        sw_Home_Shop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (sw_Home_Shop.isChecked()) {
                isHome = false;
                getShops();

            } else {
                isHome = true;
                getHomes();

            }
        });
        getHomes();


        tab_dashboard.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    sw_Home_Shop.setVisibility(View.VISIBLE);
                } else {
                    sw_Home_Shop.setVisibility(View.INVISIBLE);
                }
                switch (tab.getPosition()) {

                    case 0:
                        if (isHome) {
                            getHomes();
                        } else {
                            getShops();
                        }


                        Log.d("TAG", "Home shop selected");
                        break;
                    case 1:
                        getRestaurant();
                        Log.d("TAG", "restaurant selected");

                        break;
                    case 2:
                        getServices();
                        Log.d("TAG", "services selected");

                        break;
                    case 3:
                        getItems();
                        Log.d("TAG", "sell selected");

                        break;
                    case 4:
                        getHomeStore();
                        Log.d("TAG", "Home store selected");

                        break;
                    case 5:
                        getAnnouncements();

                        Log.d("TAG", "announcement selected");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void getNotificationCount() {
        new_notification = 0;
      //  Log.d("TAG", "User id in getnotification is" + user_id);
        mDBRef.collection("users").document(user_id).collection("notifications").whereEqualTo("read",false)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                    new_notification = new_notification + 1;
                    Log.d("TAG","New notifications ="+new_notification);

                }
                if (new_notification != 0) {
                    tv_notification_badge.setVisibility(View.VISIBLE);
                    tv_notification_badge.setText(String.valueOf(new_notification));
                } else {
                    tv_notification_badge.setVisibility(View.GONE);
                }
              //  Log.d("TAG", "no of notifications =" + new_notification);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "failed to load notifications" + e.getMessage());
            }
        });
    }

    private void checkLogin() {
        Log.d("TAG", "in check login");
        SharedPreferences checkUser = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        user_id = checkUser.getString(getString(R.string.DOCUMENT_ID), "");
        String privilege=checkUser.getString(getString(R.string.M_PRIVILEGE),"");
        if (!(user_id.equals(""))) {
            if(privilege.equals("admin")){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new admin_panel()).commit();
            }
            getNotificationCount();
        }
        if (user_id.equals("")) {
            Log.d("TAG", "user id is empty");

            fragment_login login = new fragment_login();
            login.setTargetFragment(fragment_dashboard.this, 8);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, login)
                    .addToBackStack(null).commit();

        }
    }


    private void InitViews(View view) {
        tab_dashboard = view.findViewById(R.id.tab_dashboard);
        fab_notification = view.findViewById(R.id.fab_notifications_dashboard);
        fab_notification.setOnClickListener(this);

        tv_notification_badge = view.findViewById(R.id.tv_notification_badge);

        mRecyclerView = view.findViewById(R.id.rv_dashboard);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeList = new ArrayList<>();
        mShopList = new ArrayList<>();
        mResList = new ArrayList<>();
        mAnnList = new ArrayList<>();
        mHSList = new ArrayList<>();
        mItemList = new ArrayList<>();
        mServiceList = new ArrayList<>();


        sw_Home_Shop = ((MainActivity) requireActivity()).findViewById(R.id.sw_homeshop);
        sw_Home_Shop.setVisibility(View.VISIBLE);

    }

    private void getHomes() {
        mHomeAdapter = new Home_Adapter_Dashboard(getContext(), mHomeList);
        mRecyclerView.setAdapter(mHomeAdapter);
        mDBRef.collection("homes").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Home_info home = qds.toObject(Home_info.class);
                            home.setHome_id(id);
                            mHomeList.add(home);
                        }
                        mHomeAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getShops() {

        mShopAdapter = new Shop_Adapter_Dashboard(getContext(), mShopList);
        mRecyclerView.setAdapter(mShopAdapter);
        mDBRef.collection("shop").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Shop_info shop = qds.toObject(Shop_info.class);
                            shop.setShop_id(id);
                            mShopList.add(shop);
                        }
                        mShopAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void getRestaurant() {
        mResAdapter = new Restaurant_Adapter_Dashboard(mResList, getContext());

        mRecyclerView.setAdapter(mResAdapter);
        mDBRef.collection("restaurants").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Restaurant_info restaurant = qds.toObject(Restaurant_info.class);
                            restaurant.setRes_id(id);
                            mResList.add(restaurant);
                        }
                        mResAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getHomeStore() {
        mHSAdapter = new HomeStore_Adapter_Dashboard(getContext(), mHSList);
        mRecyclerView.setAdapter(mHSAdapter);
        mDBRef.collection("store").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Home_Store_info HS = qds.toObject(Home_Store_info.class);
                            HS.setHomestore_id(id);
                            mHSList.add(HS);
                        }
                        mHSAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void getServices() {
        if (mServiceList.isEmpty()) {
            mServiceAdapter = new Services_Adapter_Dashboard(mServiceList, getContext());
            mRecyclerView.setAdapter(mServiceAdapter);
            mDBRef.collection("services").whereEqualTo("user_id", user_id).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                String id = qds.getId();
                                Service_info service = qds.toObject(Service_info.class);
                                service.setService_id(id);
                                mServiceList.add(service);
                            }
                            mServiceAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void getItems() {
        mItemAdapter = new Item_Adapter_Dashboard(getContext(), mItemList);
        mRecyclerView.setAdapter(mItemAdapter);
        mDBRef.collection("items").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Item_info item = qds.toObject(Item_info.class);
                            item.setItem_id(id);
                            mItemList.add(item);
                        }
                        mItemAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getAnnouncements() {
        mAnnAdapter = new Announcement_Adapter_Dashboard(getContext(), mAnnList);
        mRecyclerView.setAdapter(mAnnAdapter);
        mDBRef.collection("announcements").whereEqualTo("user_id", user_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String id = qds.getId();
                            Announcement_info ann = qds.toObject(Announcement_info.class);
                            ann.setAnn_id(id);
                            mAnnList.add(ann);
                        }
                        mAnnAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void OnUidGet(String u_id) {
        user_id = u_id;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_notifications_dashboard) {
        Log.d("TAG","btn Notifications clicked");
            Bundle bundle = new Bundle();
            bundle.putString("u_id", user_id);
            fragment_notifications fn = new fragment_notifications();
            fn.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fn).addToBackStack(null).commit();
        }
    }
}
