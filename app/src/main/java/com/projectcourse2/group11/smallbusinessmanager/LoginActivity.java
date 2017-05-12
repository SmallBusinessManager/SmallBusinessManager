package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email and password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView tvRegister;
    private Button mEmailSignInButton;
    private TextView tvForgot;
    private String company;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    // TODO Change to FirebaseSimpleLogin Object if we have time to ensure authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(this);

        mEmailSignInButton = (Button) findViewById(R.id.button_signIn);
        mEmailSignInButton.setOnClickListener(this);

        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(this);

    }

    private void userLogin() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Login...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("/companyEmployees/").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot d : ds.getChildren()) {
                                    if (d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        company = d.getRef().getParent().getKey();
                                        Toast.makeText(LoginActivity.this,"Welcome!" , Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("COMPANY_ID", company);
                                        finish();
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "Failed to load the company", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
//                    finish();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {
            finish();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
        if (v == mEmailSignInButton) {
            userLogin();
        }
        if (v == tvForgot) {
            //reset password
        }
    }


}