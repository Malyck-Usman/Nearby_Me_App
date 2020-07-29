package com.example.nearbyme;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_add_announcement extends Fragment implements MapDialog.GetLocationDialogInterface, View.OnClickListener, fragment_login.GetUserIdInterface {
    TextInputLayout edt_Subject, edt_Description;
    Button btn_Save, btn_Ann_Location;
    private FirebaseFirestore mDBRef;
    private DocumentReference mDocRef;
    private boolean isGetLocation = false;
    private double latitude = 0;
    private double longitude = 0;
    private String ann_id = null;
    private String user_id = null;


    public fragment_add_announcement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);




        checkLogin();

        InitViews(view);
        mDBRef = FirebaseFirestore.getInstance();
        CheckBundle();


        return view;
    }

    private void checkLogin() {
        SharedPreferences checkUserId = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        user_id = checkUserId.getString(getString(R.string.DOCUMENT_ID), "");
        if (user_id.equals("")) {
            fragment_login login = new fragment_login();
            login.setTargetFragment(fragment_add_announcement.this, 1);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, login)
                    .addToBackStack(null).commit();

        }
    }

    private void CheckBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            ann_id = bundle.getString("ann_id", null);
            if (ann_id != null) {

                Log.d("TAG", "Home id is" + ann_id);

                mDBRef.collection("announcements").document(ann_id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Announcement_info ann = documentSnapshot.toObject(Announcement_info.class);
                                edt_Subject.getEditText().setText(ann.getSubject());
                                edt_Description.getEditText().setText(ann.getDescription());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }

    }

    private void InitViews(View view) {
        edt_Subject = view.findViewById(R.id.edt_subject);
        edt_Description = view.findViewById(R.id.edt_announcement_description);
        btn_Save = view.findViewById(R.id.btn_save_announcement);
        btn_Save.setOnClickListener(this);
        btn_Ann_Location = view.findViewById(R.id.btn_ann_location);
        btn_Ann_Location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_announcement:
                if (!ValidateLocation() | !ValidateDescription() | !ValidateSubject()) {
                    return;
                }

                String subject = edt_Subject.getEditText().getText().toString();
                String description = edt_Description.getEditText().getText().toString().trim();
                Announcement_info addAnnouncementInfo = new Announcement_info(user_id, latitude, longitude, subject, description,true);
                if (ann_id != null) {
                    mDocRef = mDBRef.collection("announcements").document(ann_id);
                } else {
                    mDocRef = mDBRef.collection("announcements").document();

                }

                mDocRef.set(addAnnouncementInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_dashboard()).commit();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "failed to add announcement" + e.getMessage());
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
        if (Subject.length() > 40) {
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
        if (Description.length() < 80) {
            edt_Description.setError("Description Required Min 80 Characters");

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
        btn_Ann_Location.setTextColor(getResources().getColor(R.color.ColorPrimary));
    }

    @Override
    public void OnUidGet(String u_id) {
        Log.d("TAG", "User Id from interface is =" + u_id);
        user_id = u_id;
    }
}
