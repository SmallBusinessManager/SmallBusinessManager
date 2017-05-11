package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 07/05/2017.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView email_register;
    private EditText password_register;
    private EditText password_again_register;
    private Button registerButton;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        View view = inflater.inflate(R.layout.activity_register, container, false);

        email_register = (AutoCompleteTextView) view.findViewById(R.id.email_register);
        password_register = (EditText) view.findViewById(R.id.password_register);
        password_again_register = (EditText) view.findViewById(R.id.password_again_register);
        registerButton = (Button) view.findViewById(R.id.register_button);
        tvLogin = (TextView) view.findViewById(R.id.tvLogin);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registerUser();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment newFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.opening_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void registerUser(){
        String email=email_register.getText().toString().trim();
        String password=password_register.getText().toString().trim();
        String rePassword=password_again_register.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_SHORT).show();
            return; //stop function executing further
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePassword)){
            Toast.makeText(getActivity(),"Please re-enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(rePassword)){
            Toast.makeText(getActivity(),"Please enter same password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"User registered successfully",Toast.LENGTH_SHORT).show();
                    Fragment newFragment = new LoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.opening_frame, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    Toast.makeText(getActivity(),"registered failed",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });

    }
    @Override
    public void onClick(View v) {

    }
}