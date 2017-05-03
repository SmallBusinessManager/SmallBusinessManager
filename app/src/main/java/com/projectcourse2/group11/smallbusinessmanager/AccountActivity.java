package com.projectcourse2.group11.smallbusinessmanager;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Address;
import com.projectcourse2.group11.smallbusinessmanager.model.TestPerson;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button buttonLogout;
    private Button buttonSave;
    private Button buttonDeleteAccount;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUserName;
    private EditText etEmail;
    private EditText etStreet;
    private EditText etPost;
    private EditText etCountry;
    private EditText etPhone;
    private EditText etSSN;
    private EditText etCity;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        currentUser = firebaseAuth.getCurrentUser();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDeleteAccount = (Button) findViewById(R.id.buttonDeleteAccount);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPost = (EditText) findViewById(R.id.etPost);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etStreet = (EditText) findViewById(R.id.etStreet);
        etSSN = (EditText) findViewById(R.id.etSSN);
        etCity = (EditText) findViewById(R.id.etCity);

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == buttonSave) {
            saveUserInformation();
        }
        if (view == buttonDeleteAccount) {
            deleteAccount();
        }
    }

    private void saveUserInformation() {
        String ssn = etSSN.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String phoneNumber = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        Address address = new Address(etStreet.getText().toString(), etCity.getText().toString(), etPost.getText().toString(), etCountry.getText().toString());

        TestPerson testPerson = new TestPerson(ssn, firstName, lastName, phoneNumber, email, address);
        //todo data validation
        databaseReference.child(currentUser.getUid()).setValue(testPerson);
        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
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

}

