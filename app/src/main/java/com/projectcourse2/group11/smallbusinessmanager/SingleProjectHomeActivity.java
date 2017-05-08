package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.TestProject;

public class SingleProjectHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private ListView listView;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String projectUID,projectName;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            projectUID=bundle.getString("projectUID");
            projectName=bundle.getString("projectName");
        }
        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //// TODO: 08/05/2017 get company(wait for company register to finish)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyProjects").child("company1");

        //// TODO: 09/05/2017 listview of tasks
       /* mAdapter = new FirebaseListAdapter<String>(
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                finish();
                startActivity(new Intent(SingleProjectHomeActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(SingleProjectHomeActivity.this, OrderCreation.class));
        }
    }

}
