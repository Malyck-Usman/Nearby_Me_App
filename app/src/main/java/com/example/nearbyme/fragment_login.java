package com.example.nearbyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyme.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class fragment_login extends Fragment implements View.OnClickListener {
    private TextInputLayout txtEmail, txtPassword;
    private TextView tv_Signup;
    private Button BtnSignin;
    private FirebaseFirestore mDatabase;
    private final String TAG = "TAG";
    private GetUserIdInterface getUserId;


    public fragment_login() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Login");
        initViews(view);
        // checkPreLogin();

        mDatabase = FirebaseFirestore.getInstance();


        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(View view) {
        txtEmail = view.findViewById(R.id.txt_login_email);
        txtPassword = view.findViewById(R.id.txt_login_password);
        BtnSignin = view.findViewById(R.id.btn_signin);
        tv_Signup = view.findViewById(R.id.tv_signup);
        BtnSignin.setOnClickListener(this);
        tv_Signup.setOnClickListener(this);

    }


    private boolean validateEmail() {
        String TxtEmail = txtEmail.getEditText().getText().toString().trim();
        if (TxtEmail.isEmpty()) {
            txtEmail.setError("Field can't be empty");
            return false;
        } else

            return true;
    }

    private boolean validatePassword() {
        String TxtPassword = txtPassword.getEditText().getText().toString().trim();
        if (TxtPassword.isEmpty()) {
            txtPassword.setError("Field can't be empty");
            return false;
        } else
            return true;
    }


    private void checkPreLogin() {
        SharedPreferences checkPreLogin = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE);

        String uid = checkPreLogin.getString(getString(R.string.DOCUMENT_ID), "");
        if (uid.equals("")) {
            Log.d(TAG, "sp value is null");
        } else {
            Log.d(TAG, "sp value is " + uid);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_dashboard())
                    .commit();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    String e_mail = txtEmail.getEditText().getText().toString().trim();
                    String password = txtPassword.getEditText().getText().toString().trim();
                    mDatabase.collection("users")
                            .whereEqualTo("e_mail", e_mail)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        Toast.makeText(getContext(), "wrong Email or password", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {

                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                           // Log.d(TAG, "" + queryDocumentSnapshot.getId());
                                            String id = queryDocumentSnapshot.getId();
                                            User user=queryDocumentSnapshot.toObject(User.class);
                                            String privilege=user.getPrivilege();
                                            SharedPreferences.Editor sp_login = getActivity().getSharedPreferences(getString(R.string.M_LOGIN_FILE), MODE_PRIVATE).edit();
                                            sp_login.putString(getString(R.string.DOCUMENT_ID), id);
                                            sp_login.putString(getString(R.string.M_PRIVILEGE),privilege);
                                            sp_login.apply();
                                            ((MainActivity)getActivity()).ShowUser();

                                            getUserId.OnUidGet(id);

                                            Fragment fragment=getTargetFragment();


                                           getActivity().getSupportFragmentManager()
                                                   .beginTransaction().replace(R.id.fragment_container,fragment).commit();
                                        }


                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "failed to get values");
                                }
                            });


                }
                break;
            case R.id.tv_signup:
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_signup()).addToBackStack(null).commit();
                break;
        }
    }


    public interface GetUserIdInterface {
        void OnUidGet(String u_id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            getUserId = (GetUserIdInterface) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement GetUserIdInterface");

        }
    }
}
