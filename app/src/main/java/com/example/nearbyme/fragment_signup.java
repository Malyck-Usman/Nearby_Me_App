package com.example.nearbyme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nearbyme.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class fragment_signup extends Fragment {

    TextInputLayout edt_Name,edt_Phone,edt_Email,edt_Password,edt_Conform_Password;
    Button btn_Signup;
    private FirebaseFirestore firestoreDatabase;


    public fragment_signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_signup, container, false);
initViews(view);
firestoreDatabase =FirebaseFirestore.getInstance();

btn_Signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        if(!ValidateEmail()|!ValidatePhoneNo()|!ValidatePassword()|!ValidateConfirmPassword()){
//            return;
//        }
        String name=edt_Name.getEditText().getText().toString().trim();
        String phone_no=edt_Phone.getEditText().getText().toString().trim();
        String e_mail=edt_Email.getEditText().getText().toString().trim();
        String password=edt_Password.getEditText().getText().toString().trim();
        String password_confirm=edt_Conform_Password.getEditText().getText().toString().trim();


        //for debuging
        Log.d("TAG",""+name);
        Log.d("TAG",""+phone_no);
        Log.d("TAG",""+e_mail);
        Log.d("TAG",""+password);
        Log.d("TAG",""+password_confirm);
        Map<String,Object> data=new HashMap<>();
        data.put("name",name);
        data.put("phone_no",phone_no);
        firestoreDatabase.collection("users").document().set(data)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                    }
                    if(task.isSuccessful()){
                        Log.d("TAG","Done");
                Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("TAG","Error"+task.getException().getMessage().toString());
                    Toast.makeText(getContext(),"failed to add data",Toast.LENGTH_LONG).show();
                }
                 });
//        User user=new User(name,phone_no,e_mail,password);
//        Task<Void> task = firestoreDatabase.collection("users")
//                .document("malik")
//                .set(user);
//                 task.addOnCompleteListener(new OnCompleteListener<Void>() {
//                     @Override
//                     public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Log.d("TAG","Done");
//                    Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Log.d("TAG","Error"+task.getException().toString());
//                        Toast.makeText(getContext(),"failed to add data",Toast.LENGTH_LONG).show();
//                    }
//                     }
//                 });

    }
});

        return view;
    }









    private void initViews(View view){
        edt_Name=view.findViewById(R.id.edt_name);
        edt_Phone=view.findViewById(R.id.edt_phone);
        edt_Email=view.findViewById(R.id.edt_email);
        edt_Password=view.findViewById(R.id.edt_password);
        edt_Conform_Password=view.findViewById(R.id.edt_confirm_password);
        btn_Signup=view.findViewById(R.id.btn_signup);


    }
    private boolean ValidateEmail(){
String email=edt_Email.getEditText().getText().toString().trim();
if(email.isEmpty()){
    edt_Email.setError("E-mail is required ");
    //edt_Email.isFocused();
}else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
    edt_Email.setError("Invalid e-mail address");
}

else {
    edt_Email.setError(null);
    return true;
}

        return false;
    }
    private boolean ValidatePhoneNo(){
        String phone_no=edt_Phone.getEditText().getText().toString().trim();
        if(phone_no.isEmpty()){
            edt_Phone.setError("Mobile number is required ");
            //edt_Phone.isFocused();
        }else if(phone_no.length()!=11){
            edt_Phone.setError("Mobile number not valid ");
        }

        else {
            edt_Phone.setError(null);
            return true;
        }

        return false;
    }
    private boolean ValidatePassword(){
        String password=edt_Phone.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            edt_Password.setError("password is required ");
            //edt_password.isFocused();
        }else {
            edt_Password.setError(null);
            return true;
        }

        return false;
    }
    private boolean ValidateConfirmPassword(){
        String password=edt_Password.getEditText().getText().toString().trim();
        String confirn_password=edt_Conform_Password.getEditText().getText().toString().trim();
        if(!(password.equals(confirn_password))){
            edt_Conform_Password.setError("password dosen't match ");
            //edt_password.isFocused();
        }else {
            edt_Conform_Password.setError(null);
            return true;
        }

        return false;
    }




}
