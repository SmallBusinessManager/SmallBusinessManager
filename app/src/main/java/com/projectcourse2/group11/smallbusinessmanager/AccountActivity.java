package com.projectcourse2.group11.smallbusinessmanager;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSave;
    private Button buttonLogout;
    private Button buttonDeleteAccount;


    private EditText firstNameText, lastNameText, socialSecurityText, emailText, phoneText;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(AccountActivity.this, MainActivity.class));

    }

    private LinearLayout ll;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, ref;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID;

    /**
     * person is an person object that was read during loggin.
     * It is called user everywhere else in the code
     */
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        /*final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDeleteAccount = (Button) findViewById(R.id.buttonDeleteAccount);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        socialSecurityText = (EditText) findViewById(R.id.socialSecurityText);
        emailText = (EditText) findViewById(R.id.emailText);
        phoneText = (EditText) findViewById(R.id.phoneText);


        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);

        ll = (LinearLayout) findViewById(R.id.llMain);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        companyID = getIntent().getStringExtra("COMPANY_ID");
        person = (Person) getIntent().getSerializableExtra("USER");
        if (currentUser == null) {
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class).putExtra("USER", person));
        }

        /** start **/
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        socialSecurityText.setText(person.getSSN());
        emailText.setText(person.getEmail());
        phoneText.setText(person.getPhoneNumber());

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", person));
        }
        if (v == buttonSave) {
            saveUserInformation();
        }
        if (v == buttonDeleteAccount) {
            deleteAccount();
        }
    }


    //TODO Saving currently crashes
    private void saveUserInformation() {
        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        ref.child("companyEmployees").child(companyID).child(user.getUid()).setValue(person);

    }

    private void deleteAccount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this account?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.setMessage("Deleting account");
                        progressDialog.show();
                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child(currentUser.getUid()).removeValue();
                                    progressDialog.dismiss();
                                    AccountActivity.this.finish();
                                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

