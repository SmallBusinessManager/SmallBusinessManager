package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Manager;
import com.projectcourse2.group11.smallbusinessmanager.model.Order;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ivana on 5/2/2017.
 * Creating the order, writing it to the fireBase and reading workers from it.
 */

public class OrderCreation extends Activity  {
    private Button buttonOK;
    private Button buttonCancel;
    private List<Worker> workerList;
    private NumberPicker workerView;
    private EditText descriptionView;
    private Worker selectedWorker;
    private EditText startDateIn;
    private DatabaseReference ref;
    private String company,UID,projectID,orderID,workerSSN,description;
    private ProgressDialog progressDialog;
    private ValueEventListener listener,orderListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        company = getIntent().getStringExtra("COMPANY_ID");
        projectID=getIntent().getStringExtra("projectUID");


        //Reading workOrder from databse

        ref = FirebaseDatabase.getInstance().getReference();
        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(orderID)) {
                        description =ds.child("description").getValue(String.class);
                        workerSSN = ds.child("worker").getValue(String.class);
                        descriptionView.setText(description);
                        Toast.makeText(OrderCreation.this,workerSSN,Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                workerList = new ArrayList<>();
                    addListener();
                } catch (Exception e) {
                    Toast.makeText(OrderCreation.this, "order listener", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrderCreation.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        /**
         * Defining the listener
         */

        listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    workerList = new ArrayList<>();
 //                   try {
                        String ssn, email, firstName, lastName, phoneNumber;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UID = ds.getKey();
                            email = ds.child("email").getValue(String.class);
                            firstName = ds.child("firstName").getValue(String.class);
                            lastName = ds.child("lastName").getValue(String.class);
                            phoneNumber = ds.child("phoneNumber").getValue(String.class);
                            Position pos = ds.child("position").getValue(Position.class);
                            ssn = ds.child("SSN").getValue(String.class);


                            if (pos.equals(Position.WORKER)) {
                                Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email, UID);
                                if (workerSSN.equals(ssn)){
                                    selectedWorker = w;
                                }
                                workerList.add(w);
                            }
                        }
                        populateList();
                        selectedWorker = workerList.get(0);
                        progressDialog.dismiss();

//                    } catch (Exception e) {
//                        Toast.makeText(OrderCreation.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(OrderCreation.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };
        if (getIntent().hasExtra("ORDER_ID")){
            orderID=getIntent().getStringExtra("ORDER_ID");
            Toast.makeText(OrderCreation.this, orderID, Toast.LENGTH_SHORT).show();
            ref.child("companyWorkOrders").child(company).addValueEventListener(orderListener);
        }else {
            workerSSN="";
            ref.child("/companyEmployees/").child(company).addValueEventListener(listener);
        }




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
        startDateIn = (EditText) findViewById(R.id.startDateIn);


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
                ref.child("/companyEmployees/").removeEventListener(listener);
                Intent MainIntent = new Intent(OrderCreation.this, MainActivity.class).putExtra("COMPANY_ID",company);
                startActivity(MainIntent);
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (selectedWorker == null) {
                    selectedWorker = workerList.get(0);
                }

                if (createOrder()) {
                    finish();
                    ref.child("/companyEmployees/").removeEventListener(listener);
                    startActivity(new Intent(OrderCreation.this, MainActivity.class).putExtra("COMPANY_ID",company));
                    progressDialog.dismiss();
                }
            }
        });

        //it takes time to load and populate number picker, lets just say its loading
        String[] tmp = {"Loading"};
        workerView.setDisplayedValues(tmp);
    }

    /**
     *  This part right here is for writing to the database
     *  Some values are dummy values
     *  Returns TRUE if success and false if exception is thrown
     */
    private boolean createOrder() {
        String description = descriptionView.getText().toString().trim();
        ref = FirebaseDatabase.getInstance().getReference();
        try {
            if (!(startDateIn.getText().toString().trim().equalsIgnoreCase(""))) {
                String strDate = startDateIn.getText().toString().replace('-',' ').trim();
                descriptionView.setText(strDate.substring(8,10)+"\n"+strDate.substring(5,7)+"\n"+strDate.substring(0,4));
                Date sDate = new Date(Integer.parseInt(strDate.substring(8,10)),Integer.parseInt(strDate.substring(5,7)),Integer.parseInt(strDate.substring(0,4)));
                Order order = new Order(description, selectedWorker, projectID);
                order.startOrder(sDate);
                ref.child("/companyWorkOrders/" + company + "/").updateChildren(order.toHashMap());
                return true;
            } else {
                Date sDate = new Date(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.YEAR));
                Order order = new Order(description, selectedWorker, projectID);
                order.startOrder(sDate);
                ref.child("/companyWorkOrders/" + company + "/").updateChildren(order.toHashMap());
                return true;
            }
        } catch (Exception e){
            Toast.makeText(OrderCreation.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

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

    private void addListener(){
        ref.child("/companyEmployees/").child(company).addValueEventListener(listener);
        ref.removeEventListener(orderListener);
    }

}
