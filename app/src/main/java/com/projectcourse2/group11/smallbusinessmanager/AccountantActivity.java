package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;

public class AccountantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button button, button2, button3, button4, button5, button6, button7;

    private ListView listView, listView2, listView3, listView4;
    private EditText editText, editText2, editText3, editText4;
    private ListAdapter mAdapter, mAdapter2, mAdapter3;
    private ArrayAdapter mAdapter4;
    private HashMap<String, String> salaryList;

    private Person user;
    private String companyID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountant_main);
        listView = (ListView) findViewById(R.id.listViewP);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView3 = (ListView) findViewById(R.id.listView3);
        listView4 = (ListView) findViewById(R.id.listView4);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        user = (Person) getIntent().getSerializableExtra("USER");
        companyID = getIntent().getStringExtra("COMPANY_ID");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        // list workers
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyEmployees").child(companyID);
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
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID);
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
        final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("expenses").child(companyID);
        mAdapter3 = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref3) {
            @Override
            protected String parseSnapshot(DataSnapshot snapshot) {
                return snapshot.child("description").getValue(String.class) + " " + snapshot.child("amount").getValue(Integer.class);
            }

            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        // Approve Expenses
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

        // Search for an Employee to return its Salary
        button5.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                final ValueEventListener listener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        salaryList = new HashMap<>();
                        final String editTextString = editText2.getText().toString();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().contains(editTextString)) {
                                salaryList.put(ds.child("salary").getValue().toString(), ds.getKey());
                            }
                        }
                        mAdapter4 = new ArrayAdapter<>(AccountantActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(salaryList.keySet()));

                        listView4.setAdapter(mAdapter4);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AccountantActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                    }
                };
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("employeeSalary").addValueEventListener(listener);

            }
        });

        // Set Salary
        final DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference().child("employeeSalary");

        button6.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String editTextString = editText3.getText().toString();
                int editTextString2 = Integer.parseInt(editText4.getText().toString());
                ref5.child(editTextString).child("salary").setValue(editTextString2);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AccountantActivity.this, AddExpenseActivity.class).putExtra("COMPANY_ID", companyID));
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            startActivity(new Intent(AccountantActivity.this, LoginActivity.class));
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
            finish();
            startActivity(new Intent(AccountantActivity.this, InvoiceActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_account) {
            finish();
            startActivity(new Intent(AccountantActivity.this, AccountActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_company) {
            finish();
            startActivity(new Intent(AccountantActivity.this, CompanyActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_project) {
            finish();
            startActivity(new Intent(AccountantActivity.this, ProjectActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        } else if (id == R.id.nav_order) {
            finish();
            startActivity(new Intent(AccountantActivity.this, SingleProjectHomeActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
