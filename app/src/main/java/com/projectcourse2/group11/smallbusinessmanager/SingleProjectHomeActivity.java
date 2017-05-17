package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Order;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SingleProjectHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    //private ListView listView;
    // private ListAdapter mAdapter;

    private String projectUID;
    private String projectName;
    private DatabaseReference ref;
    private String companyID;
    private ListAdapter mAdapter;
    private ListView listView;
    private ProgressDialog progressDialog;
    private HashMap<String, String> orderList = new HashMap<>();
    private Person user;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading orders");
        progressDialog.show();

        listView = (ListView) findViewById(R.id.listView);


            project=(Project) getIntent().getSerializableExtra("PROJECT");
            projectUID = project.getId();
            projectName = project.getName();
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person)getIntent().getSerializableExtra("USER");
            this.setTitle(projectName);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        //// TODO: 08/05/2017 get company(wait for company register to finish)
        ref = FirebaseDatabase.getInstance().getReference();

/*
        //// TODO: 09/05/2017 listview of tasks
         //listView = (ListView) findViewById(R.id.listView);
        mAdapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot) {
                return snapshot.child("name").getValue(String.class);
            }

            @Override
            protected void populateView(View v, String model, final int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
                Log.d("TAG",model);
            }

        };
        listView.setAdapter(mAdapter);*/

        final ValueEventListener listener  = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.child("projectID").getValue(String.class).equals(projectUID)){
                        orderList.put(ds.child("description").getValue(String.class),ds.getKey());
                    }
                }
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SingleProjectHomeActivity.this,android.R.layout.simple_list_item_single_choice,new ArrayList<>(orderList.keySet()));
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setAdapter(myAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SingleProjectHomeActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
            }
        };
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("companyWorkOrders").child(companyID).addValueEventListener(listener);
        /**
         * @deprecated  !!!No longer needed
         * There is no filtering, it creates a row for each order in the database even if its not displayed

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID);
        mAdapter = new FirebaseListAdapter<Order>(
                SingleProjectHomeActivity.this,
                Order.class,
                android.R.layout.simple_list_item_single_choice,
                ref) {
            @Override
            protected void populateView(View v, Order model, int position) {
                if(model.getProjectID().equals(projectUID)) {
                    TextView textView = (TextView) v.findViewById(android.R.id.text1);
                    textView.setText(model.getDescription());
                } else {

                }
            }
        };

        progressDialog.dismiss();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(mAdapter);
        */
        listView.setOnItemClickListener(new DoubleClickListener() {
            String selectedOrderId;
            @Override
            protected void onSingleClick(final AdapterView<?> parent, final View v, final int position, long id) {
                selectedOrderId = orderList.get(parent.getItemAtPosition(position));

                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.nav_delete_project) {
                            FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID).child(selectedOrderId).removeValue();
                            ref.child(selectedOrderId).removeValue();
                        }
                        return true;
                    }
                });
            }

            @Override
            protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedOrderId = orderList.get(parent.getItemAtPosition(position));
                Intent intent = new Intent(SingleProjectHomeActivity.this, OrderCreation.class);
                intent.putExtra("ORDER_ID", selectedOrderId);
                intent.putExtra("COMPANY_ID", companyID);
                intent.putExtra("PROJECT",project);
                intent.putExtra("USER",user);
                finish();
                startActivity(intent);
            }
        });
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_single_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:

                    finish();
                    startActivity(new Intent(SingleProjectHomeActivity.this, ProjectActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
                    break;
                case R.id.nav_edit_project:
                    Intent intent = new Intent(SingleProjectHomeActivity.this, ProjectCreatActivity.class);
                    intent.putExtra("PROJECT",project);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("USER", user);
                    finish();
                    startActivity(intent);
            }
        } catch (Exception e){
            //
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(SingleProjectHomeActivity.this, OrderCreation.class).putExtra("COMPANY_ID",companyID).putExtra("USER",user).putExtra("PROJECT",project));
        }
    }

}
