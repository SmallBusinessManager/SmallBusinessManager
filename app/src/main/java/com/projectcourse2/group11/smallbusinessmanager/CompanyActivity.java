package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;

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
        finish();
        startActivity(new Intent(CompanyActivity.this, MainActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
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
        company = (Company) getIntent().getSerializableExtra("COMPANY");
        if (currentUser == null) {
            finish();
            startActivity(new Intent(CompanyActivity.this, LoginActivity.class).putExtra("USER", person));
        }

        nameText.setText(company.getCompanyName());
        addressText.setText(company.getAddress());
        cityText.setText(company.getCity());

    }

    @Override
    public void onClick(View v){
        if (v == saveButton){
            saveCompanyInfo();
        }
        if (v == manageButton){
            //TODO Switch to EmployeeActivity
            finish();
            
        }
    }

    private void saveCompanyInfo(){
        //TODO Implement saving company information
    }
}
