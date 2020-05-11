package com.example.nearbyme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private DrawerLayout navigationdrawer;
Toolbar toolbar;
    private boolean mLocationPermissionGranted;
    public static final int PERMISSION_REQUEST_CODE=9001;
    private final int PLAY_SERVICES_ERROR_CODE=9002;
    private final int GPS_REQUEST_CODE=9003;
    String TAG="This";



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

            //////////////


        if(isServicesOk()){
            Log.d(TAG,"services ok");
            if(isGPSEnabled()){
                Log.d(TAG,"gps is enabeled");
                if(CheckLocationPermission()){
                    Log.d(TAG,"Ready to map");
                    Toast.makeText(this,"Ready to map ",Toast.LENGTH_LONG).show();

                }
                else{
                    requestLocationPermission();
                 }

            }
        }

      //////////////////



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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home()).commit();
                break;

            case R.id.nav_services:
                setTitle("services");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_services()).commit();
                break;
            case R.id.nav_add_services:
                setTitle("add services");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_services()).commit();
                break;

            case R.id.nav_homeshop:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home_shop()).commit();
                break;
            case R.id.nav_add_home_shop:
                setTitle("add home shop");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_home_shop()).commit();
                break;

            case R.id.nav_restaurents:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_restaurant_hotel()).commit();
                break;
            case R.id.nav_add_restaurant:
                setTitle("add restaurants");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_restaurants()).commit();
                break;

            case R.id.nav_buysell:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_buy_sell()).commit();
                break;
            case R.id.nav_add_Item:
                setTitle("add Item");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_Item()).commit();
                break;

            case R.id.nav_homestores:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_homestores()).commit();
                break;
            case R.id.nav_add_home_store:
                setTitle("add home store");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_homestore()).commit();
                break;

            case R.id.nav_announcement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_announcement()).commit();
                break;
            case R.id.nav_add_announcement:
                setTitle("add announcements");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_add_announcement()).commit();
                break;


            default:
                break;
        }
        navigationdrawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private boolean  isServicesOk() {
        GoogleApiAvailability googleApi=GoogleApiAvailability.getInstance();
        int result=googleApi.isGooglePlayServicesAvailable(this);
        if(result== ConnectionResult.SUCCESS){
            return true;
        }
        else if(googleApi.isUserResolvableError(result)){
            Dialog dialog=googleApi.getErrorDialog(this,result,PLAY_SERVICES_ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface task) {
                    Toast.makeText(getApplicationContext(), "dialog is cancelled by User", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }else {
            Toast.makeText(this,"Play services are required by this application",Toast.LENGTH_LONG).show();
        }
        return false;}
    private boolean isGPSEnabled(){
        LocationManager locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabeled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabeled){
            return true;
        }
        else
        {
            AlertDialog alertDialog=new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("Gps is required for this app to work,please enable GPS")
                    .setPositiveButton("yes",(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);


                        }
                    }))
                    .setCancelable(false)
                    .show();
        }



        return false;
    }
    private boolean CheckLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED;
    }
    private void requestLocationPermission(){
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"permissions not granted2");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted=true;
            Log.d(TAG,"permission granted");
            Toast.makeText(this,"permission granted",Toast.LENGTH_LONG).show();
        }else {
            Log.d(TAG,"permission denied");
            Toast.makeText(this,"permission Denied",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GPS_REQUEST_CODE){
            if(isGPSEnabled()){
                Toast.makeText(this,"GPS is Enabeled",Toast.LENGTH_LONG).show();
                //google map here

            }
            else {
                Toast.makeText(this,"GPS Not Enabeled, Unable to show user location",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }
}
