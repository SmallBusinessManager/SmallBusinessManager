package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.AccountActivity;
import com.projectcourse2.group11.smallbusinessmanager.OpeningActivity;
import com.projectcourse2.group11.smallbusinessmanager.R;
import com.projectcourse2.group11.smallbusinessmanager.model.Address;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Order;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Bjarni on 02/05/2017.
 */

public class CreateOrderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_order, container, false);
    }
    private Button buttonOK;
    private Button buttonCancel;
    private List<Worker> workerList = new ArrayList<>();
    private NumberPicker workerView;
    private EditText descriptionView;
    private Worker selectedWorker;
    private DatabaseReference ref;
 //   android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();

    public void oncCreate(Bundle savedInstanceState) {

        //Hardcoded for testing purposes
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
                    ssn=ds.child("ssn").getValue(String.class);
                    Address addr = ds.child("address").getValue(Address.class);
                    Worker w = new Worker(ssn, firstName, lastName, phoneNumber, email, addr);
                    workerList.add(w);
                    descriptionView.append(email+"\n");
//                    Worker worker = ds.getValue(Worker.class);
//                    descriptionView.append(ds.getValue(Worker.class).toString());
                }
                populateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
//        workerList.add(new Worker("197210102312","Erdogan","Tayyip","911","dick_Tator@yomama.org",new Address("street","city","12345","Country")));
//        workerList.add(new Manager("197210103232","Phat","American","911","dat_Wall@trump.org",new Address("Mystreet","Mycity","0012345","MyCountry")));

        getActivity().setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.new_order);

        ref = FirebaseDatabase.getInstance().getReference();

        buttonOK = (Button) getActivity().findViewById(R.id.buttonOK);
        buttonCancel = (Button) getActivity().findViewById(R.id.buttonCancel);
        descriptionView = (EditText) getActivity().findViewById(R.id.orderDescription);
        workerView = (NumberPicker) getActivity().findViewById(R.id.workerPicker);
        workerView.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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
                getActivity().finish();
                Intent MainIntent = new Intent(getActivity(), OpeningActivity.class);
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
                getActivity().finish();
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });
        String[] tmp = {"Loading"};
        workerView.setDisplayedValues(tmp);
    }


    //just adds the address to the firebase
    private void createOrder() {
        String description = descriptionView.getText().toString().trim();
        Order order = new Order(description, selectedWorker, new Project( workerList.get(1), new Date(21, 12, 2017), new Date(22, 12, 2017)));
//        ref.child("/worker/" + workerList.get(1).getSSN() + "/").setValue(workerList.get(1));
//        ref.child("/worker/" + workerList.get(0).getSSN() + "/").setValue(workerList.get(0));
        ref.updateChildren(order.toHashMap());
        System.out.println("create order terminated");


    }
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
