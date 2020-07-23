package com.example.nearbyme.admin_panel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nearbyme.MainActivity;
import com.example.nearbyme.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class admin_panel extends Fragment {
    TabLayout Tab_Admin;

    public admin_panel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Dashboard");

        View view= inflater.inflate(R.layout.fragment_admin_panel, container, false);

        InitViews(view);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,new fragment_all_users()).commit();

        Tab_Admin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,new fragment_all_users()).commit();
                }else if(tab.getPosition()==1){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,new fragment_allowed_users()).commit();

                }else if(tab.getPosition()==2){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,new fragment_blocked_users()).commit();
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

    private void InitViews(View view) {
        Tab_Admin=view.findViewById(R.id.tab_admin);
    }
}
