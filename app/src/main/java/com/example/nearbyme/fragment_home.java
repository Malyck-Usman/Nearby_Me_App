package com.example.nearbyme;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class fragment_home extends Fragment implements View.OnClickListener {
 CardView CdvHomeShop,CdvHomeStore,CdvRestaurentHotels,CdvBuySell,CdvAnnouncement,CdvServices;
 FloatingActionButton fab_Add,fab_Home_Shop,fab_Restaurents,fab_Services,fab_buy_sell,fab_Home_Store,fab_Announcement;
 float TranslationY=100f;
 float TranslationX=100f;
 private Boolean isMenuOpen=false;
 OvershootInterpolator interpolator=new OvershootInterpolator();
 FragmentTransaction transection;
   // FrameLayout Main_container;

    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
  //Main_container= ((MainActivity)getActivity()).findViewById(R.id.fragment_container);
       initCardViewMenu(view);
       initFab(view);
//View view1=getActivity().findViewById(R.id.fragment_container);
//Main_container=(FrameLayout) view1;
        ((MainActivity) getActivity()).setActionBarTitle("Nearby Me");

        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cdvHomeShop:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_shop()).commit();
                break;

            case R.id.cdvRestaurantsHotels:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_restaurant_hotel()).commit();
                break;
            case R.id.cdvServices:
     getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_services()).commit();
                break;
            case R.id.cdvBuySell:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_buy_sell()).commit();
                break;
            case R.id.cdvHomeStores:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_homestores()).commit();
                break;
            case R.id.cdvAnnouncements:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_announcement()).commit();
                break;
                //cases for FAB

           case  R.id.fab_add:
            if(isMenuOpen){
                closeMenu();
            }else{
                openMenu();
            }
                break;
           case  R.id.fab_home_shop:
               getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_home_shop()).commit();

            break;
            case R.id.fab_restaurants:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_restaurants()).commit();

              //  getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_restaurants()).commit();

                break;
            case R.id.fab_services:

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_services()).commit();

                break;
            case R.id.fab_buy_sell:


                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_Item()).commit();

                break;
            case R.id.fab_home_store:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_homestore()).commit();

                break;
            case R.id.fab_announcements:
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_signup()).commit();

                break;

                default:
                break;
        }



    }
    private void initCardViewMenu(View view){
        CdvHomeShop=view.findViewById(R.id.cdvHomeShop);
        CdvHomeShop.setOnClickListener(this);
        CdvRestaurentHotels=view.findViewById(R.id.cdvRestaurantsHotels);
        CdvRestaurentHotels.setOnClickListener(this);
        CdvServices=view.findViewById(R.id.cdvServices);
        CdvServices.setOnClickListener(this);
        CdvBuySell=view.findViewById(R.id.cdvBuySell);
        CdvBuySell.setOnClickListener(this);
        CdvHomeStore=view.findViewById(R.id.cdvHomeStores);
        CdvHomeStore.setOnClickListener(this);
        CdvAnnouncement=view.findViewById(R.id.cdvAnnouncements);
        CdvAnnouncement.setOnClickListener(this);
    }
    private void initFab(View view){
        fab_Add=view.findViewById(R.id.fab_add);
        fab_Home_Store=view.findViewById(R.id.fab_home_store);
        fab_Announcement=view.findViewById(R.id.fab_announcements);
        fab_Services=view.findViewById(R.id.fab_services);
        fab_Restaurents=view.findViewById(R.id.fab_restaurants);
        fab_Home_Shop=view.findViewById(R.id.fab_home_shop);
        fab_buy_sell=view.findViewById(R.id.fab_buy_sell);
        fab_Add.setOnClickListener(this);
        fab_Home_Shop.setOnClickListener(this);
        fab_Restaurents.setOnClickListener(this);
        fab_Services.setOnClickListener(this);
        fab_buy_sell.setOnClickListener(this);
        fab_Home_Store.setOnClickListener(this);
        fab_Announcement.setOnClickListener(this);
        fab_Add.setAlpha(0.75f);
        fab_Home_Shop.setAlpha(0f);
        fab_Restaurents.setAlpha(0f);
        fab_Services.setAlpha(0f);
        fab_buy_sell.setAlpha(0f);
        fab_Home_Store.setAlpha(0f);
        fab_Announcement.setAlpha(0f);

    }
    private void openMenu(){
        isMenuOpen= !isMenuOpen;
        fab_Add.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fab_Home_Shop.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_Restaurents.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_Services.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_buy_sell.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_Home_Store.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_Announcement.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();



    }
    private void closeMenu(){
        isMenuOpen= !isMenuOpen;
        fab_Add.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_Home_Shop.animate().translationY(TranslationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_Restaurents.animate().translationY(TranslationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_Services.animate().translationY(TranslationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_buy_sell.animate().translationX(TranslationX).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_Home_Store.animate().translationX(TranslationX).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_Announcement.animate().translationX(TranslationX).alpha(0f).setInterpolator(interpolator).setDuration(300).start();



    }

}
