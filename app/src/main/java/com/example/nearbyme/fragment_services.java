package com.example.nearbyme;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.Service_info;
import com.example.nearbyme.Model.Services_adapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_services extends Fragment implements OnMapReadyCallback,View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private SeekBar sb_Radius;
    private TextView edt_Radius;
    private Button btn_Search, btn_Find_Location;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private LatLng latlng;
    private Circle OnMapCircle;
    private FirebaseFirestore mDBRef;
    private double User_Lat = 0;
    private double User_Lon = 0;
    private double radius = 500;
    private RecyclerView mRecyclerView;
    private Services_adapter mServicesAdapter;
    private List<Service_info> mServicesList;
    ConstraintLayout constraintlayout;
    private boolean LocationGot = false;
    private int s_no;
    View view;

    public fragment_services() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Services");
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_services, container, false);

            InitViews(view);
            mServicesList = new ArrayList<>();
            mServicesAdapter = new Services_adapter(getContext(), mServicesList);
            mRecyclerView.setAdapter(mServicesAdapter);

            mDBRef = FirebaseFirestore.getInstance();

            mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().add(R.id.container_map_services, supportMapFragment).commit();
            supportMapFragment.getMapAsync(this); //calls on map ready
        }
   return view;
    }

    private void InitViews(View view) {
        sb_Radius = view.findViewById(R.id.sb_radius_services);
        sb_Radius.setOnSeekBarChangeListener(this);
        edt_Radius = view.findViewById(R.id.edt_radius_services);
        btn_Find_Location = view.findViewById(R.id.btn_find_location_services);
        btn_Search = view.findViewById(R.id.btn_search_services);
        btn_Find_Location.setOnClickListener(this);
        btn_Search.setOnClickListener(this);

        mRecyclerView = view.findViewById(R.id.rv_services);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        constraintlayout = view.findViewById(R.id.constraint_layout_services);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.btn_find_location_services:
           {
               sb_Radius.setEnabled(true);
               mLocationClient.getLastLocation().addOnCompleteListener((Task<Location> task) -> {

                   if (task.isSuccessful()) {
                       Location location = task.getResult();
                       if (location != null) {
                           LocationGot = true;
                           btn_Search.setText("SEARCH");
                           btn_Search.setTextColor(getResources().getColor(R.color.secondary));
                           //  btn_Find_Location.setTextColor(getResources().getColor(R.color.app_green));
                           User_Lat = location.getLatitude();
                           User_Lon = location.getLongitude();
                           mServicesAdapter.getHomeStoreLatLong(User_Lat, User_Lon);
                           mGoogleMap.setMyLocationEnabled(false);
                       } else {
                           btn_Find_Location.setText("First Select your location from map GPS button");
                           btn_Find_Location.setTextColor(getResources().getColor(R.color.app_red));
                           mGoogleMap.setMyLocationEnabled(true);
                       }
                       latlng = new LatLng(User_Lat, User_Lon);
                       CameraUpdate cameraUpdate;
                       cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 15.5f);
                       mGoogleMap.animateCamera(cameraUpdate);
                       //  mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
                       //  mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                       if (OnMapCircle != null) {
                           OnMapCircle.remove();
                       }

                       DrawCircle(500);
                       edt_Radius.setText("500");
                       addMarker();


                   }
               });
           }
               break;

            case R.id.btn_search_services:
                if(!ValidateLocationBtn()){
                    return;
                }
                mGoogleMap.clear();
                DrawCircle(radius);
                addMarker();
                mDBRef.collection("services").whereEqualTo("status",true).orderBy("service_type", Query.Direction.ASCENDING).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                mServicesList.clear();

                                s_no = 1;
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    String service_id=qds.getId();
                                    Service_info services = qds.toObject(Service_info.class);
                                    services.setService_id(service_id);
                                    double ResultLatitude = services.getLatitude();
                                    double ResultLongitude = services.getLongitude();
                                    float[] length = new float[2];
                                    Location.distanceBetween(User_Lat, User_Lon, ResultLatitude, ResultLongitude, length);
                                    float distance = length[0];
                                    if (distance <= radius) {

                                        TextView text = new TextView(getActivity());
                                        text.setText(String.valueOf(s_no));
                                        text.setTextColor(Color.BLACK);
                                        text.setTextSize(12);
                                        //  text.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
                                        IconGenerator generator = new IconGenerator(getActivity());
                                        generator.setBackground(getActivity().getDrawable(R.drawable.marker_icon_services));
                                        generator.setContentView(text);
                                        Bitmap icon = generator.makeIcon();

                                        //   MarkerOptions tp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(icon));
                                        //  googleMap.addMarker(tp);

                                        mGoogleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ResultLatitude, ResultLongitude))
                                                .title("home")
                                                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
                                        mServicesList.add(services);
                                        s_no++;
                                    }

                                }
                                if (mServicesList == null) {
                                    Toast.makeText(getContext(), "No Result Found,Try Increasing Radius", Toast.LENGTH_LONG).show();
                                }
                                if (mServicesList != null) {
                                    changeConstOfMap();

                                }
                                mServicesAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","failed to load service"+e.getMessage());
                        Toast.makeText(getContext(), "Failed to load data,Check your internet connection and try again", Toast.LENGTH_LONG).show();
                    }
                });

                break;


        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        radius = 500 + (progress * 100);
        edt_Radius.setText(radius + " m");

        if (latlng != null) {
            OnMapCircle.setRadius(radius);


        } else {
            sb_Radius.setEnabled(false);
            Toast.makeText(getContext(), "select your location first", Toast.LENGTH_LONG).show();
        }

        if (radius > 800 && radius <= 1400) {
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        } else if (radius > 1400 && radius <= 2900) {
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        } else if (radius > 2900 && radius <= 5800) {
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        } else if (radius > 5800 && radius <= 10500) {
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        } else {
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng LatLngDefault = new LatLng(30.3753, 69.3451);
        CameraUpdate defaultUpdate = CameraUpdateFactory.newLatLngZoom(LatLngDefault, 5.5f);
        mGoogleMap.moveCamera(defaultUpdate);
    }

    private void changeConstOfMap() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintlayout);
        constraintSet.connect(R.id.container_map_services, ConstraintSet.BOTTOM, R.id.rv_services, ConstraintSet.TOP, 0);
        constraintSet.applyTo(constraintlayout);
    }
    private void addMarker() {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(User_Lat, User_Lon))
                .title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_me))).showInfoWindow();
    }
    private void DrawCircle(double radius) {
        if (latlng != null) {

            OnMapCircle = mGoogleMap.addCircle(new CircleOptions()
                    .center(latlng)
                    .radius(radius)
                    .strokeWidth(1f)
                    .strokeColor(Color.parseColor("#fff4c20d"))
                    .fillColor(Color.parseColor("#66f4c20d")));
        }
    }
    private boolean ValidateLocationBtn(){
        if(!LocationGot){
            btn_Search.setText("Select Location First");
            btn_Search.setTextColor(getResources().getColor(R.color.app_red));
            return false;
        }
        else return true;

    }
    @Override
    public void onDestroyView() {
        if (view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }


}
