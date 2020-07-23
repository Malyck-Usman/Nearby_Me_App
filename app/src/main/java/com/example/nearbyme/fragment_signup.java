package com.example.nearbyme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class fragment_signup extends Fragment {

    TextInputLayout edt_Name, edt_Phone, edt_Email, edt_Password, edt_Conform_Password;
    Button btn_Signup;
    private FirebaseFirestore mDBRef;
        String isAllowed;


    public fragment_signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews(view);
        mDBRef = FirebaseFirestore.getInstance();

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","btn clicked");
                if (!ValidateName() | !ValidateEmail() | !ValidatePhoneNo() | !ValidatePassword() | !ValidateConfirmPassword()) {
                    return;
                }
//                else if (AllowedEmail().equals("false")) {
//                    Log.d("TAG","Allowed Email="+AllowedEmail());
//                    return;
//                }
                Log.d("TAG","After Allowed email statement");
                String name = edt_Name.getEditText().getText().toString().trim();
                String phone_no = edt_Phone.getEditText().getText().toString().trim();
                String e_mail = edt_Email.getEditText().getText().toString().trim();
                String password = edt_Password.getEditText().getText().toString().trim();

//                Log.d("TAG","edt user name"+name);
//                Log.d("TAG","edt user phone no"+phone_no);
//                Log.d("TAG","edt user email"+edt_Email);
//                Log.d("TAG","edt user password"+edt_Password);

                User user = new User(name, phone_no, e_mail, password, true,"user");
//                User user = new User(name, phone_no, e_mail, password);
//                Log.d("TAG",user.getUser_name());
//                Log.d("TAG",user.getE_mail());
//                Log.d("TAG",user.getPhone_no());
//                Log.d("TAG",user.getPassword());



                mDBRef = FirebaseFirestore.getInstance();
                String id = mDBRef.collection("users").document().getId();
                Log.d("TAG","Document id of new user is"+id);
                mDBRef.collection("users").document(id).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AlertDialog.Builder success = new AlertDialog.Builder(getContext());
                                success.setCancelable(false);
                                success.setTitle("SIGNUP");
                                success.setMessage("SIGNUP Successful !!!");
                                success.setPositiveButton("OK", null);
                                success.show();

                                SharedPreferences.Editor sp_login = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE).edit();
                                sp_login.putString(getString(R.string.DOCUMENT_ID), id);
                                sp_login.apply();



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","failed to add data"+e.getMessage());

                    }
                });


            }
        });

        return view;
    }

    private String AllowedEmail() {
        String e_mail = edt_Email.getEditText().getText().toString().trim();
        isAllowed = null;
        mDBRef.collection("users").whereEqualTo("e_mail", e_mail).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!(queryDocumentSnapshots.isEmpty())) {
                            Log.d("TAG","datasnapshot is notempty");
                           // edt_Email.getEditText().setError(null);
                            edt_Email.setError("Email already used");

                            isAllowed = "false";
                            Log.d("TAG","is allowed in if statement"+isAllowed);


                        }
                        else {
                             edt_Email.getEditText().setError(null);
                             isAllowed="true";
//                            edt_Email.setError("Email already used");
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "Failed to Connect to server, try again", Toast.LENGTH_LONG).show();
            }
        });
//        while(isAllowed == null){
//            Log.d("TAG","isAllowed is null"+isAllowed);
//        }
        Log.d("TAG","isAllowed is "+isAllowed);

        return isAllowed;
    }


    private void initViews(View view) {
        edt_Name = view.findViewById(R.id.edt_name);
        edt_Phone = view.findViewById(R.id.edt_phone);
        edt_Email = view.findViewById(R.id.edt_email);
        edt_Password = view.findViewById(R.id.edt_password);
        edt_Conform_Password = view.findViewById(R.id.edt_confirm_password);
        btn_Signup = view.findViewById(R.id.btn_signup);


    }

    private boolean ValidateEmail() {
        String email = edt_Email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            edt_Email.setError("E-mail is required ");
            //edt_Email.isFocused();
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            edt_Email.setError("Invalid e-mail address");
        } else {
            edt_Email.setError(null);
            return true;
        }

        return false;
    }

    private boolean ValidatePhoneNo() {
        String phone_no = edt_Phone.getEditText().getText().toString().trim();
        if (phone_no.isEmpty()) {
            edt_Phone.setError("Mobile number is required ");
            //edt_Phone.isFocused();
        } else if (phone_no.length() != 11) {
            edt_Phone.setError("Mobile number not valid ");
        } else {
            edt_Phone.setError(null);
            return true;
        }

        return false;
    }

    private boolean ValidatePassword() {
        String password = edt_Phone.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            edt_Password.setError("password is required ");
            //edt_password.isFocused();
        } else {
            edt_Password.setError(null);
            return true;
        }

        return false;
    }

    private boolean ValidateName() {
        String name = edt_Name.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            edt_Name.setError("Name is required ");

        } else {
            edt_Name.setError(null);
            return true;
        }

        return false;
    }

    private boolean ValidateConfirmPassword() {
        String password = edt_Password.getEditText().getText().toString().trim();
        String confirn_password = edt_Conform_Password.getEditText().getText().toString().trim();
        if (!(password.equals(confirn_password))) {
            edt_Conform_Password.setError("password dosen't match ");
            //edt_password.isFocused();
        } else {
            edt_Conform_Password.setError(null);
            return true;
        }

        return false;
    }


}
