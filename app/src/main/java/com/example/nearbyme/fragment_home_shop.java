package com.example.nearbyme;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_home_shop extends Fragment implements OnMapReadyCallback , View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    SeekBar sb_Radius;
    EditText edt_Radius;
    ImageButton btn_Find_Location;
    Button btn_Search;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    LatLng latlng;
    private Circle OnMapCircle;




    public fragment_home_shop() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home_shop, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Home/Shop for rent");

       //initialization of views
        sb_Radius=view.findViewById(R.id.sb_radius);
        sb_Radius.setOnSeekBarChangeListener(this);
        edt_Radius=view.findViewById(R.id.edt_radius);
        btn_Find_Location=view.findViewById(R.id.btn_find_location);
        btn_Search=view.findViewById(R.id.btn_search);
        btn_Find_Location.setOnClickListener(this);
        btn_Search.setOnClickListener(this);

        mLocationClient= LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment supportMapFragment=SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.container_map,supportMapFragment).commit();
        supportMapFragment.getMapAsync(this); //calls on map ready

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(),"map is showing on screen",Toast.LENGTH_LONG).show();
mGoogleMap=googleMap;
mGoogleMap.setMyLocationEnabled(true);
mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_find_location:
            {sb_Radius.setEnabled(true);
                mLocationClient.getLastLocation().addOnCompleteListener((Task<Location> task)->{

if(task.isSuccessful()){
Location location=task.getResult();
double lat=location.getLatitude();
double lng=location.getLongitude();
    latlng = new LatLng(lat,lng);
    CameraUpdate cameraUpdate;
    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,15.5f);
    mGoogleMap.moveCamera(cameraUpdate);
    mGoogleMap.addMarker(new MarkerOptions()
            .position(new LatLng(lat,lng))
            .title("Me"));

    mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    if(OnMapCircle!=null){
        OnMapCircle.remove();
    }

    OnMapCircle=mGoogleMap.addCircle(new CircleOptions()
            .center(latlng)
            .radius(500)
            .strokeWidth(1f)
            .strokeColor(Color.parseColor("#fff4c20d"))
            .fillColor(Color.parseColor("#66f4c20d")));
    edt_Radius.setText("500");


}
                });
            }
            break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        double radius=500+(progress*100);
        edt_Radius.setText(radius+" m");

        if(latlng!=null){
            OnMapCircle.setRadius(radius);


        }else{
            sb_Radius.setEnabled(false);
            Toast.makeText(getContext(),"select your location first",Toast.LENGTH_LONG).show();
        }

        if(radius>800 && radius<=1400){
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        }else if(radius>1400 && radius<=2900){
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }else if(radius>2900 && radius<=5800){
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }else if(radius>5800 && radius<=10500){
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }else{}
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
