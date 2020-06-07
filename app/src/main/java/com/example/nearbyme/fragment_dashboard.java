package com.example.nearbyme;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.nearbyme.Model.Announcement_info;
import com.example.nearbyme.Model.HomeStore_Adapter;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
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
public class fragment_dashboard extends Fragment implements fragment_login.GetUserIdInterface {
    ToggleButton sw_Home_Shop;

    private TabLayout tab_Admin;
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


    public fragment_dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        checkLogin();
        ((MainActivity) getActivity()).setActionBarTitle("Dashboard");
        InitViews(view);
        mDBRef = FirebaseFirestore.getInstance();


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


        tab_Admin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void checkLogin() {
        SharedPreferences checkUserId = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        user_id = checkUserId.getString(getString(R.string.DOCUMENT_ID), "");
        if (user_id.equals("")) {
            fragment_login login = new fragment_login();
            login.setTargetFragment(fragment_dashboard.this, 8);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, login)
                    .addToBackStack(null).commit();

        }
    }





    private void InitViews(View view) {
        tab_Admin = view.findViewById(R.id.tab_admin);
//        tab_Announcement=view.findViewById(R.id.tab_announcement);
//        tab_Home_Shop=view.findViewById(R.id.tab_home_shop);
//       tab_Homestore =view.findViewById(R.id.tab_homestore);
//       tab_Restaurant=view.findViewById(R.id.tab_restaurant);
//       tab_Sell=view.findViewById(R.id.tab_sell);
//       tab_Services=view.findViewById(R.id.tab_service);
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
        user_id=u_id;
    }
}
