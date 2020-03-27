package com.example.nearbyme;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


public class fragment_home extends Fragment implements View.OnClickListener {
 CardView CdvHomeShop,CdvHomeStore,CdvRestaurentHotels,CdvBuySell,CdvAnnouncement,CdvServices;

    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
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



        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cdvHomeShop:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_shop()).commit();
                break;

            case R.id.cdvRestaurantsHotels:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_restaurant_hotel()).commit();
                break;
            case R.id.cdvServices:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_services()).commit();
                break;
            case R.id.cdvBuySell:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_buy_sell()).commit();
                break;
            case R.id.cdvHomeStores:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_homestores()).commit();
                break;
            case R.id.cdvAnnouncements:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_announcement()).commit();
                break;
            default:
                break;
        }



    }
}
