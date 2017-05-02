package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private AutoCompleteTextView email_register;
    private EditText password_register;
    private EditText password_again_register;
    private Button registerButton;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        email_register=(AutoCompleteTextView)findViewById(R.id.email_register);
        password_register=(EditText)findViewById(R.id.password_register);
        password_again_register=(EditText)findViewById(R.id.password_again_register);
        registerButton=(Button)findViewById(R.id.register_button);
        tvLogin=(TextView)findViewById(R.id.tvLogin);

        registerButton.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==registerButton){
            registerUser();
        }
        if (v==tvLogin){
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }
    private void registerUser(){
        String email=email_register.getText().toString().trim();
        String password=password_register.getText().toString().trim();
        String rePassword=password_again_register.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return; //stop function executing further
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePassword)){
            Toast.makeText(this,"Please re-enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(rePassword)){
            Toast.makeText(this,"Please enter same password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"User registered successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    RegisterActivity.this.startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivity.this,"User registered failed",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}

