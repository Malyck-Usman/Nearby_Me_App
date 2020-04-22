package com.example.nearbyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout navigationdrawer;
Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //to add navigation drawer toggle icon
        navigationdrawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,
                navigationdrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        navigationdrawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            //code for default home item selected
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
     }
     public void setToolbarTitle(String title)
     {
         toolbar.setTitle(title);
         setSupportActionBar(toolbar);

     }
    @Override //for open drawer to close it on back press and close activity if drawer is closed
    public void onBackPressed() {
        if(navigationdrawer.isDrawerOpen(GravityCompat.START))
        {
            navigationdrawer.closeDrawer(GravityCompat.START);
        }else{
            Fragment fragInFrame=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(!(fragInFrame instanceof fragment_home )){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home()).commit();
            }
            else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                //home fragment here

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home()).commit();

                break;

            case R.id.nav_services:
                //services fragment here
               // setToolbarTitle("services");
                setTitle("services");


                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_services()).commit();

                    break;

            case R.id.nav_homeshop:
                //homeshop fragment here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home_shop()).commit();
                break;

            case R.id.nav_restaurents:
                //restaurent fragment here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_restaurant_hotel()).commit();
                break;
            case R.id.nav_buysell:
                //buysell fragment here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_buy_sell()).commit();
                break;

            case R.id.nav_homestores:
                //home store fragment here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_homestores()).commit();
                break;

            case R.id.nav_announcement:
                //announcement fragment here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_announcement()).commit();
                break;


            default:
                break;
        }
        navigationdrawer.closeDrawer(GravityCompat.START);


        return true;
    }
}
