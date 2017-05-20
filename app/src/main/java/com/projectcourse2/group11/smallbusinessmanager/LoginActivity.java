package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.projectcourse2.group11.smallbusinessmanager.model.Accountant;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.TeamLeader;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

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
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox saveLoginCheckBox;
    private String email, password;
    // TODO Change to FirebaseSimpleLogin Object if we have time to ensure authentication
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);

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
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        saveLogin = preferences.getBoolean("saveLogin", false);

        if (saveLogin) {

            String name = preferences.getString("username", "");
            String pass = preferences.getString("password", "");
            mEmailView.setText(name);
            mPasswordView.setText(pass);
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(LoginActivity.this, OpeningActivity.class));
    }

    private void userLogin() {

        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Logging In...");
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
                                        Position pos = d.child("position").getValue(Position.class);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        switch (pos) {
                                            case ACCOUNTANT:
                                                Accountant accountant = d.getValue(Accountant.class);
                                                intent.putExtra("USER", accountant);
                                                break;
                                            case MANAGER:
                                                Manager manager = d.getValue(Manager.class);
                                                intent.putExtra("USER", manager);
                                                break;
                                            case TEAM_LEADER:
                                                TeamLeader teamLeader = d.getValue(TeamLeader.class);
                                                intent.putExtra("USER", teamLeader);
                                                break;
                                            case WORKER:
                                                Worker worker = d.getValue(Worker.class);
                                                intent.putExtra("USER", worker);
                                                break;
                                            default:
                                                Toast.makeText(LoginActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                                break;
                                        }

                                        intent.putExtra("COMPANY_ID", company);
//                                        intent.putExtra("POSITION",pos.toString());
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
            if (saveLoginCheckBox.isChecked()) {
                editor.putBoolean("saveLogin", true);
                editor.putString("username", mEmailView.getText().toString());
                editor.putString("password", mPasswordView.getText().toString());
                editor.apply();
            } else {
                editor.clear();
                editor.apply();
            }
            userLogin();
        }
        if (v == tvForgot) {
            //reset password
        }
    }


}