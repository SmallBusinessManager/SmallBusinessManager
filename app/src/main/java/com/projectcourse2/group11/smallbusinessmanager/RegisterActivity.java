package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import org.w3c.dom.Text;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private AutoCompleteTextView email_register;
    private EditText password_register, password_again_register, etCompanyName_Register, etFirstName_Register, etLastName_Register, etSSN_Register;
    private Button registerButton;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        etCompanyName_Register = (EditText) findViewById(R.id.etCompanyName_register);
        etFirstName_Register = (EditText) findViewById(R.id.etFirstName_register);
        etLastName_Register = (EditText) findViewById(R.id.etLastName_register);
        etSSN_Register = (EditText) findViewById(R.id.etSSN_register);
        email_register = (AutoCompleteTextView) findViewById(R.id.email_register);
        password_register = (EditText) findViewById(R.id.password_register);
        password_again_register = (EditText) findViewById(R.id.password_again_register);
        registerButton = (Button) findViewById(R.id.register_button);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        registerButton.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            registerUser();
        }
        if (v == tvLogin) {
            Intent intent = new Intent(RegisterActivity.this, OrderCreation.class);
            startActivity(intent);
        }

    }

    private void registerUser() {
        final String newCompanyName = etCompanyName_Register.getText().toString();
        final String firstName = etFirstName_Register.getText().toString();
        final String lastName = etLastName_Register.getText().toString();
        final String ssn = etSSN_Register.getText().toString();
        final String email = email_register.getText().toString().trim();
        String password = password_register.getText().toString().trim();
        String rePassword = password_again_register.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter e-mail", Toast.LENGTH_SHORT).show();
            return; //stop function executing further
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePassword)) {
            Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Please enter same password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        //TODO Condition 103-112
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Company newCompany = new Company(newCompanyName, uid);
        Manager owner = new Manager(ssn, firstName, lastName, null, email, uid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.push().getKey();
        databaseReference.child("company").child(key).setValue(newCompany);
        databaseReference.child("worker").child(uid).setValue(owner);

        // Register New Company Account & Owner Account
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "User registered failed", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void saveToDatabase(Company newCompany, Worker owner) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("company").setValue(newCompany);
        databaseReference.child("worker").setValue(owner);


    }
}

