package com.example.nearbyme;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_home_shop extends Fragment {
    FrameLayout search_control_container;

    public fragment_home_shop() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home_shop, container, false);
search_control_container=view.findViewById(R.id.container_search_control);
getChildFragmentManager().beginTransaction().add(R.id.container_search_control,new fragment_radius_control()).commit();


        return view;
    }
}
