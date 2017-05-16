package com.projectcourse2.group11.smallbusinessmanager;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.Worker;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String companyID;
    private ListView listView;
    private ListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private HashMap<String, String> orderList = new HashMap<>();
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading dashboard");
        progressDialog.show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        companyID= getIntent().getStringExtra("COMPANY_ID");
        user=(Person)getIntent().getSerializableExtra("USER");
        listView = (ListView) findViewById(R.id.MainListView);
        Toast.makeText(MainActivity.this,user.getPosition().toString(),Toast.LENGTH_SHORT);


        /**
         * If the logged in user is a worker or a team leader
         * load all the work orders that this worker has connected to it.
         */
        if (user.getPosition().equals(Position.WORKER)||user.getPosition().equals(Position.TEAM_LEADER)){
            ValueEventListener listener  = new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        if (ds.child("worker").getValue(String.class).equals(user.getSSN())){
                            orderList.put(ds.child("description").getValue(String.class),ds.getKey());
                        }
                    }
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_single_choice,new ArrayList<>(orderList.keySet()));
                    progressDialog.dismiss();
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listView.setAdapter(myAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                }
            };
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("companyWorkOrders").child(companyID).addValueEventListener(listener);





        } else if (user.getPosition().equals(Position.ACCOUNTANT)){
            startActivity(new Intent(MainActivity.this,AccountantActivity.class).putExtra("COMPANY_ID",companyID).putExtra("USER",user));
        } else {
            /**
             *  if logged in user is not a worker or a team leader
             *  that is if the user is accountant, manager,
             *  load all the projects for the company
             */
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID);
            mAdapter = new FirebaseListAdapter<Project>(
                    MainActivity.this,
                    Project.class,
                    android.R.layout.simple_list_item_single_choice,
                    ref) {
                @Override
                protected void populateView(View v, Project model, int position) {
                    TextView textView = (TextView) v.findViewById(android.R.id.text1);
                    textView.setText(model.getName());
                    progressDialog.dismiss();
                }
            };
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(mAdapter);

            progressDialog.dismiss();

            listView.setOnItemClickListener(new DoubleClickListener() {
                @Override
                protected void onSingleClick(AdapterView<?> parent, View v, int position, long id) {
                    final Project project = (Project) parent.getItemAtPosition(position);

                    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.nav_delete_project) {
                                FirebaseDatabase.getInstance().getReference().child("projectOrders").child(project.getId()).removeValue();
                                ref.child(project.getId()).removeValue();
                            }
                            return true;
                        }
                    });
                }

                @Override
                protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                    Project project = (Project) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, SingleProjectHomeActivity.class);
                    intent.putExtra("PROJECT",project);
                    intent.putExtra("COMPANY_ID",companyID);
                    intent.putExtra("USER",user);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_account) {
            finish();
            startActivity(new Intent(MainActivity.this, AccountActivity.class).putExtra("USER",user).putExtra("COMPANY_ID",companyID));
        } else if (id == R.id.nav_project) {
            finish();
            startActivity(new Intent(MainActivity.this,ProjectActivity.class).putExtra("COMPANY_ID" ,companyID).putExtra("USER",user));
        }else if (id==R.id.nav_order){
            finish();
            startActivity(new Intent(MainActivity.this, OrderCreation.class).putExtra("COMPANY_ID" ,companyID).putExtra("projectUID","TO BE OVERWRITTEN").putExtra("USER",user));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
