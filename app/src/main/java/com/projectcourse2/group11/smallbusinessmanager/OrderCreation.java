package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
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
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.Status;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ivana on 5/2/2017.
 * Creating the order, writing it to the fireBase and reading workers from it.
 */

public class OrderCreation extends AppCompatActivity implements View.OnClickListener{
    private Button buttonOK;
    private Button buttonCancel;
    private List<Worker> workerList;
    private NumberPicker workerView;
    private EditText descriptionView;
    private Worker selectedWorker;
    private TextView tvStartDateIn;
    private DatabaseReference ref;
    private String company,UID,projectID,orderID,workerSSN,description;
    private ProgressDialog progressDialog;
    private ValueEventListener listener,orderListener;
    private Person user;
    private int _day, _month, _year;
    private Date startDate;
    private static final int DIALOG_ID_START = 0;
    private Project project;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        company = getIntent().getStringExtra("COMPANY_ID");
        if (getIntent().getSerializableExtra("PROJECT")!=null){
        project=(Project)getIntent().getSerializableExtra("PROJECT");
        projectID=project.getId();
        }else {
            projectID=getIntent().getStringExtra("PROJECT_ID");
        }

        user = (Person) getIntent().getSerializableExtra("USER");


        //Reading workOrder from database

        ref = FirebaseDatabase.getInstance().getReference();
        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Status status=null;
                try {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(orderID)) {
                        description =ds.child("description").getValue(String.class);
                        workerSSN = ds.child("workerSSN").getValue(String.class);
                        descriptionView.setText(description);
                        String stDate = ds.child("startDate").getValue(String.class);
                        if (stDate!=null) {
                            tvStartDateIn.setText("Start Date:" +stDate);
                        } else {
                            tvStartDateIn.setText("Not Started");
                        }
                        status= ds.child("status").getValue(Status.class);

                        break;
                    }
                }
                workerList = new ArrayList<>();
                    addListener();
                    if (user.getPosition().equals(Position.WORKER)){
                        descriptionView.setEnabled(false);
                        workerView.setEnabled(false);
                        if (status.equals(Status.NOT_STARTED)){
                            buttonOK.setText(R.string.Start);
                        }else if (status.equals(Status.STARTED)){
                            buttonOK.setText(R.string.Mark_as_finished);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(OrderCreation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    try {
                        String ssn, email, firstName, lastName, phoneNumber;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UID = ds.getKey();
                            email = ds.child("email").getValue(String.class);
                            firstName = ds.child("firstName").getValue(String.class);
                            lastName = ds.child("lastName").getValue(String.class);
                            phoneNumber = ds.child("phoneNumber").getValue(String.class);
                            Position pos = ds.child("position").getValue(Position.class);
                            ssn = ds.child("ssn").getValue(String.class);


                            if (pos.equals(Position.WORKER)) {
                                Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email, UID);
                                if (workerSSN.equals(ssn)){
                                    selectedWorker = w;
                                    if (user.getPosition().equals(Position.WORKER)) {
                                        if ((!selectedWorker.getSsn().equals(user.getSsn()))) {
                                            buttonOK.setEnabled(false);
                                        } else {
                                            workerView.setEnabled(false);
                                        }
                                    }
                                }
                                workerList.add(w);
                            }
                        }

                        populateList();
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        Toast.makeText(OrderCreation.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(OrderCreation.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            };






        //preparing UI
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrder);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //Get new reference to the database, the previous was in /workers/
        ref = FirebaseDatabase.getInstance().getReference();

        buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        descriptionView = (EditText) findViewById(R.id.orderDescription);
        workerView = (NumberPicker) findViewById(R.id.workerPicker);
        workerView.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        tvStartDateIn = (TextView) findViewById(R.id.startDateIn);

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
                Intent MainIntent = new Intent(OrderCreation.this, MainActivity.class).putExtra("COMPANY_ID",company).putExtra("USER",user);
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
                    startActivity(new Intent(OrderCreation.this, MainActivity.class).putExtra("COMPANY_ID",company).putExtra("USER",user));
                    progressDialog.dismiss();
                }
            }
        });

        //it takes time to load and populate number picker, lets just say its loading
        String[] tmp = {"Loading"};

        workerView.setDisplayedValues(tmp);
        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);

        if (getIntent().hasExtra("ORDER_ID")){
            orderID=getIntent().getStringExtra("ORDER_ID");
            ref.child("companyWorkOrders").child(company).addValueEventListener(orderListener);
        }else {
//            if (user.getPosition().equals(Position.WORKER)) {
                workerSSN = user.getSsn();
//            } else {
//                workerSSN = "";
//            }
            ref.child("/companyEmployees/").child(company).addValueEventListener(listener);
            tvStartDateIn.setOnClickListener(this);
        }

    }

    /**
     *  This part right here is for writing to the database
     *  Some values are dummy values
     *  Returns TRUE if success and false if exception is thrown
     */
    private boolean createOrder() {
        String description = descriptionView.getText().toString().trim();
        ref = FirebaseDatabase.getInstance().getReference();
        Order order;

        try {
            if (orderID!=null) {
                order = new Order(orderID,description, selectedWorker, projectID);
            }else {
                order = new Order(description,selectedWorker,projectID);
            }
            if (buttonOK.getText().equals("Start")) {
                order.startOrder();
            } else if (buttonOK.getText().equals("Mark as Finished")) {
                order.markAsFinished();
            }
//                if(orderID!=null) {
//                    ref.child("companyWorkOrders").child(company).child(orderID).setValue(order);
//                }else {
                    ref.child("/companyWorkOrders/" + company + "/").updateChildren(order.toHashMap());
//                }
                return true;
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
            workersNames[0]=user.getFirstName()+" "+user.getLastName();
            selectedWorker=new Worker(user.getSsn(),user.getFirstName(),user.getLastName(),user.getPhoneNumber(),user.getEmail(),FirebaseAuth.getInstance().getCurrentUser().getUid());
            workerList.add(selectedWorker);
        }
        workerView.setMinValue(0);
        workerView.setDisplayedValues(workersNames);
        if (workerList.size()>1) {
            workerView.setMaxValue(workersNames.length-1);
            workerView.setValue(workerList.indexOf(selectedWorker));
        } else {
            workerView.setMaxValue(0);
        }
    }

    private void addListener(){
        ref.child("/companyEmployees/").child(company).addValueEventListener(listener);
        ref.removeEventListener(orderListener);
    }
    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            startDate = new Date(_day, _month, _year);
            tvStartDateIn.setText("Start Date:"+startDate.toString());

        }
    };
    @Override
    public void onClick(View v) {
        if (v == tvStartDateIn) {
            showDialog(DIALOG_ID_START);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_START) {
            return new DatePickerDialog(this, datePickerListener1, _year, _month, _day);
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
//                    progressDialog.show();
//                    if (selectedWorker == null) {
//                        selectedWorker = workerList.get(0);
//                    }
//
//                    if (createOrder()) {
//                        finish();
//                        ref.child("/companyEmployees/").removeEventListener(listener);
                        startActivity(new Intent(OrderCreation.this, MainActivity.class).putExtra("COMPANY_ID",company).putExtra("USER",user));
                        progressDialog.dismiss();
//                    }
                    break;
            }
        } catch (Exception e){
            //
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (project!=null) {
            finish();
            startActivity(new Intent(OrderCreation.this, SingleProjectHomeActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", company).putExtra("PROJECT",project));
        }else {
            finish();
            startActivity(new Intent(OrderCreation.this, MainActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", company));
        }
    }

}
