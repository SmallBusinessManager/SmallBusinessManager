package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView=(ListView)findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("project").child("test");
        FirebaseListAdapter<String> mAdapter=new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView=(TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        listView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                finish();
                startActivity(new Intent(ProjectActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(ProjectActivity.this, ProjectCreatActivity.class));
        }
    }
}
