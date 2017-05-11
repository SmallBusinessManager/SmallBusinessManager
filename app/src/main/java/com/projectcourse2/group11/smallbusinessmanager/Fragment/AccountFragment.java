package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.projectcourse2.group11.smallbusinessmanager.ExpandableListAdapter;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.CreateOrderFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.LoginFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.OpeningFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.RegisterFragment;
import com.projectcourse2.group11.smallbusinessmanager.OpeningActivity;
import com.projectcourse2.group11.smallbusinessmanager.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener {
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
    private String company;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account,null);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonDeleteAccount = (Button) view.findViewById(R.id.buttonDeleteAccount);
        buttonEdit = (Button) view.findViewById(R.id.buttonEdit);

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);

        ll = (LinearLayout) view.findViewById(R.id.llMain);
        setEditTextTo(false);

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), OpeningActivity.class));
        }

        prepareListData();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

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
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void prepareListData() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("/companyEmployees/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(uid)) {
                        company = "XBUVAedmKGTHl2bU4qNxGxLnaYd2";//ds.getRef().getParent().getKey();
                        break;
                    }
                }
                ref.child("/companyEmployees/" + company + "/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listDataHeader.add("Personal Information");
                        listDataHeader.add("Account Information");
                        listDataHeader.add("In Company Information");

                        List<String> personalInfo = new ArrayList<>();
                        personalInfo.add("SSN:");
                        personalInfo.add("LastName:");
                        personalInfo.add("Email:");
                        personalInfo.add("Phone:");
                        personalInfo.add("Age:");
                        personalInfo.add("Gender:");
                        personalInfo.add("Address:");
                        personalInfo.add("PostCode:");
                        personalInfo.add("City:");
                        personalInfo.add("Country:");

                        List<String> accountInfo = new ArrayList<>();
                        accountInfo.add("UserName:");
                        accountInfo.add("Password:");

                        List<String> inCompanyInfo = new ArrayList<>();
                        inCompanyInfo.add("Contract ID:");
                        inCompanyInfo.add("Title:");
                        inCompanyInfo.add("Salary:");
                        inCompanyInfo.add("WorkingHour:");

                        listDataChild.put(listDataHeader.get(0), personalInfo);
                        listDataChild.put(listDataHeader.get(1), accountInfo);
                        listDataChild.put(listDataHeader.get(2), inCompanyInfo);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEdit) {
            setEditTextTo(true);
        }
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), OpeningActivity.class));

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
        Toast.makeText(getActivity(), "Information saved", Toast.LENGTH_LONG).show();
    }

    private void deleteAccount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(),OpeningActivity.class));

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