package com.projectcourse2.group11.smallbusinessmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Expenses;

import java.util.List;

public class AccountantActivity extends AppCompatActivity {
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private ListView listView;
    private ListView listView2;
    private ListView listView3;
    private EditText editText;
    private EditText editText2;
    private ListAdapter mAdapter;
    private ListAdapter mAdapter2;
    private ListAdapter mAdapter3;
    private ListAdapter mAdapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountant);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView3 = (ListView) findViewById(R.id.listView3);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);


        editText = (EditText) findViewById(R.id.editText);


        // list workers
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyEmployees").child("company1");
        mAdapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot) {
                return snapshot.child("firstName").getValue(String.class) + " " + snapshot.child("lastName").getValue(String.class);
            }

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listView.setAdapter(mAdapter);

            }
        });

        // view projects
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("companyProjects").child("company1");
        mAdapter2 = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref2) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot1) {
                return snapshot1.child("name").getValue(String.class);
            }

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                listView2.setAdapter(mAdapter2);
            }
        });

        // view expenses
        final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("expenses").child("company1");
        mAdapter3 = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref3) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot) {
                return snapshot.child("description").getValue(String.class) + " " + snapshot.child("amount").getValue(Float.class);
            }

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                listView3.setAdapter(mAdapter3);
            }
        });

        // approve expense

        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String editTextString = editText.getText().toString();

                ref3.child(editTextString).child("approved").setValue(true);
            }
        });
        final DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("employeeSalary");
        mAdapter4 = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref4) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot) {
                return snapshot.child("description").getValue(String.class) + " " + snapshot.child("amount").getValue(Float.class);
            }

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

    }
}
