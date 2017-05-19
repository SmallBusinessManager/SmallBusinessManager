package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Company;
import com.projectcourse2.group11.smallbusinessmanager.model.Employee;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.User;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

/**
 * Created by Phil on 5/18/2017.
 */

public class EmployeeActivity extends AppCompatActivity {
    private Button addEmployeeButton;
    private ListView listView;

    ListAdapter mAdapter;
    ProgressDialog progressDialog;

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
        startActivity(new Intent(EmployeeActivity.this, CompanyActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID));
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_employees);

        //listView = (ListView) findViewById(R.id.listView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        companyID = getIntent().getStringExtra("COMPANY_ID");
        person = (Person) getIntent().getSerializableExtra("USER");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Employees");
        progressDialog.show();

        //TODO Only shows first employee in the database
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyEmployees").child(companyID);
        mAdapter = new FirebaseListAdapter<User>(
                EmployeeActivity.this,
                User.class,
                R.layout.single_employee_item,
                ref) {
            @Override
            protected void populateView(View v, User model, int position) {
                TextView name = (TextView) v.findViewById(R.id.nameText);
                TextView employeePosition = (TextView) v.findViewById(R.id.positionText);
                name.setText(model.getFirstName() + " " + model.getLastName());
                employeePosition.setText(model.getPosition().toString());
            }
        };
        progressDialog.dismiss();
        listView.setAdapter(mAdapter);
    }
}
