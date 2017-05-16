package com.projectcourse2.group11.smallbusinessmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonEdit;
    private Button buttonSave;
    private Button buttonLogout;
    private Button buttonDeleteAccount;

    private LinearLayout ll;
    private ProgressDialog progressDialog;

    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, ref;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String uid = user.getUid();
    private String companyID;

    /**
     *  person is an person object that was read during loggin.
     *  It is called user everywhere else in the code
     */
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDeleteAccount = (Button) findViewById(R.id.buttonDeleteAccount);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);


        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);

        ll = (LinearLayout) findViewById(R.id.llMain);
        setEditTextTo(false);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        companyID=getIntent().getStringExtra("COMPANY_ID");
        person = (Person)getIntent().getSerializableExtra("USER");
        if (currentUser == null) {
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class).putExtra("USER",person));
        }

        prepareListData();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    private void prepareListData() {
//        ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("/companyEmployees/").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                for (DataSnapshot ds : dataSnapshot.getChildren()) {
////                    if (ds.getKey().equals(uid)) {
////                        company = "XBUVAedmKGTHl2bU4qNxGxLnaYd2";//ds.getRef().getParent().getKey();
////                        break;
////                    }
////                }
//                ref.child("/companyEmployees/" + companyID + "/").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listDataHeader.add("Personal Information");
                        listDataHeader.add("Account Information");
                        listDataHeader.add("In Company Information");

                        List<String> personalInfo = new ArrayList<>();
                        personalInfo.add("SSN:  "+person.getSSN());
                        personalInfo.add("First Name:   "+person.getFirstName());
                        personalInfo.add("Last Name: "+person.getLastName());
                        personalInfo.add("Email:    "+person.getEmail());
                        personalInfo.add("Phone:    "+person.getPhoneNumber());
//                        personalInfo.add("Address:  "+person.getAddress().getStreetNumber());
//                        personalInfo.add("PostCode: "+ person.getAddress().getPostalCode());
//                        personalInfo.add("City: "+person.getAddress().getCity());
//                        personalInfo.add("Country:  "+person.getAddress().getCountry());

                        List<String> inCompanyInfo = new ArrayList<>();
                        inCompanyInfo.add("Contract ID:");
                        inCompanyInfo.add("Title:   "+person.getPosition());
                        inCompanyInfo.add("Salary:");
                        inCompanyInfo.add("WorkingHour:");

                        listDataChild.put(listDataHeader.get(0), personalInfo);
                        listDataChild.put(listDataHeader.get(2), inCompanyInfo);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit) {
            setEditTextTo(true);
        }
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class).putExtra("COMPANY_ID",companyID).putExtra("USER",person));
        }
        if (v == buttonSave) {
            saveUserInformation();
        }
        if (v == buttonDeleteAccount) {
            deleteAccount();
        }
    }

    private void setEditTextTo(boolean b) {
        for (View view : ll.getTouchables()) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setEnabled(b);
                editText.setFocusable(b);
                editText.setFocusableInTouchMode(b);
            }
        }
    }

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

}

