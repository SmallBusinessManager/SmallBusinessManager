package com.projectcourse2.group11.smallbusinessmanager;

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
import com.projectcourse2.group11.smallbusinessmanager.model.TestProject;

public class SingleProjectHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    //private ListView listView;
    // private ListAdapter mAdapter;

    private String projectUID;
    private String projectName;
    private DatabaseReference ref;
    private String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            projectUID = bundle.getString("projectUID");
            projectName = bundle.getString("name");
            this.setTitle(projectName);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        //// TODO: 08/05/2017 get company(wait for company register to finish)
        ref = FirebaseDatabase.getInstance().getReference();

        /**
         * Getting company ID
         */
        companyID=getIntent().getStringExtra("COMPANY_ID");
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_single_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                startActivity(new Intent(SingleProjectHomeActivity.this, ProjectActivity.class).putExtra("COMPANY_ID",companyID));
                break;
            case R.id.nav_edit_project:
                Intent intent=new Intent(SingleProjectHomeActivity.this,ProjectCreatActivity.class);
                intent.putExtra("projectUID",projectUID);
                intent.putExtra("projectName",projectName);
                intent.putExtra("COMPANY_ID",companyID);
                finish();
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(SingleProjectHomeActivity.this, OrderCreation.class).putExtra("COMPANY_ID",companyID));
        }
    }

}
