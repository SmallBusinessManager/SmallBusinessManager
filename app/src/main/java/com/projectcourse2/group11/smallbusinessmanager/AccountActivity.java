package com.projectcourse2.group11.smallbusinessmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Employee;

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

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        }

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

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
                // TODO Auto-generated method stub
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
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Personal Information");
        listDataHeader.add("Account Information");
        listDataHeader.add("In Company Information");
        List<String> personalInfo = new ArrayList<>();
        personalInfo.add("SSN:19910115-0000");
        personalInfo.add("LastName:Wang");
        personalInfo.add("Email:danfeng.trondset@gmail.com");
        personalInfo.add("Phone:0706556305");
        personalInfo.add("Age:25");
        personalInfo.add("Gender:FEMALE");
        personalInfo.add("Address:Fältvägen 3");
        personalInfo.add("PostCode:12345");
        personalInfo.add("City:Kristianstad");
        personalInfo.add("Country:Sweden");

        List<String> accountInfo = new ArrayList<>();
        accountInfo.add("UserName:Danfeng");
        accountInfo.add("Password:******");

        List<String> inCompanyInfo = new ArrayList<>();
        inCompanyInfo.add("Contract ID:sbm45678");
        inCompanyInfo.add("Title:Employee");
        inCompanyInfo.add("Salary:0");
        inCompanyInfo.add("WorkingHour:100.5");

        databaseReference.child(currentUser.getUid()).child("firstName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.getValue(String.class);
                Log.d("TAG", firstName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listDataChild.put(listDataHeader.get(0), personalInfo);
        listDataChild.put(listDataHeader.get(1), accountInfo);
        listDataChild.put(listDataHeader.get(2), inCompanyInfo);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit) {
            setEditTextTo(true);
        }
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
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
