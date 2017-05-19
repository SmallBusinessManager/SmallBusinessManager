package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Accountant;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.TeamLeader;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

/**
 * Created by Phil on 5/18/2017.
 */

public class CompanyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameText, addressText, cityText;
    private Button saveButton, manageButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID;

    private Person person;
    private Company company;

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(CompanyActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        nameText = (EditText) findViewById(R.id.nameText);
        addressText = (EditText) findViewById(R.id.addressText);
        cityText = (EditText) findViewById(R.id.cityText);

        saveButton = (Button) findViewById(R.id.saveButton);
        manageButton = (Button) findViewById(R.id.manageButton);

        saveButton.setOnClickListener(this);
        manageButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        companyID = getIntent().getStringExtra("COMPANY_ID");
        person = (Person) getIntent().getSerializableExtra("USER");

        databaseReference.child("company").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(companyID)) {
                        company = ds.getValue(Company.class);

                        nameText.setText(company.getCompanyName());
                        addressText.setText(company.getAddress());
                        cityText.setText(company.getCity());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Database did not read.");
            }
        });

        //TODO Add another ValueEventListener to get address from correct Document

        if (currentUser == null) {
            finish();
            startActivity(new Intent(CompanyActivity.this, LoginActivity.class).putExtra("USER", person));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == saveButton) {
            saveCompanyInfo();
        }
        if (v == manageButton) {
            Intent intent = new Intent(CompanyActivity.this, EmployeeActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID);
            startActivity(intent);
            finish();

        }
    }

    private void saveCompanyInfo() {
        //TODO Save Company Address to correct Document
        DatabaseReference ref = databaseReference.child("company").child(companyID);
        ref.child("companyName").setValue(nameText.getText().toString());
        ref.child("address").setValue(addressText.getText().toString());
        ref.child("city").setValue(cityText.getText().toString());
    }
}
