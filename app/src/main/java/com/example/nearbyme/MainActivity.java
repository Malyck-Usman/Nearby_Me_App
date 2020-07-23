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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nearbyme.Model.User;
import com.example.nearbyme.admin_panel.admin_panel;
import com.example.nearbyme.admin_panel.fragment_admin_user_adds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        View.OnClickListener {


    TextView nav_user_name;
    private DrawerLayout navigationdrawer;
    Toolbar toolbar;
    private boolean mLocationPermissionGranted;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    private final int PLAY_SERVICES_ERROR_CODE = 9002;
    private final int GPS_REQUEST_CODE = 9003;
    String TAG = "This";
    ToggleButton sw_homeShop;
    private FirebaseFirestore mdbref;
    View headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);//added
        sw_homeShop = findViewById(R.id.sw_homeshop);
        sw_homeShop.setVisibility(View.INVISIBLE);

        //to add navigation drawer toggle icon
        navigationdrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                navigationdrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        navigationdrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


///
        headerView = navigationView.getHeaderView(0);
        ShowUser();

        //code for default home item selected
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        //////////////


        if (isServicesOk()) {
            Log.d(TAG, "services ok");
            if (isGPSEnabled()) {
                Log.d(TAG, "gps is enabeled");
                if (CheckLocationPermission()) {
                    Log.d(TAG, "Ready to map");

                } else {
                    requestLocationPermission();
                }

            }
        }

        //////////////////


    }


    @Override //for open drawer to close it on back press and close activity if drawer is closed
    public void onBackPressed() {
        Fragment fragInFrame = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (navigationdrawer.isDrawerOpen(GravityCompat.START)) {
            navigationdrawer.closeDrawer(GravityCompat.START);
//        }else if (fragInFrame instanceof fragment_detail_restaurant){
//            super.onBackPressed();
//           Fragment f = getSupportFragmentManager().findFragmentByTag("ResDetail");
//            getSupportFragmentManager().beginTransaction().remove(f).commit();

        } else if (fragInFrame instanceof fragment_home_shop
                || fragInFrame instanceof fragment_add_home_shop
                || fragInFrame instanceof fragment_homestores
                || fragInFrame instanceof fragment_add_homestore
                || fragInFrame instanceof fragment_services
                || fragInFrame instanceof fragment_add_services
                || fragInFrame instanceof fragment_buy_sell
                || fragInFrame instanceof fragment_add_Item
                || fragInFrame instanceof fragment_restaurant
                || fragInFrame instanceof fragment_add_restaurants
                || fragInFrame instanceof fragment_announcement
                || fragInFrame instanceof fragment_add_announcement
                || fragInFrame instanceof fragment_dashboard
                || fragInFrame instanceof fragment_login


        ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();

        } else if (fragInFrame instanceof fragment_home) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit Nearby Me")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
//
//        else{
//            if(!(fragInFrame instanceof fragment_home )){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_home()).commit();
//            }
        else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_homeshop || item.getItemId() == R.id.nav_add_home_shop) {
            sw_homeShop.setVisibility(View.VISIBLE);
        } else {
            sw_homeShop.setVisibility(View.INVISIBLE);
        }
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
                break;

            case R.id.nav_services:
                setTitle("services");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_services()).commit();
                break;
            case R.id.nav_add_services:
                setTitle("add services");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_services()).commit();
                break;

            case R.id.nav_homeshop:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_shop()).commit();
                break;
            case R.id.nav_add_home_shop:
                setTitle("add home shop");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_home_shop()).commit();
                break;

            case R.id.nav_restaurents:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_restaurant()).commit();
                break;
            case R.id.nav_add_restaurant:
                setTitle("add restaurants");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_restaurants()).commit();
                break;

            case R.id.nav_buysell:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_buy_sell()).commit();
                break;
            case R.id.nav_add_Item:
                setTitle("add Item");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_Item()).commit();
                break;

            case R.id.nav_homestores:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_homestores()).commit();
                break;
            case R.id.nav_add_home_store:
                setTitle("add home store");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_homestore()).commit();
                break;

            case R.id.nav_announcement:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_announcement()).commit();
                break;
            case R.id.nav_add_announcement:
                setTitle("add announcements");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_add_announcement()).commit();
                break;
            case R.id.nav_user_dashboard:
                setTitle("Dashboard");

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_dashboard()).commit();
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


    private boolean isServicesOk() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int result = googleApi.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface task) {
                    Toast.makeText(getApplicationContext(), "dialog is cancelled by User", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "Play services are required by this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabeled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabeled) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("Gps is required for this app to work,please enable GPS")
                    .setPositiveButton("yes", (new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);


                        }
                    }))
                    .setCancelable(false)
                    .show();
        }


        return false;
    }

    private boolean CheckLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permissions not granted2");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Log.d(TAG, "permission granted");
            Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "permission denied");
            Toast.makeText(this, "permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            if (isGPSEnabled()) {
                Toast.makeText(this, "GPS is Enabeled", Toast.LENGTH_LONG).show();
                //google map here

            } else {
                Toast.makeText(this, "GPS Not Enabeled, Unable to show user location", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void ShowUser() {

        nav_user_name = (TextView) headerView.findViewById(R.id.tv_nav_username);
        nav_user_name.setVisibility(View.INVISIBLE);
        SharedPreferences sp = getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        String user_id = sp.getString(getString(R.string.DOCUMENT_ID), "");
        Log.d("TAG", "document id " + user_id);
        if (!user_id.equals("")) {
            mdbref = FirebaseFirestore.getInstance();
            mdbref.collection("users").document(user_id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!(documentSnapshot.exists())) {
                                SharedPreferences.Editor edit=getSharedPreferences(getString(R.string.M_LOGIN_FILE),MODE_PRIVATE).edit();
                                edit.putString(getString(R.string.DOCUMENT_ID),"");
                                edit.apply();
                                return;
                            }
                            User user = documentSnapshot.toObject(User.class);
                            nav_user_name.setVisibility(View.VISIBLE);
                            nav_user_name.setText(user.getUser_name());
                        }
                    });
            nav_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(MainActivity.this, v);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_dashboard, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.logout) {
                                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE).edit();
                                editor.putString(getString(R.string.DOCUMENT_ID), "");
                                editor.apply();
                                nav_user_name.setVisibility(View.INVISIBLE);


                            } else if (item.getItemId() == R.id.delete) {
                                Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Delete Account");
                                builder.setMessage("Are you sure you want to delete the account and all the data associated with it ?" +
                                        "Once Deleted cannot be undone!!!");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Cancel", null);
                                builder.setPositiveButton("ok", null);
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mdbref.collection("announcements").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("announcements").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        /////////////////////del home
                                        mdbref.collection("homes").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("homes").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        ////////////////////items
                                        mdbref.collection("items").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("items").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        ///////////////////restaurants
                                        mdbref.collection("restaurants").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("restaurants").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        //////////////////services
                                        mdbref.collection("services").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("services").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        /////////////////shop
                                        mdbref.collection("shop").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("shop").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        ////////////////store
                                        mdbref.collection("store").whereEqualTo("user_id", user_id)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                                    if (!qds.getId().equals("")) {

                                                        mdbref.collection("store").document(qds.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("TAG", "Deleted document " + qds.getId());
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                                        ///////////////user
                                        mdbref.collection("users").document(user_id).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Account Delete Successful", Toast.LENGTH_LONG).show();
                                                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE).edit();
                                                        editor.putString(getString(R.string.DOCUMENT_ID), "");
                                                        editor.apply();
                                                        nav_user_name.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                    }
                                });
                                builder.create();
                                builder.show();

                            }
                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }
            });
        }

    }


}
