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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.Item_info;
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
public class fragment_add_Item extends Fragment implements MapDialog.GetLocationDialogInterface, View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, fragment_login.GetUserIdInterface {
    Spinner sp_Category, sp_Subcategory;
    private RadioGroup rg_Condition;
    private RadioButton rb_New, rb_Used;
    TextInputLayout edt_Name, edt_Brand, edt_Price, edt_Description;
    private Button btn_Save, btn_Item_Location;
    TextView errorTextCategory, errorTextSubCategory;
    private boolean IsChecked = false;
    private FirebaseFirestore mDBRef;
    private DocumentReference mDocRef;
    private String item_id = null;
    private String user_id = null;

    String Condition = null;
    String[] Kids_Accessories = {"-Select Sub-Category-", "Kids Furniture", "Toys", "Prams & Walkers", "Swings & Slides", "Kids Bikes", "Kids Accessories"};
    String[] Books_Sports = {"-Select Sub-Category-", "Books & Magazines", "Sports Equipment", "Gym & Fitness", "Musical Instruments", "Other Hobbies"};
    String[] Fashion_Beauty = {"-Select Sub-Category-", "Clothes", "Footwear", "Jewellery", "MakeUp", "Skin & Hair", "Watches", "Wedding Accessories", "Lawn & Pret", "Couture", "Other Fashion", "Accessories"};
    String[] Furniture_HomeDecor = {"-Select Sub-Category-", "Sofa & Chairs", "Beds & Wardrobes", "Home Decoration", "Tables & Dining", "Garden & Outdoor", "Painting & Mirrors", "Rugs & Carpets", "Curtains & Blinds", "Office Furniture", "Other Household Items"};
    String[] Animals = {"-Select Sub-Category-", "Animals", "Fish & Aquariums", "Birds", "Hens", "Cats", "Dogs", "Livestock", "Pet Food & Accessories", "Other Animals"};
    String[] Bikes = {"-Select Sub-Category-", "Motorcycles", "Bicycles", "ATV & Quads", "Scooters", "Spare Parts"};
    String[] Electronics_HomeAppliances = {"-Select Sub-Category-", "AC & Coolers", "Fridges & Freezers", "Washing Machines & Dryers", "kitchen Appliances", "Generators,UPS & Power Solutions", "Cameras & Accessories", "TV, Video & Audio", "Other Home Appliances", "Electronics"};
    String[] Vehicles = {"-Select Sub-Category-", "Cars", "Cars Accessories", "Spare Parts", "Rickshaw & Chingchi", "Other Vehicles"};
    String[] Software_Games = {"-Select Sub-Category-", "Games", "Windows", "Utility Software", "Software Development", "Designing & Printing"};
    String[] Mobile_Computer = {"-Select Sub-Category-", "Mobile Phones", "Laptop", "Desktop Computer", "Tablets", "Computer Accessories", "Mobile Accessories"};
    String[] Category = {"-Select a Category-", "Mobiles & Computers", "Software Games & Entertainment", "Vehicles", "Electronics & Home Appliances", "Bikes", "Animals & Pets", "Furniture & Home Decoration", "Fashion & Beauty", "Books & Sports", "Kids Accessories"};
    String[] SubCategory = {"-Select Category first-"};
    private boolean isGetLocation = false;
    private double latitude = 0;
    private double longitude = 0;

