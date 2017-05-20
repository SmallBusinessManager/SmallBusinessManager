package com.projectcourse2.group11.smallbusinessmanager;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Accountant;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Employee;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.TeamLeader;
import com.projectcourse2.group11.smallbusinessmanager.model.User;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.security.acl.Owner;

/**
 * Created by Phil on 5/18/2017.
 */

public class EmployeeAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addButton;
    private EditText firstNameText, lastNameText, socialText, emailText, phoneText, passwordText;
    private RadioGroup positionGroup;
    private RadioButton workerRadio, teamLeaderRadio, accountantRadio, managerRadio;

    private static final int radioID1 = 1000;
    private static final int radioID2 = 2000;
    private static final int radioID3 = 3000;
    private static final int radioID4 = 4000;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progressDialog;

    private String companyID;
    private String pos;

    private Person person;

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(EmployeeAddActivity.this, EmployeeActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_employee_add);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        socialText = (EditText) findViewById(R.id.socialText);
        emailText = (EditText) findViewById(R.id.emailText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        positionGroup = (RadioGroup) findViewById(R.id.positionGroup);
        workerRadio = (RadioButton) findViewById(R.id.workerRadio);
        teamLeaderRadio = (RadioButton) findViewById(R.id.teamLeaderRadio);
        accountantRadio = (RadioButton) findViewById(R.id.accountantRadio);
        managerRadio = (RadioButton) findViewById(R.id.managerRadio);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        companyID = getIntent().getStringExtra("COMPANY_ID");
        person = (Person) getIntent().getSerializableExtra("USER");

        pos = "Worker";

        positionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                pos = checkedRadioButton.getText().toString();
                Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == addButton) {
            //TODO Assign employee a UID and add to database
            final String SSN = socialText.getText().toString();
            final String firstName = firstNameText.getText().toString();
            final String lastName = lastNameText.getText().toString();
            final String email = emailText.getText().toString();
            final String phone = phoneText.getText().toString();
            final String password = passwordText.getText().toString();

            if (firstNameText != null && lastNameText != null && socialText != null && emailText != null && phoneText != null) {
                final String token = FirebaseAuth.getInstance().getCurrentUser().getToken(true).toString();
                /** Create new employee account **/
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String UID = user.getUid();

                                    switch (pos) {
                                        case "Worker":
                                            Worker worker = new Worker(SSN, firstName, lastName, phone, email, UID);
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                            databaseReference.child("companyEmployees").child(companyID).child(UID).setValue(worker);
                                            break;
                                        case "Team Leader":
                                            TeamLeader teamLead = new TeamLeader(SSN, firstName, lastName, phone, email, UID);
                                            databaseReference = FirebaseDatabase.getInstance().getReference();
                                            databaseReference.child("companyEmployees").child(companyID).child(UID).setValue(teamLead);
                                            break;
                                        case "Accountant":
                                            Accountant accountant = new Accountant(SSN, firstName, lastName, phone, email, UID, null, 10);
                                            databaseReference = FirebaseDatabase.getInstance().getReference();
                                            databaseReference.child("companyEmployees").child(companyID).child(UID).setValue(accountant);
                                            break;
                                        case "Manager":
                                            Manager manager = new Manager(SSN, firstName, lastName, phone, email, UID);
                                            databaseReference = FirebaseDatabase.getInstance().getReference();
                                            databaseReference.child("companyEmployees").child(companyID).child(UID).setValue(manager);
                                            break;
                                    }
                                    FirebaseAuth.getInstance().signInWithCustomToken(token);
                                    Toast.makeText(EmployeeAddActivity.this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    intent.setClass(EmployeeAddActivity.this, EmployeeActivity.class);
                                    EmployeeAddActivity.this.startActivity(intent);


                                } else {
                                    Toast.makeText(EmployeeAddActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        }


    }
}

