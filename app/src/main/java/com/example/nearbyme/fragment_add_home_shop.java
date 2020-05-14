package com.example.nearbyme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.nearbyme.Model.Home_info;
import com.example.nearbyme.Model.Shop_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_add_home_shop extends Fragment implements MapDialog.GetLocationDialogInterface, View.OnClickListener {
    TextInputLayout edt_Dimensions, edt_Area, edt_Covered_Area, edt_Rent, edt_Security, edt_Floor, edt_Rooms, edt_Bathrooms, edt_Kitchens, edt_Description;
    CheckBox cb_Finishing, cb_Store, cb_Parking, cb_Gas, cb_Water, cb_Garage, cb_Lawn, cb_Furnished;
    Button btn_Save_Home_Shop,btn_Home_Shop_Location;
    SwitchCompat sw_Home_Shop;
    private boolean isHome = true;
    private boolean furnished=false;
    private boolean gas=false;
    private boolean water=false;
    private boolean garage=false;
    private boolean lawn=false;
    private boolean finishing=false;
    private boolean store=false;
    private boolean parking=false;
    private FirebaseFirestore mDatabaseRef;
    private boolean isGetLocation=false;
    private double latitude=0;
    private double longitude=0;

    public fragment_add_home_shop() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Add Home");
        //   getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_signup()).commit();
        View view = inflater.inflate(R.layout.fragment_add_home_shop, container, false);
        initViews(view);
        mDatabaseRef=FirebaseFirestore.getInstance();
        ShowHome(view);
        sw_Home_Shop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (sw_Home_Shop.isChecked()) {
                ((MainActivity) getActivity()).setActionBarTitle("Add Shop");
                isHome = false;
                ShowShop(view);
            } else {
                ((MainActivity) getActivity()).setActionBarTitle("Add Home");
                isHome = true;
                ShowHome(view);
            }
        });
        btn_Save_Home_Shop.setOnClickListener(this);


        return view;
    }

    private void initViews(View view) {
        edt_Dimensions = view.findViewById(R.id.edt_dimensions);
        edt_Area = view.findViewById(R.id.edt_area);
        edt_Covered_Area = view.findViewById(R.id.edt_covered_area);
        edt_Rent = view.findViewById(R.id.edt_rent);
        edt_Security = view.findViewById(R.id.edt_security);
        edt_Floor = view.findViewById(R.id.edt_floor);
        edt_Rooms = view.findViewById(R.id.edt_no_of_rooms);
        edt_Bathrooms = view.findViewById(R.id.edt_no_of_baths);
        edt_Kitchens = view.findViewById(R.id.edt_no_of_kitchen);
        edt_Description = view.findViewById(R.id.edt_description_home_shop);

        cb_Finishing = view.findViewById(R.id.cb_finishing);
        cb_Store = view.findViewById(R.id.cb_store);
        cb_Parking = view.findViewById(R.id.cb_parking);
        cb_Gas = view.findViewById(R.id.cb_gas_connection);
        cb_Water = view.findViewById(R.id.cb_water_connection);
        cb_Garage = view.findViewById(R.id.cb_garage);
        cb_Lawn = view.findViewById(R.id.cb_lawn);
        cb_Furnished = view.findViewById(R.id.cb_furnished);

        btn_Save_Home_Shop = view.findViewById(R.id.btn_save_home_shop);
        btn_Home_Shop_Location=view.findViewById(R.id.btn_home_shop_location);

        btn_Home_Shop_Location.setOnClickListener(this);

        sw_Home_Shop = view.findViewById(R.id.sw_home_shop);
    }

    private void ShowShop(View view) {
        edt_Area.setVisibility(View.GONE);
        edt_Covered_Area.setVisibility(View.GONE);
        edt_Rooms.setVisibility(View.GONE);
        edt_Bathrooms.setVisibility(View.GONE);
        edt_Kitchens.setVisibility(View.GONE);
        cb_Lawn.setVisibility(View.GONE);
        cb_Garage.setVisibility(View.GONE);
        cb_Water.setVisibility(View.GONE);
        cb_Gas.setVisibility(View.GONE);
        cb_Furnished.setVisibility(View.GONE);
        edt_Dimensions.setVisibility(View.VISIBLE);
        cb_Store.setVisibility(View.VISIBLE);
        cb_Parking.setVisibility(View.VISIBLE);
        cb_Finishing.setVisibility(View.VISIBLE);
    }

    private void ShowHome(View view) {
        edt_Dimensions.setVisibility(View.GONE);
        cb_Store.setVisibility(View.GONE);
        cb_Parking.setVisibility(View.GONE);
        cb_Finishing.setVisibility(View.GONE);
        edt_Area.setVisibility(View.VISIBLE);
        edt_Covered_Area.setVisibility(View.VISIBLE);
        edt_Rooms.setVisibility(View.VISIBLE);
        edt_Bathrooms.setVisibility(View.VISIBLE);
        edt_Kitchens.setVisibility(View.VISIBLE);
        cb_Lawn.setVisibility(View.VISIBLE);
        cb_Garage.setVisibility(View.VISIBLE);
        cb_Water.setVisibility(View.VISIBLE);
        cb_Gas.setVisibility(View.VISIBLE);
        cb_Furnished.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        case R.id.btn_save_home_shop: {
            ////////////////////////////////////
            String key = "unihRmGR3h6UtXGi3HJ8";/////////
            ////////////////////////////////////
            if (isHome) {
                if     (!ValidateLocation()|!ValidateTotalArea() |
                        !ValidateCoveredArea() |
                        !ValidateRentAmount() |
                        !ValidateSecurityAmount() |
                        !ValidateFloor() |
                        !ValidateNoOfRooms() |
                        !ValidateNoOfBathrooms() |
                        !ValidateNoOfKitchens() |
                        !ValidateDescription()) {
                    return;
                }
                else{
                    checkBoxesForHome();


                    float total_area = Float.parseFloat(edt_Area.getEditText().getText().toString().trim());
                    float covered_area = Float.parseFloat(edt_Covered_Area.getEditText().getText().toString().trim());
                    int rent_amount = Integer.parseInt(edt_Rent.getEditText().getText().toString());
                    int security=checkSecurity();

                    String floor = edt_Floor.getEditText().getText().toString();
                    int rooms = Integer.parseInt(edt_Rooms.getEditText().getText().toString());
                    int bathrooms = Integer.parseInt(edt_Bathrooms.getEditText().getText().toString());
                    int kitchens=checkKitchens();
                    String description=edt_Description.getEditText().getText().toString();
                    Home_info insInfo=new Home_info(key,latitude,longitude,total_area,covered_area,rent_amount,security,floor,rooms,bathrooms,kitchens,description,furnished,gas,water,garage,lawn);
mDatabaseRef.collection("homes").document().set(insInfo)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Congrats!!! ,your home added successfully",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("TAG","failed to add"+e.getMessage());
    }
});

                }
            }
            else {
                if (!ValidateLocation()|!ValidateDimensions() | !ValidateRentAmount() | !ValidateSecurityAmount() | !ValidateFloor() | !ValidateDescription()) {
                    return;
                }else
                {
                    checkBoxesForShop();
                    String dimension=edt_Dimensions.getEditText().getText().toString().trim();
                   int rent=Integer.parseInt(edt_Rent.getEditText().getText().toString());
                   int security=checkSecurity();
                    String floor=edt_Floor.getEditText().getText().toString().trim();
                    String description=edt_Description.getEditText().getText().toString().trim();

                    Shop_info ins_shop=new Shop_info(key,latitude,longitude,dimension,rent,security,floor,description,finishing,store,parking);
                       mDatabaseRef.collection("shop").document().set(ins_shop)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       Toast.makeText(getContext(),"Congrats !! Shop added successfully",Toast.LENGTH_LONG).show();

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.d("TAG","failed to add shop"+e.getMessage());
                           }
                       });
                }
            }
        }
        break;
            case R.id.btn_home_shop_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_home_shop.this, 4);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;
        }
    }



    private int checkKitchens() {
        String kitchen=edt_Kitchens.getEditText().getText().toString();
        if(kitchen.equals("")){
            return 0;
        }
       return Integer.parseInt(kitchen);
    }

    private int checkSecurity() {
        String security=edt_Security.getEditText().getText().toString().trim();
        if(security.equals("")){
            return 0;
        }
        return Integer.parseInt(security);
    }


    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Home_Shop_Location.setText("Location Required,Click To Get Location");
            btn_Home_Shop_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }


    private boolean ValidateTotalArea() {
        String TotalArea = edt_Area.getEditText().getText().toString().trim();
        if (TotalArea.length() > 5) {
            edt_Area.setError("Area Exceeded required limit");

        } else if (TotalArea.isEmpty()) {
            edt_Area.setError("Area Required");
        } else {
            edt_Area.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateCoveredArea() {
        String CoveredArea = edt_Covered_Area.getEditText().getText().toString().trim();
        if (CoveredArea.length() > 5) {
            edt_Area.setError("Area Exceeded required limit");

        } else if (CoveredArea.isEmpty()) {
            edt_Covered_Area.setError("Area Required");
        } else {
            edt_Covered_Area.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateRentAmount() {
        String RentAmount = edt_Rent.getEditText().getText().toString().trim();
        if (RentAmount.length() > 7) {
            edt_Rent.setError("Rent Amount Exceeded Required Limit");

        } else if (RentAmount.isEmpty()) {
            edt_Rent.setError("Rent Required");
        } else {
            edt_Rent.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateSecurityAmount() {
        String SecurityAmount = edt_Security.getEditText().getText().toString().trim();
        if (SecurityAmount.length() > 7) {
            edt_Security.setError("Security Amount Exceeded Required Limit");
        } else {
            edt_Security.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateFloor() {
        String Floor = edt_Floor.getEditText().getText().toString().trim();
        if (Floor.length() > 10) {
            edt_Floor.setError("Floor text Exceeded Required Limit");
        } else {
            edt_Floor.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateNoOfRooms() {
        String Rooms = edt_Rooms.getEditText().getText().toString().trim();
        if (Rooms.length() > 3) {
            edt_Rooms.setError("No Of Rooms Exceeded Required Limit");

        } else if (Rooms.isEmpty()) {
            edt_Rooms.setError("No Of Rooms Required");
        } else {
            edt_Rooms.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateNoOfBathrooms() {
        String Baths = edt_Bathrooms.getEditText().getText().toString().trim();
        if (Baths.length() > 3) {
            edt_Rooms.setError("No Of Bathrooms Exceeded Required Limit");

        } else if (Baths.isEmpty()) {
            edt_Bathrooms.setError("No Of Bathrooms Required");
        } else {
            edt_Bathrooms.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateNoOfKitchens() {
        String Kitchens = edt_Kitchens.getEditText().getText().toString().trim();
        if (Kitchens.length() > 3) {
            edt_Kitchens.setError("No Of Kitchens Exceeded Required Limit");

        } else {
            edt_Kitchens.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateDescription() {
        String Description = edt_Description.getEditText().getText().toString().trim();
        if (Description.length() > 200) {
            edt_Description.setError("Description Length Exceeded Limit");
        } else {
            edt_Description.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateDimensions() {
        String Dimensions = edt_Dimensions.getEditText().getText().toString().trim();
        if (Dimensions.length() > 7) {
            edt_Dimensions.setError("Dimension Text Exceeded Required Limit");

        } else if (Dimensions.isEmpty()) {
            edt_Dimensions.setError("Dimension Required");
        } else {
            edt_Dimensions.setError(null);
            return true;
        }
        return false;
    }

    private void checkBoxesForHome() {
        furnished= cb_Furnished.isChecked();
        gas=cb_Gas.isChecked();
        water=cb_Water.isChecked();
        garage=cb_Garage.isChecked();
        lawn=cb_Lawn.isChecked();

    }
    private void checkBoxesForShop() {
        finishing= cb_Finishing.isChecked();
        store=cb_Store.isChecked();
        parking=cb_Parking.isChecked();
    }


    @Override
    public void OnLocationGet(double lat, double lng) {
        isGetLocation = true;
        latitude = lat;
        longitude = lng;
        btn_Home_Shop_Location.setText("Location Saved");
        btn_Home_Shop_Location.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
