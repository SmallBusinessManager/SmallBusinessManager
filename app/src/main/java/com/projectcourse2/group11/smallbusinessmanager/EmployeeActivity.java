package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Employee;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.User;

/**
 * Created by Phil on 5/18/2017.
 */

public class EmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    private ListView listView;

    ListAdapter mAdapter;
    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID, employeeID;
    private User selectedUser;
    private Person person;

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(EmployeeActivity.this, CompanyActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_employees);

        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User sUser = (User) parent.getItemAtPosition(position);
                selectedUser = sUser;
                if (!(person.getPosition().equals(Position.WORKER)||person.getPosition().equals(Position.TEAM_LEADER))) {
                    Intent intent = new Intent(EmployeeActivity.this, EmployeeSingleActivity.class).putExtra("EMPLOYEE", selectedUser);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("USER", person);
                    finish();
                    startActivity(intent);
                }
            }
        });

        progressDialog.dismiss();
        listView.setAdapter(mAdapter);

        if (person.getPosition().equals(Position.WORKER) || person.getPosition().equals(Position.TEAM_LEADER)) {
            fab.hide();
            fab.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(EmployeeActivity.this, EmployeeAddActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID));
        }
    }
}
