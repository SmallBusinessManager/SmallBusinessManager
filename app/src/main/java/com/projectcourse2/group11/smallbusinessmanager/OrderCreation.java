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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Reading all worker from database and sorting by position
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("/worker/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ssn,email,firstName,lastName,phoneNumber;
                    email=ds.child("email").getValue(String.class);
                    firstName=ds.child("firstName").getValue(String.class);
                    lastName=ds.child("lastName").getValue(String.class);
                    phoneNumber=ds.child("phoneNumber").getValue(String.class);
                    Position pos = ds.child("position").getValue(Position.class);
                    ssn=ds.getKey();

                    //separating managers and workers
                    if (pos.equals(Position.WORKER)) {
                        Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email);
                        workerList.add(w);
                    }else if (pos.equals(Position.MANAGER)){
                        Manager man = new Manager(ssn,firstName,lastName,phoneNumber,email);
                        managerList.add(man);
                    }
                }
                populateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String[] err = {"Error while loading"};
                workerView.setDisplayedValues(err);
            }
        });
//        workerList.add(new Worker("197210102312","Erdogan","Tayyip","911","dick_Tator@yomama.org"));
//        workerList.get(0).setAddress(new Address("street","city","12345","Country"));
//        managerList.add(new Manager("197210103232","Phat","American","911","dat_Wall@trump.org"));
//        managerList.get(0).setAddress(new Address("Mystreet","Mycity","0012345","MyCountry"));

        //preparing UI
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order);

        //Get new refence to the databse, the previuous was in /workers/
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
     *  Some lines are commented out but it was used to add dummy values/objects to the database
     */
    private void createOrder() {
        String description = descriptionView.getText().toString().trim();
//        Order order = new Order(description, selectedWorker, new Project( managerList.get(0), new Date(21, 12, 2017), new Date(22, 12, 2017)));
//        ref.child("/worker/" + workerList.get(1).getSSN() + "/").setValue(workerList.get(1));
//        ref.child("/worker/" + workerList.get(0).getSSN() + "/").setValue(workerList.get(0));
//        ref.updateChildren(workerList.get(0).toHashMap());
//        ref.updateChildren(managerList.get(0).toHashMap());
//        ref.updateChildren(order.toHashMap());
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
            workersNames[0]="-----";}
        workerView.setMinValue(0);
        workerView.setDisplayedValues(workersNames);
        if (workerList.size()>1) {
            workerView.setMaxValue(workersNames.length-1);
        } else {
            workerView.setMaxValue(0);
        }
    }
}