    public fragment_add_Item() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add__item, container, false);
        checkLogin();
        InitViews(view);
        mDBRef = FirebaseFirestore.getInstance();
        CheckBundle();


        return view;
    }

    private void CheckBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            item_id = bundle.getString("item_id", null);
            if (item_id != null) {

                mDBRef.collection("items").document(item_id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Item_info item = documentSnapshot.toObject(Item_info.class);
                                edt_Name.getEditText().setText(item.getItem_name());
                                edt_Brand.getEditText().setText(item.getBrand_name());
                                edt_Price.getEditText().setText(String.valueOf(item.getPrice()));
                                edt_Description.getEditText().setText(item.getDescription());
                                String condition = item.getCondition();
                                if (condition.equals("Used")) {
                                    rb_Used.setChecked(true);
                                } else {
                                    rb_New.setChecked(true);
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

    private void checkLogin() {
        SharedPreferences checkUserId = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);
        user_id = checkUserId.getString(getString(R.string.DOCUMENT_ID), "");
        if (user_id.equals("")) {
            fragment_login login = new fragment_login();
            login.setTargetFragment(fragment_add_Item.this, 1);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, login)
                    .addToBackStack(null).commit();

        }
    }


    private void InitViews(View view) {
        edt_Name = view.findViewById(R.id.edt_item_name);
        edt_Brand = view.findViewById(R.id.edt_brand_name);
        edt_Price = view.findViewById(R.id.edt_item_price);
        edt_Description = view.findViewById(R.id.edt_item_description);

        sp_Category = view.findViewById(R.id.sp_item_category);
        sp_Subcategory = view.findViewById(R.id.sp_sub_category);

        rg_Condition = view.findViewById(R.id.rg_condition);
        rb_New = view.findViewById(R.id.rb_new);
        rb_Used = view.findViewById(R.id.rb_used);

        btn_Save = view.findViewById(R.id.btn_save_item);
        btn_Item_Location = view.findViewById(R.id.btn_item_location);

        rg_Condition.setOnCheckedChangeListener(this);
        btn_Save.setOnClickListener(this);
        btn_Item_Location.setOnClickListener(this);

        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, SubCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Category);
        sp_Category.setAdapter(adapter);
        sp_Category.setOnItemSelectedListener(this);
        sp_Subcategory.setAdapter(subCategoryAdapter);


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        IsChecked = true;
        if (rb_New.isChecked()) {
            Condition = "New";
        }
        if (rb_Used.isChecked()) {
            Condition = "Used";
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, SubCategory);
                sp_Subcategory.setAdapter(subCategoryAdapter);

                break;
            case 1:
                ArrayAdapter<String> Mobile_comp = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Mobile_Computer);
                sp_Subcategory.setAdapter(Mobile_comp);
                break;

            case 2:
                ArrayAdapter<String> software_games = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Software_Games);
                sp_Subcategory.setAdapter(software_games);
                break;


            case 3:
                ArrayAdapter<String> vehicles = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Vehicles);
                sp_Subcategory.setAdapter(vehicles);
                break;

            case 4:
                ArrayAdapter<String> electronics = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Electronics_HomeAppliances);
                sp_Subcategory.setAdapter(electronics);
                break;


            case 5:
                ArrayAdapter<String> bikes = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Bikes);
                sp_Subcategory.setAdapter(bikes);
                break;

            case 6:
                ArrayAdapter<String> animals_pets = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Animals);
                sp_Subcategory.setAdapter(animals_pets);
                break;

            case 7:
                ArrayAdapter<String> furniture_homedecor = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Furniture_HomeDecor);
                sp_Subcategory.setAdapter(furniture_homedecor);
                break;

            case 8:
                ArrayAdapter<String> fashion = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Fashion_Beauty);
                sp_Subcategory.setAdapter(fashion);
                break;

            case 9:
                ArrayAdapter<String> books_sports = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Books_Sports);
                sp_Subcategory.setAdapter(books_sports);
                break;

            case 10:
                ArrayAdapter<String> kids = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, Kids_Accessories);
                sp_Subcategory.setAdapter(kids);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_item:

                if (!ValidateLocation() | !ValidateCategory() | !ValidateSubCategory() | !ValidateItemName() | !ValidateBrand() | !ValidateCondition() | !ValidatePrice() | !ValidateDescription()) {
                    return;
                } else {
                    ////////////////////////////////////
                    /////////
                    ////////////////////////////////////
                    String category = sp_Category.getSelectedItem().toString();
                    String sub_category = sp_Subcategory.getSelectedItem().toString();
                    String item_name = edt_Name.getEditText().getText().toString().trim();
                    String brand_name = edt_Brand.getEditText().getText().toString().trim();
                    int price = Integer.parseInt(edt_Price.getEditText().getText().toString().trim());
                    String item_description = edt_Description.getEditText().getText().toString().trim();
                    Item_info addItem = new Item_info(user_id, latitude, longitude, category, sub_category, item_name, brand_name, Condition, price, item_description,true);

                    if (item_id != null) {
                        mDocRef = mDBRef.collection("items").document(item_id);
                    } else {
                        mDocRef = mDBRef.collection("items").document();

                    }


                    mDocRef.set(addItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Item added successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Failed to add item" + e.getMessage());
                        }
                    });
                }
                break;

            case R.id.btn_item_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_Item.this, 5);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;


        }//end of switch
    }

    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Item_Location.setText("Location Required,Click To Get Location");
            btn_Item_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }

    private boolean ValidateCategory() {
        errorTextCategory = (TextView) sp_Category.getSelectedView();
        if (sp_Category.getSelectedItemPosition() == 0) {
            errorTextCategory.setError("");
            errorTextCategory.setTextColor(getResources().getColor(R.color.app_red));//just to highlight that this is an error
            errorTextCategory.setText("Please Select a  Category");
        } else {
            if (errorTextCategory.getText().length() > 0) {

                errorTextCategory.setText("");
            }
            return true;
        }
        return false;
    }

    private boolean ValidateSubCategory() {
        errorTextSubCategory = (TextView) sp_Subcategory.getSelectedView();
        if (sp_Subcategory.getSelectedItemPosition() == 0) {
            errorTextSubCategory.setError("");
            errorTextSubCategory.setTextColor(getResources().getColor(R.color.app_red));//just to highlight that this is an error
            errorTextSubCategory.setText("Please Select Sub-Category");
        } else {
            if (errorTextSubCategory.getText().length() > 0) {

                errorTextSubCategory.setText("");
            }
            return true;
        }
        return false;
    }

    private boolean ValidateItemName() {
        String Name = edt_Name.getEditText().getText().toString().trim();
        if (Name.length() > 25) {
            edt_Name.setError("Name Text Exceeded the Limit ");

        } else if (Name.length() == 0) {
            edt_Name.setError("Name Required");
        } else {
            edt_Name.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateBrand() {
        String Brand = edt_Brand.getEditText().getText().toString().trim();
        if (Brand.length() > 25) {
            edt_Brand.setError("Brand Name Text Exceeded the Limit");

        } else {
            edt_Brand.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateCondition() {
        if (!IsChecked) {

            rb_Used.setError("You Must Select Charges type");
        } else {

            rb_Used.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidatePrice() {
        String price = edt_Price.getEditText().getText().toString().trim();
        if (price.length() > 8) {
            edt_Price.setError("Price Exceeded the Limit");

        } else if (price.length() == 0) {
            edt_Price.setError("Price Required");

        } else {
            edt_Price.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateDescription() {
        String ServiceDesc = edt_Description.getEditText().getText().toString().trim();
        if (ServiceDesc.length() > 200) {
            edt_Description.setError("Description Too Long");

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
        btn_Item_Location.setText("Location Saved");
        btn_Item_Location.setTextColor(getResources().getColor(R.color.ColorPrimary));
    }

    @Override
    public void OnUidGet(String u_id) {
        user_id = u_id;
    }
}
