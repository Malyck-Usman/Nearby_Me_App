package com.example.nearbyme;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.zip.Inflater;

public class MapDialog extends DialogFragment implements OnMapReadyCallback {
    SharedPreferences sp;
    private GetLocationDialogInterface locationDialogInterface;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    View view;
    MapView mv;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();



    view = inflater.inflate(R.layout.map_dialog_layout,null);
    mLocationClient= LocationServices.getFusedLocationProviderClient(getActivity());



        builder.setView(view)
                .setTitle("Choose Location")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).setPositiveButton("Get Location from Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
mLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
    @Override
    public void onSuccess(Location location) {
        double lat=location.getLatitude();
        double lng=location.getLongitude();
        locationDialogInterface.OnLocationGet(lat,lng);
//        SharedPreferences.Editor My_location=getActivity().getSharedPreferences(getString(R.string.M_LOCATION_FILE),Context.MODE_PRIVATE).edit();
//        My_location.putString(getString(R.string.LAT),String.valueOf(lat));
//        My_location.putString(getString(R.string.LNG),String.valueOf(lng));
//        My_location.apply();

        Toast.makeText(getContext(),"Lat="+lat+"Lng="+lng,Toast.LENGTH_LONG).show();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getContext(),"Failed to get Location,Check your GPS and try again",Toast.LENGTH_LONG).show();
    }
});

            }
        });


mv=view.findViewById(R.id.map_fragment);
mv.onCreate(savedInstanceState);
mv.getMapAsync(this);

       return builder.create();

  }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(getActivity(),"Map is showing on screen",Toast.LENGTH_SHORT).show();


    }




    @Override
    public void onStart() {
        super.onStart();
        mv.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mv.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mv.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mv.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mv.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }
    public interface GetLocationDialogInterface{
        void OnLocationGet(double lat,double lng);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {

        locationDialogInterface=(GetLocationDialogInterface) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement GetLocationDialogInterface");
        }
    }

}

