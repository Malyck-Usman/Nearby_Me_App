package com.example.nearbyme;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nearbyme.Model.Dish;
import com.example.nearbyme.Model.Dish_Adapter;
import com.example.nearbyme.Model.MediaUpload;
import com.example.nearbyme.Model.Restaurant_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class fragment_add_restaurants extends Fragment implements MapDialog.GetLocationDialogInterface, View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 10;
    private Uri filePath;
    private ProgressBar mProgressbar;
    private Uri mImageUri;
    ///////////
    private TextInputLayout edt_Restaurant_Name, edt_Res_Description, edt_Dish_Name, edt_Dish_Price;
    private EditText edt_Res_Opening_Time, edt_Res_Closing_Time;
    private CheckBox cb_Home_Delivery, cb_Table_Reservation;
    private Button btn_Add_Dish, btn_Save_Restaurant;
    private Spinner sp_Restaurant_Type;
    private TextView errorText;
    private boolean HomeDelivery = false;
    private boolean TableReservation = false;
    private String[] Restaurants = {"-Select Restaurant Type-", "Fine Dining", "Casual Dining", "Contemporary Casual", "Family Style", "Fast Casual", " Fast Food", "Cafe", "Buffet"};
    private int opening_hour, opening_minute, closing_hour, closing_minute;
    private RecyclerView mDishRecyclerView;
    private Dish_Adapter mDishAdapter;
    private List<Dish> mDishList;
    private FirebaseFirestore mDatabaseRef;
    private DocumentReference mDBDocRef;
    private StorageReference mStorageRef;
    private Map<String, Object> dishes;
    /////

    Button btn_Choose_img, btn_upload_img, btn_Res_Location;
    ImageView iv_Res_Image;
    private boolean isGetLocation = false;
    private double latitude = 0;
    private double longitude = 0;

    public fragment_add_restaurants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Add Restaurant");
        View view = inflater.inflate(R.layout.fragment_add_restaurants, container, false);
        mDatabaseRef = FirebaseFirestore.getInstance();

        SharedPreferences.Editor sp = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE).edit();

        ////////////////////////////////////////////////////////////
        sp.putString(getString(R.string.DOCUMENT_ID), "unihRmGR3h6UtXGi3HJ8");///
        sp.apply();/////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        InitViews(view);


        edt_Res_Opening_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    opening_hour = sHour;
                                    opening_minute = sMinute;
                                    edt_Res_Opening_Time.setText(sHour + ":" + sMinute);
                                }
                            }, 0, 0, true);
                    picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    picker.show();
                }
            }
        });
        edt_Res_Closing_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    closing_hour = sHour;
                                    closing_minute = sMinute;
                                    edt_Res_Closing_Time.setText(sHour + ":" + sMinute);
                                }
                            }, 23, 59, true);
                    picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    picker.show();
                }
            }
        });


        mDishList = new ArrayList<>();
        mDishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDishAdapter = new Dish_Adapter(getContext(), mDishList);
        mDishRecyclerView.setAdapter(mDishAdapter);
        mStorageRef = FirebaseStorage.getInstance().getReference("ImagesUploads");

        btn_Add_Dish.setOnClickListener(this);
        btn_Save_Restaurant.setOnClickListener(this);
        btn_Choose_img.setOnClickListener(this);
        btn_upload_img.setOnClickListener(this);


        return view;
    }

    private void InitViews(View view) {
        edt_Restaurant_Name = view.findViewById(R.id.edt_restaurant_name);
        edt_Res_Opening_Time = view.findViewById(R.id.edt_res_opening_time);


        edt_Res_Closing_Time = view.findViewById(R.id.edt_res_closing_time);
        edt_Res_Description = view.findViewById(R.id.edt_res_description);
        edt_Dish_Name = view.findViewById(R.id.edt_dish_name);
        edt_Dish_Price = view.findViewById(R.id.edt_dish_price);


        sp_Restaurant_Type = view.findViewById(R.id.sp_restaurant_type);


        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Restaurants);
        sp_Restaurant_Type.setAdapter(restaurantAdapter);
        mDishRecyclerView = view.findViewById(R.id.rv_dishes);


        cb_Home_Delivery = view.findViewById(R.id.cb_home_delivery);
        cb_Table_Reservation = view.findViewById(R.id.cb_table_reservation);


        btn_Add_Dish = view.findViewById(R.id.btn_add_dish);
        btn_Save_Restaurant = view.findViewById(R.id.btn_save_restaurent);
        btn_Res_Location = view.findViewById(R.id.btn_res_location);
        btn_Res_Location.setOnClickListener(this);

        dishes = new HashMap<>();
        ///////////
        btn_Choose_img = view.findViewById(R.id.btn_choose_res_image);
        btn_upload_img = view.findViewById(R.id.btn_upload_res_image);
        iv_Res_Image = view.findViewById(R.id.iv_res_image);
        mProgressbar = view.findViewById(R.id.pb_res_img);
        //////////////////

        mDBDocRef = mDatabaseRef.collection("restaurants").document();

    }

    private boolean ValidateLocation() {
        if (!isGetLocation) {
            btn_Res_Location.setText("Location Required,Click To Get Location");
            btn_Res_Location.setTextColor(getResources().getColor(R.color.app_red));
        } else {

            return true;
        }
        return false;
    }

    private boolean ValidateResType() {
        errorText = (TextView) sp_Restaurant_Type.getSelectedView();
        if (sp_Restaurant_Type.getSelectedItemPosition() == 0) {
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.app_red));//just to highlight that this is an error
            errorText.setText("Please Select a Restaurant Type");
        } else {
            if (errorText.getText().length() > 0) {

                errorText.setText("");
            }
            return true;
        }
        return false;
    }

    private boolean ValidateResName() {
        String ResName = edt_Restaurant_Name.getEditText().getText().toString().trim();
        if (ResName.length() > 25) {
            edt_Restaurant_Name.setError("Name Too Long");

        } else if (ResName.isEmpty()) {
            edt_Restaurant_Name.setError("Name Required");
        } else {
            edt_Restaurant_Name.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateOpeningTime() {
        String openingTime = edt_Res_Opening_Time.getText().toString().trim();
        if (openingTime.isEmpty()) {
            edt_Res_Opening_Time.setError("Opening Time Required");
        } else if (openingTime.length() > 5) {
            edt_Res_Opening_Time.setError("Invalid time, Select again");
        } else {
            edt_Res_Opening_Time.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateClosingTime() {
        String closingTime = edt_Res_Closing_Time.getText().toString().trim();
        if (closingTime.isEmpty()) {
            edt_Res_Closing_Time.setError("Closing Time Required");
        } else if (closingTime.length() > 5) {
            edt_Res_Closing_Time.setError("Invalid time, Select again");
        } else {
            edt_Res_Closing_Time.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateResDescription() {
        String ResDesc = edt_Res_Description.getEditText().getText().toString().trim();
        if (ResDesc.length() > 200) {
            edt_Res_Description.setError("Description Too Long");

        } else {
            edt_Res_Description.setError(null);
            return true;
        }
        return false;
    }

    private boolean ValidateRecyclerView() {
        if (dishes.isEmpty()) {
            edt_Dish_Name.setError("you must add min one dish");
            edt_Dish_Price.setError("you must add min one dish");
        } else {
            edt_Dish_Name.setError(null);
            edt_Dish_Price.setError(null);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_dish: {
                String dish_name = edt_Dish_Name.getEditText().getText().toString().trim();
                String dish_price = edt_Dish_Price.getEditText().getText().toString().trim();
                Dish dish = new Dish(dish_name, dish_price);
                mDishList.add(dish);
                mDishAdapter.notifyDataSetChanged();
                dishes.put(dish_name, dish_price);

            }
            break;

            case R.id.btn_save_restaurent:

                if (!ValidateLocation() | !ValidateResType() | !ValidateResName() | !ValidateOpeningTime() | !ValidateClosingTime() | !ValidateResDescription() | !ValidateRecyclerView()) {
                    return;

                }
                if (cb_Home_Delivery.isChecked()) {
                    HomeDelivery = true;
                } else {
                    HomeDelivery = false;
                }
                if (cb_Table_Reservation.isChecked()) {
                    TableReservation = true;
                } else {
                    TableReservation = false;
                }

                ////////////////////////////////////
                SharedPreferences Login = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);

                String key = Login.getString(getString(R.string.DOCUMENT_ID), "");
                if (key.equals("")) {
                    Toast.makeText(getActivity(), "Key not found", Toast.LENGTH_LONG).show();
                    return;
                }
                ////////////////////////////////////

                String res_type = sp_Restaurant_Type.getSelectedItem().toString();
                String res_name = edt_Restaurant_Name.getEditText().getText().toString().trim();
                String res_description = edt_Res_Description.getEditText().getText().toString().trim();
                Restaurant_info res_info = new Restaurant_info(key
                        , latitude
                        , longitude
                        , res_type
                        , res_name
                        , opening_hour
                        , opening_minute
                        , closing_hour
                        , closing_minute
                        , res_description
                        , HomeDelivery
                        , TableReservation);
                //  mDatabaseRef.collection("restaurants").document()
                mDBDocRef.set(res_info)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "data inserted successfully");
                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();
                                mDBDocRef.collection("dishes").document().set(dishes);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error:" + e.getMessage().toString());

                    }
                });

                break;
            case R.id.btn_choose_res_image:
                Toast.makeText(getContext(), "btn choose clicked", Toast.LENGTH_LONG).show();
                chooseImage();
                break;
            case R.id.btn_upload_res_image:
                uploadImage();
                break;
            case R.id.btn_res_location:
                MapDialog mapDialog = new MapDialog();
                mapDialog.setTargetFragment(fragment_add_restaurants.this, 1);
                mapDialog.setCancelable(false);
                mapDialog.show(getActivity().getSupportFragmentManager(), "MapDialog");
                break;

        }

    }


    private void uploadImage() {
        if (mImageUri != null) {
            StorageReference filereference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtention(mImageUri));
            filereference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            MediaUpload imgUpload = new MediaUpload(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            mDBDocRef.collection("Images").document().set(imgUpload);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "failed to upload" + e.getMessage());
                }
            });
        }


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtention(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(getContext()).load(mImageUri).into(iv_Res_Image);
            //without use of picasso
            //iv_Res_Image.setImageURI(mImageUri);
        }
    }


    @Override
    public void OnLocationGet(double lat, double lng) {
        //  edt_Location.getEditText().setText("Location saved");

        isGetLocation = true;
        latitude = lat;
        longitude = lng;
        btn_Res_Location.setText("Location Saved");
        btn_Res_Location.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
