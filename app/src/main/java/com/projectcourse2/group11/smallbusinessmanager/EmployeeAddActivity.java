package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Employee;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;

/**
 * Created by Phil on 5/18/2017.
 */

public class EmployeeAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addButton;
    private EditText firstNameText, lastNameText, socialText, emailText, phoneText;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID;

    private Person person;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(EmployeeAddActivity.this, EmployeeActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID));
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_employee_add);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        socialText = (EditText) findViewById(R.id.socialText);
        emailText = (EditText) findViewById(R.id.emailText);
        phoneText = (EditText) findViewById(R.id.phoneText);


    }

    @Override
    public void onClick(View v) {
        if (v == addButton){
            databaseReference.child("companyEmployees").child(companyID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (firstNameText != null && lastNameText != null && socialText != null && emailText != null && phoneText != null){
                        //TODO Assign employee a UID and
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", "Could Not Add Employee");
                }
            });
        }
    }
}
