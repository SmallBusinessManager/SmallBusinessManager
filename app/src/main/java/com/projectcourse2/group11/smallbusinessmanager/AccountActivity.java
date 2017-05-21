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

import android.view.LayoutInflater;
import android.view.Menu;
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
import com.projectcourse2.group11.smallbusinessmanager.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLogout;
    private Button buttonDeleteAccount;
    private Button btResetPass;
    private EditText firstNameText, lastNameText, socialSecurityText, emailText, phoneText;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private String companyID;
    private Person person;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAccount);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonDeleteAccount = (Button) findViewById(R.id.buttonDeleteAccount);
        btResetPass = (Button) findViewById(R.id.bt_resetPass);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        socialSecurityText = (EditText) findViewById(R.id.socialSecurityText);
        emailText = (EditText) findViewById(R.id.emailText);
        phoneText = (EditText) findViewById(R.id.phoneText);

        buttonLogout.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);
        btResetPass.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent()!=null){
            companyID = getIntent().getStringExtra("COMPANY_ID");
            person = (Person) getIntent().getSerializableExtra("USER");
        }

        if (currentUser == null) {
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        }

        /** start **/
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        socialSecurityText.setText(person.getSsn());
        emailText.setText(person.getEmail());
        phoneText.setText(person.getPhoneNumber());
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);
            finish();
            firebaseAuth.signOut();

        }
        if (v == buttonDeleteAccount) {
            deleteAccount();
        }
        if (v == btResetPass) {
            LayoutInflater li = LayoutInflater.from(this);
            View dialogView = li.inflate(R.layout.password_reset_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setTitle("Reset Password");
            // alertDialogBuilder.setIcon(R.drawable.ic_launcher);
            alertDialogBuilder.setView(dialogView);
            final EditText et_oldPass = (EditText) dialogView
                    .findViewById(R.id.et_oldPass);
            final EditText passwordD = (EditText) dialogView
                    .findViewById(R.id.et_resetPasswordD);
            final EditText passwordAD = (EditText) dialogView
                    .findViewById(R.id.et_resetPasswordAD);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    //cannot get password from Firebase, therefor verify current password
                                    if (passwordD.getText().toString().equals(passwordAD.getText().toString())) {
                                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(passwordD.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AccountActivity.this, "Reset password successfully! Pleas login again", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(new Intent(AccountActivity.this, OpeningActivity.class));
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(AccountActivity.this, "You entered two different passwords, please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void saveUserInformation() {
        String firstName=firstNameText.getText().toString();
        String lastName=lastNameText.getText().toString();
        String ssn=socialSecurityText.getText().toString();
        String email=emailText.getText().toString();
        String phoneNr=phoneText.getText().toString();

        user=new User(ssn,firstName,lastName,phoneNr,email,person.getPosition(),currentUser.getUid());
        person.setFirstName(firstName);
        person.setFirstName(lastName);
        person.setFirstName(phoneNr);
        person.setFirstName(email);

        databaseReference.child("companyEmployees").child(companyID).updateChildren(user.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AccountActivity.this, "Information saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this account?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference.child("companyEmployees").child(companyID).child(currentUser.getUid()).removeValue();
                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AccountActivity.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                                    AccountActivity.this.finish();
                                    startActivity(new Intent(AccountActivity.this, OpeningActivity.class));
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
                startActivity(new Intent(AccountActivity.this, MainActivity.class).putExtra("USER", person).putExtra("COMPANY_ID", companyID));
                break;
            case R.id.nav_save_changes:
                saveUserInformation();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_account, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(AccountActivity.this,ProjectActivity.class);
        startActivity(intent);
        finish();
    }
}

