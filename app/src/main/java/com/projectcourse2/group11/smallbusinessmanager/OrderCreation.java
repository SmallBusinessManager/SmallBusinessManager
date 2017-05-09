package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Address;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Order;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.Status;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivana on 5/2/2017.
 * Creating the order, writing it to the fireBase and reading workers from it.
 */

public class OrderCreation extends Activity implements View.OnClickListener {
    private Button buttonOK;
    private Button buttonCancel;
    private List<Worker> workerList = new ArrayList<>();
    private List<Manager> managerList = new ArrayList<>();
    private NumberPicker workerView;
    private EditText descriptionView;
    private Worker selectedWorker;
    private DatabaseReference ref;
    private String company;
    private String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Reading all worker from database and sorting by position
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("/companyEmployees/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                     if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                         company = ds.getRef().getParent().getKey();
                        break;
                    }
                }
                ref =FirebaseDatabase.getInstance().getReference();
                ref.child("/companyEmployees/"+company+"/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String ssn, email, firstName, lastName, phoneNumber;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UID = ds.getKey();
                                email = ds.child("email").getValue(String.class);
                                firstName = ds.child("firstName").getValue(String.class);
                                lastName = ds.child("lastName").getValue(String.class);
                                phoneNumber = ds.child("phoneNumber").getValue(String.class);
                                Position pos = ds.child("position").getValue(Position.class);
                                ssn = ds.child("SSN").getValue(String.class);

                                //separating managers and workers
                                if (pos.equals(Position.WORKER)) {
                                    Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email, UID);
                                    workerList.add(w);
                                } else if (pos.equals(Position.MANAGER)) {
                                    Manager man = new Manager(ssn, firstName, lastName, phoneNumber, email, UID);
                                    managerList.add(man);
                                }
                            }
                            populateList();
                        }catch (Exception e){
                            String[] err = {UID};
                            workerView.setDisplayedValues(err);
                            System.out.println(UID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String[] err = {"Error while loading"};
                        workerView.setDisplayedValues(err);
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
                String[] err = {"Error while loading company."};
                workerView.setDisplayedValues(err);
            }
        });
//        ref =FirebaseDatabase.getInstance().getReference();
//        ref.child("/companyEmployees/"+company+"/").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    String ssn, email, firstName, lastName, phoneNumber;
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        UID = ds.getKey();
//                        email = ds.child("email").getValue(String.class);
//                        firstName = ds.child("firstName").getValue(String.class);
//                        lastName = ds.child("lastName").getValue(String.class);
//                        phoneNumber = ds.child("phoneNumber").getValue(String.class);
//                        Position pos = ds.child("position").getValue(Position.class);
//                        ssn = ds.child("SSN").getValue(String.class);
//
//                        //separating managers and workers
//                        if (pos.equals(Position.WORKER)) {
//                            Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email, UID);
//                            workerList.add(w);
//                        } else if (pos.equals(Position.MANAGER)) {
//                            Manager man = new Manager(ssn, firstName, lastName, phoneNumber, email, UID);
//                            managerList.add(man);
//                        }
//                    }
//                    populateList();
//                }catch (Exception e){
//                    String[] err = {UID};
//                    workerView.setDisplayedValues(err);
//                    System.out.println(UID);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                String[] err = {"Error while loading"};
//                workerView.setDisplayedValues(err);
//            }
//        });



        //preparing UI
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order);

        //Get new reference to the database, the previous was in /workers/
        ref = FirebaseDatabase.getInstance().getReference();

        buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        descriptionView = (EditText) findViewById(R.id.orderDescription);
        workerView = (NumberPicker) findViewById(R.id.workerPicker);
        workerView.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //Number picker with worker names instead of numbers
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedWorker = workerList.get(newVal);
            }
        };

        workerView.setOnValueChangedListener(myValChangedListener);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                Intent MainIntent = new Intent(OrderCreation.this, OpeningActivity.class);
                startActivity(MainIntent);
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWorker == null) {
                    selectedWorker = workerList.get(0);
                }
                createOrder();
                finish();
                startActivity(new Intent(OrderCreation.this, AccountActivity.class));
            }
        });

        //it takes time to load and populate number picker, lets just say its loading
        String[] tmp = {"Loading"};
        workerView.setDisplayedValues(tmp);
    }

    @Override
    public void onClick(View v) {
//        if (v==buttonOK){
//            createOrder();
//        }
    }

    /**
     *  This part right here is for writing to the database
     *  Some values are dummy values
     */
    private void createOrder() {
        String description = descriptionView.getText().toString().trim();
        ref = FirebaseDatabase.getInstance().getReference();
        //Order order = new Order(description, selectedWorker, new Project( managerList.get(0), new Date(21, 12, 2017), new Date(22, 12, 2017)));
        //ref.child("/companyWorkOrders/"+company+"/").updateChildren(order.toHashMap());
    }

    /**
     *  This populates the list of number picker.
     *  This method takes just the first and the last name of each worker retrieved from the database
     *  adds it to a string array
     *  and passes the string array to number picker
     */
    private void populateList(){
        String[] workersNames;
        if (workerList.size()!=0) {
            workersNames= new String[workerList.size()];
            for (int i = 0; i < workerList.size(); i++) {
                workersNames[i] = workerList.get(i).getFirstName() + " " + workerList.get(i).getLastName();
            }
        } else {workersNames= new String[1];
            workersNames[0]="---";}
        workerView.setMinValue(0);
        workerView.setDisplayedValues(workersNames);
        if (workerList.size()>1) {
            workerView.setMaxValue(workersNames.length-1);
        } else {
            workerView.setMaxValue(0);
        }
    }
}
