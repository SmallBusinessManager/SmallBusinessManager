package com.projectcourse2.group11.smallbusinessmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.User;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;

/**
 * Created by Phil on 5/19/2017.
 */

public class EmployeeSingleActivity extends AppCompatActivity {
    private TextView firstNameText, lastNameText, socialText, emailText, phoneText, positionText;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID, employeeID;
    private User selectedUser;
    private Person person;

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(EmployeeSingleActivity.this, EmployeeActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_employee_single);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firstNameText = (TextView) findViewById(R.id.firstNameText);
        lastNameText = (TextView) findViewById(R.id.lastNameText);
        socialText = (TextView) findViewById(R.id.socialText);
        emailText = (TextView) findViewById(R.id.emailText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        positionText = (TextView) findViewById(R.id.positionText);

        if (getIntent() != null) {
            person = (Person) getIntent().getSerializableExtra("USER");
            companyID = getIntent().getStringExtra("COMPANY_ID");
            selectedUser = (User) getIntent().getSerializableExtra("EMPLOYEE");
        }

        firstNameText.setText(selectedUser.getFirstName());
        lastNameText.setText(selectedUser.getLastName());
        socialText.setText(selectedUser.getSSN());
        emailText.setText(selectedUser.getEmail());
        phoneText.setText(selectedUser.getPhoneNumber());
        positionText.setText(selectedUser.getPosition().toString());

        databaseReference.child("companyEmployees").child(companyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot d : ds.getChildren()) {
                        if (d.getKey().equals(selectedUser.getSSN())) {
                            employeeID = ds.getKey();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_single_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(EmployeeSingleActivity.this, EmployeeActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", person));
                break;
            case R.id.nav_delete_employee:
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeSingleActivity.this);
                builder.setTitle("Confirmation Required");
                builder.setMessage("Are you sure you want to remove " + selectedUser.getFirstName() + "" + selectedUser.getLastName() + " from the company?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                                FirebaseDatabase.getInstance().getReference().child("companyEmployees").child(employeeID).removeValue();

                                Toast.makeText(EmployeeSingleActivity.this, "Employee Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(EmployeeSingleActivity.this, EmployeeActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", person));
                            }
                        })

                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                Toast.makeText(this, "Could Not Remove Employee", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
