package com.example.nearbyme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nearbyme.Model.Announcement_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_add_announcement extends Fragment implements MapDialog.GetLocationDialogInterface,View.OnClickListener {
    TextInputLayout edt_Subject, edt_Description;
    Button btn_Save,btn_Ann_Location;
    private FirebaseFirestore mDBRef;
    private boolean isGetLocation=false;
    private double latitude=0;
    private double longitude=0;

    public fragment_add_announcement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_announcement, container, false);
        InitViews(view);
        mDBRef=FirebaseFirestore.getInstance();


        return view;
    }

    private void InitViews(View view) {
        edt_Subject=view.findViewById(R.id.edt_subject);
        edt_Description=view.findViewById(R.id.edt_announcement_description);
        btn_Save=view.findViewById(R.id.btn_save_announcement);
        btn_Save.setOnClickListener(this);
        btn_Ann_Location=view.findViewById(R.id.btn_ann_location);
        btn_Ann_Location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_announcement:
                if(!ValidateLocation()|!ValidateDescription()|!ValidateSubject()){
            return;
        }
        ////////////////////////////////////
        String key = "unihRmGR3h6UtXGi3HJ8";/////////
        ////////////////////////////////////
        String subject = edt_Subject.getEditText().getText().toString();
        String description = edt_Description.getEditText().getText().toString().trim();
        Announcement_info addAnnouncementInfo=new Announcement_info(key,latitude,longitude,subject,description);
        mDBRef.collection("announcements").document().set(addAnnouncementInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Announcement added successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","failed to add announcement"+e.getMessage());
            }
        });
                break;

            case R.id.btn_ann_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_announcement.this, 3);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;

        }
    }
    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Ann_Location.setText("Location Required,Click To Get Location");
            btn_Ann_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }


    private boolean ValidateSubject() {
        String Subject = edt_Subject.getEditText().getText().toString().trim();
        if (Subject.length() > 25) {
            edt_Subject.setError("Subject Text Exceeded Limit  ");

        } else if (Subject.isEmpty()) {
            edt_Subject.setError("Subject is Required");
        } else {
            edt_Subject.setError(null);
            return true;
        }
        return false;
    }
    private boolean ValidateDescription() {
        String Description = edt_Description.getEditText().getText().toString().trim();
        if (Description.length() > 300) {
            edt_Description.setError("Description Text Exceeded Limit");

        } else if (Description.isEmpty()) {
            edt_Description.setError("Description is  Required");
        } else {
            edt_Description.setError(null);
            return true;
        }
        return false;
    }

    @Override
    public void OnLocationGet(double lat, double lng) {
        isGetLocation = true;
        latitude = lat;
        longitude = lng;
        btn_Ann_Location.setText("Location Saved");
        btn_Ann_Location.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
