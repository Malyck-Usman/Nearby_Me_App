package com.example.nearbyme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;


public class fragment_login extends Fragment {
    private TextInputLayout txtEmail,txtPassword;
    private Button BtnSignin;


    public fragment_login() {

    }



    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        txtEmail=view.findViewById(R.id.txt_login_email);
        txtPassword=view.findViewById(R.id.txt_login_password);
        BtnSignin=view.findViewById(R.id.btn_signin);
        BtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword())
                    return;
            }
        });



        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }
private boolean validateEmail(){
        String TxtEmail=txtEmail.getEditText().getText().toString().trim();
        if(TxtEmail.isEmpty()){
            txtEmail.setError("Field can't be empty");
            return false; }
        else

            return true ;
    }
    private boolean validatePassword(){
        String TxtPassword=txtPassword.getEditText().getText().toString().trim();
        if(TxtPassword.isEmpty()){
            txtPassword.setError("field can't be empty");
            return false;
        }else
            return true;
    }



}
