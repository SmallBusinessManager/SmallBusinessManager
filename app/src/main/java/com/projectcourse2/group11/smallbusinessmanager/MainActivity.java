package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
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
import android.widget.ViewFlipper;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ListAdapter mAdapter;
    private HashMap<String, String> orderList = new HashMap<>();
    private TextView emailHeader;
    private ViewFlipper viewFlipperMain;
    private Toolbar toolbar;

    private String companyID;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewFlipperMain = (ViewFlipper) findViewById(R.id.viewFlipperMain);
        viewFlipperMain.setFlipInterval(0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_schedule:
                        Toast.makeText(MainActivity.this, "schedule selected", Toast.LENGTH_SHORT).show();
                        viewFlipperMain.setDisplayedChild(0);
                        break;
                    case R.id.nav_local_activity:
                        Toast.makeText(MainActivity.this, "activity selected", Toast.LENGTH_SHORT).show();
                        viewFlipperMain.setDisplayedChild(1);
                        initializeView();
                        break;

                }

                return true;
            }

        });

        View headerView = navigationView.getHeaderView(0);
        emailHeader = (TextView) headerView.findViewById(R.id.emailHeader);
        emailHeader.setText(user.getEmail());
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"contact developer Danfeng for details",Toast.LENGTH_SHORT).show();
           // finish();
           // finishAffinity();
           // startActivity(new Intent(MainActivity.this, InvoiceActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_account) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, AccountActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_company) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, CompanyActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_project) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, ProjectActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        } else if (id == R.id.nav_order) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, SingleProjectHomeActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeView(){
        listView = (ListView) findViewById(R.id.MainListView);
        /**
         * If the logged in user is a worker or a team leader
         * load all the work orders that this worker has connected to it.
         */
        if (user.getPosition().equals(Position.WORKER) || user.getPosition().equals(Position.TEAM_LEADER)) {
            final ArrayList<String> projectIDs = new ArrayList<>();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("workerSSN").getValue(String.class).equals(user.getSsn())) {
                            orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                            projectIDs.add(ds.child("projectID").getValue(String.class));
                        }
                    }
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listView.setAdapter(myAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                }
            };
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("companyWorkOrders").child(companyID).addValueEventListener(listener);

            listView.setOnItemClickListener(new DoubleClickListener() {
                @Override
                protected void onSingleClick(AdapterView<?> parent, View v, int position, long id) {
                    final String order = orderList.get(parent.getItemAtPosition(position));

                    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.nav_delete_project) {
                                FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID).child(order).removeValue();
                                reference.child(order).removeValue();
                            }
                            return true;
                        }
                    });
                }

                @Override
                protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                    String order = orderList.get(parent.getItemAtPosition(position));
                    Intent intent = new Intent(MainActivity.this, OrderCreation.class);
                    intent.putExtra("ORDER_ID", order);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("PROJECT_ID", projectIDs.get(position));
                    intent.putExtra("USER", user);
                    finish();
                    finishAffinity();
                    startActivity(intent);
                }
            });


        } else if (user.getPosition().equals(Position.ACCOUNTANT)) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, AccountantActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        } else {
            /**
             *  if logged in user is not a worker or a team leader
             *  that is if the user is manager,
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
                    Log.e("hehe",model.getName());
                }
            };
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(mAdapter);

           /* listView.setOnItemClickListener(new DoubleClickListener() {
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
                    intent.putExtra("PROJECT", project);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("USER", user);
                    finish();
                    finishAffinity();
                    startActivity(intent);
                }
            });*/
        }
    }
}
