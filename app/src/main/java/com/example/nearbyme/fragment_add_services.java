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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.Service_info;
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
public class fragment_add_services extends Fragment implements MapDialog.GetLocationDialogInterface, RadioGroup.OnCheckedChangeListener, View.OnClickListener, fragment_login.GetUserIdInterface {
    Spinner sp_Services;
    RadioGroup rg_Charges_Type;
    RadioButton rb_Per_Day, rb_Per_Service, rb_Per_Hour, rb_Per_Client;
    TextInputLayout edt_Speciality, edt_Experience, edt_Charges, edt_Description;
    TextView errorText;
    Button btn_Save, btn_Services_Location;
    String[] Services = {"-Select the service-", "Doctor", "Teacher", "Mechanic", "Painter", "Carpenter", "Maison", "Plumber"};
    private boolean IsChecked = false;
    String Charge_Type = null;
    private FirebaseFirestore mDBRef;
    private DocumentReference mDocRef;

    private boolean isGetLocation = false;
    private double latitude = 0;
    private double longitude = 0;
    private String service_id = null;
    private String user_id = null;

    public fragment_add_services() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Add Service");
        checkLogin();
        View view = inflater.inflate(R.layout.fragment_add_services, container, false);
        initViews(view);
        mDBRef = FirebaseFirestore.getInstance();
        checkBundle();
        return view;

    }
    private void checkLogin() {
        SharedPreferences checkUserId = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        user_id = checkUserId.getString(getString(R.string.DOCUMENT_ID), "");
        if (user_id.equals("")) {
            fragment_login login = new fragment_login();
            login.setTargetFragment(fragment_add_services.this, 7);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, login)
                    .addToBackStack(null).commit();

        }
    }

    private void checkBundle() {
        Bundle bundle = this.getArguments();
        if(bundle!=null){

        service_id = bundle.getString("service_id", null);
        if (service_id != null) {
            mDBRef.collection("services").document(service_id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Service_info service = documentSnapshot.toObject(Service_info.class);
                            edt_Speciality.getEditText().setText(service.getSpeciality());
                            edt_Experience.getEditText().setText(String.valueOf(service.getExperience()));
                            edt_Charges.getEditText().setText(String.valueOf(service.getCharge_amount()));
                            edt_Description.getEditText().setText(service.getDescription());
                            String chargeType = service.getCharge_type();
                            if (chargeType.equals("per hour")) {
                                rb_Per_Hour.setChecked(true);
                            } else if (chargeType.equals("per Client")) {
                                rb_Per_Client.setChecked(true);
                            } else if (chargeType.equals("per day")) {
                                rb_Per_Day.setChecked(true);
                            } else {
                                rb_Per_Service.setChecked(true);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        }
    }


    private void initViews(View view) {
        edt_Speciality = view.findViewById(R.id.edt_speciality);
        edt_Experience = view.findViewById(R.id.edt_experience);
        edt_Charges = view.findViewById(R.id.edt_charges);
        edt_Description = view.findViewById(R.id.edt_service_description);

        sp_Services = view.findViewById(R.id.sp_services);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, Services);
        sp_Services.setAdapter(adapter);

        rg_Charges_Type = view.findViewById(R.id.rg_charges_type);
        rb_Per_Client = view.findViewById(R.id.rb_per_client);
        rb_Per_Day = view.findViewById(R.id.rb_per_day);
        rb_Per_Hour = view.findViewById(R.id.rb_per_hour);
        rb_Per_Service = view.findViewById(R.id.rb_per_service);


        btn_Save = view.findViewById(R.id.btn_save_service);
        btn_Services_Location = view.findViewById(R.id.btn_sevices_location);

        rg_Charges_Type.setOnCheckedChangeListener(this);
        btn_Save.setOnClickListener(this);
        btn_Services_Location.setOnClickListener(this);


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        IsChecked = true;
        if (rb_Per_Service.isChecked()) {
            Charge_Type = rb_Per_Service.getText().toString();
        }
        if (rb_Per_Day.isChecked()) {
            Charge_Type = "Per Day";
        }
        if (rb_Per_Hour.isChecked()) {
            Charge_Type = "Per Hour";
        }
        if (rb_Per_Client.isChecked()) {
            Charge_Type = "Per Client";
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_service:

                if (!ValidateLocation() | !ValidateServiceType() | !ValidateSpeciality() | !ValidateExperience() | !ValidateCharges() | !ValidateChargesType() | !ValidateResDescription()) {
                    return;
                } else {

                    String service_type = sp_Services.getSelectedItem().toString();
                    String speciality = edt_Speciality.getEditText().getText().toString().trim();
                    int experience = Integer.parseInt(edt_Experience.getEditText().getText().toString().trim());
                    int charges = Integer.parseInt(edt_Charges.getEditText().getText().toString().trim());
                    String store_description = edt_Description.getEditText().getText().toString().trim();
                    Service_info addServiceInfo = new Service_info(user_id, latitude, longitude, service_type, speciality, experience, Charge_Type, charges, store_description,true);
                    if (service_id != null) {
                        mDocRef = mDBRef.collection("services").document(service_id);
                    } else {
                        mDocRef = mDBRef.collection("services").document();

                    }


                    mDocRef.set(addServiceInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "service added successfully", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error adding data" + e.getMessage());
                        }
                    });
                }
                break;
            case R.id.btn_sevices_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_services.this, 6);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;
        }


    }

    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Services_Location.setText("Location Required,Click To Get Location");
            btn_Services_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }

    private boolean ValidateServiceType() {
        errorText = (TextView) sp_Services.getSelectedView();
        if (sp_Services.getSelectedItemPosition() == 0) {
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.app_red));//just to highlight that this is an error
            errorText.setText("Please Select a Service Type");
        } else {
            if (errorText.getText().length() > 0) {

                errorText.setText("");
            }
            return true;
        }
        return false;
    }

    private boolean ValidateSpeciality() {
        String Speciality = edt_Speciality.getEditText().getText().toString().trim();
        if (Speciality.length() > 25) {
            edt_Speciality.setError("Text Too Long ");

        } else {
            edt_Speciality.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateExperience() {
        String Experience = edt_Experience.getEditText().getText().toString().trim();
        if (Experience.length() > 2) {
            edt_Experience.setError("Invalid Experience");

        } else if (Experience.isEmpty()) {
            edt_Experience.setError("Experience Required");
        } else {
            edt_Experience.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateChargesType() {
        if (!IsChecked) {

            rb_Per_Client.setError("You Must Select Charges type");
        } else {

            rb_Per_Client.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateCharges() {
        String Charges = edt_Charges.getEditText().getText().toString().trim();
        if (Charges.length() > 5) {
            edt_Charges.setError("Length Exceeded The Limit");

        } else if (Charges.isEmpty()) {
            edt_Charges.setError("Charges Required");
        } else {
            edt_Charges.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateResDescription() {
        String ServiceDesc = edt_Description.getEditText().getText().toString().trim();
        if (ServiceDesc.length() < 80) {
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
        btn_Services_Location.setText("Location Saved");
        btn_Services_Location.setTextColor(getResources().getColor(R.color.ColorPrimary));
    }

    @Override
    public void OnUidGet(String u_id) {
        user_id=u_id;
    }
}
