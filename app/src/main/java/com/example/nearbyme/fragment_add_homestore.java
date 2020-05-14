package com.example.nearbyme;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nearbyme.Model.Home_Store_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragment_add_homestore extends Fragment implements View.OnClickListener,MapDialog.GetLocationDialogInterface {
    Spinner sp_Store_Type;
    TextInputLayout edt_Store_Name, edt_Description;
    EditText edt_Store_Opening_Time,edt_Store_Closing_Time;
    Button btn_Save,btn_Home_store_Location;
    TextView errorText;
    String[] Store_type = {"-Select Store Type-", "Garments", "Grocery", "Handicraft", "Decoration", "Spare Parts"};
    private int opening_hour, opening_minute, closing_hour, closing_minute;
private FirebaseFirestore mDBRef;
    private boolean isGetLocation=false;
    private double latitude=0;
    private double longitude=0;
    public fragment_add_homestore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_homestore, container, false);
        initViews(view);
        edt_Store_Opening_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    opening_hour = sHour;
                                    opening_minute = sMinute;
                                    edt_Store_Opening_Time.setText(sHour + ":" + sMinute);
                                }
                            }, 0, 0, true);
                    picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    picker.show();
                }
            }
        });
        edt_Store_Closing_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    closing_hour = sHour;
                                    closing_minute = sMinute;
                                    edt_Store_Closing_Time.setText(sHour + ":" + sMinute);
                                }
                            }, 23, 59, true);
                    picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    picker.show();
                }
            }
        });
mDBRef=FirebaseFirestore.getInstance();

        return view;
    }

    private void initViews(View view) {
        edt_Store_Name = view.findViewById(R.id.edt_store_name);
        edt_Store_Opening_Time = view.findViewById(R.id.edt_store_opening_time);
        edt_Store_Closing_Time = view.findViewById(R.id.edt_store_closing_time);
        edt_Description = view.findViewById(R.id.edt_store_description);

        sp_Store_Type = view.findViewById(R.id.sp_store_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Store_type);
        sp_Store_Type.setAdapter(adapter);
        btn_Save = view.findViewById(R.id.btn_save_store);
        btn_Save.setOnClickListener(this);
        btn_Home_store_Location=view.findViewById(R.id.btn_home_store_location);
        btn_Home_store_Location.setOnClickListener(this);


    }
    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Home_store_Location.setText("Location Required,Click To Get Location");
            btn_Home_store_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }

    private boolean ValidateStoreType() {
        errorText = (TextView) sp_Store_Type.getSelectedView();
        if (sp_Store_Type.getSelectedItemPosition() == 0) {
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.app_red));//just to highlight that this is an error
            errorText.setText("Please Select a Store Type");
        } else {
            if (errorText.getText().length() > 0) {

                errorText.setText("");
            }
            return true;
        }
        return false;
    }
    private boolean ValidateStoreName() {
        String ResName = edt_Store_Name.getEditText().getText().toString().trim();
        if (ResName.length() > 25) {
            edt_Store_Name.setError("Name Too Long");

        } else if (ResName.isEmpty()) {
            edt_Store_Name.setError("Name Required");
        } else {
            edt_Store_Name.setError(null);
            return true;
        }
        return false;
    }


    private boolean ValidateOpeningTime() {
        String openingTime = edt_Store_Opening_Time.getText().toString().trim();
        if (openingTime.isEmpty()) {
            edt_Store_Opening_Time.setError("Opening Time Required");
        } else if (openingTime.length() > 5) {
            edt_Store_Opening_Time.setError("Invalid time, Select again");
        } else {
            edt_Store_Opening_Time.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateClosingTime() {
        String closingTime = edt_Store_Closing_Time.getText().toString().trim();
        if (closingTime.isEmpty()) {
            edt_Store_Closing_Time.setError("Closing Time Required");
        } else if (closingTime.length() > 5) {
            edt_Store_Closing_Time.setError("Invalid time, Select again");
        } else {
            edt_Store_Closing_Time.setError(null);
            return true;
        }
        return false;
    }
    private boolean ValidateResDescription() {
        String ResDesc = edt_Description.getEditText().getText().toString().trim();
        if (ResDesc.length() > 200) {
            edt_Description.setError("Description Too Long");

        } else {
            edt_Description.setError(null);
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_store:

        if(!ValidateLocation()|!ValidateStoreType()|!ValidateStoreName()|!ValidateOpeningTime()|!ValidateClosingTime()|!ValidateResDescription()){
            return;
        }else
        {
            ////////////////////////////////////
            String key = "unihRmGR3h6UtXGi3HJ8";/////////
            ////////////////////////////////////
            String store_type = sp_Store_Type.getSelectedItem().toString();
            String store_name = edt_Store_Name.getEditText().getText().toString().trim();
            String store_description = edt_Description.getEditText().getText().toString().trim();
            Home_Store_info addStoreInfo=new Home_Store_info(key,latitude,longitude,store_type,store_name,opening_hour,opening_minute,closing_hour,closing_minute,store_description);
mDBRef.collection("store").document().set(addStoreInfo)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Congrats!! Store added successfully",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("TAG","failed to add store "+e.getMessage());
    }
});
        }
                break;
            case R.id.btn_home_store_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_homestore.this, 5);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;
        }
    }

    @Override
    public void OnLocationGet(double lat, double lng) {
        isGetLocation = true;
        latitude = lat;
        longitude = lng;
        btn_Home_store_Location.setText("Location Saved");
        btn_Home_store_Location.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
