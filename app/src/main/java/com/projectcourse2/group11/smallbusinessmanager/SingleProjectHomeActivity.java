package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleProjectHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    //private ListView listView;
    // private ListAdapter mAdapter;

    private String projectUID;
    private String projectName;
    private DatabaseReference ref;
    private String companyID;
    private ListView listView;
    private ProgressDialog progressDialog;
    private HashMap<String, String> orderList;
    private Person user;
    private Project project;
    private ArrayAdapter<String> myAdapter;
    private boolean sorted = false;
    private String selectedOrderId;

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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listViewF);

        if (getIntent().getSerializableExtra("PROJECT") != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");
            projectUID = project.getId();
            projectName = project.getName();
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
            this.setTitle(projectName);


            ref = FirebaseDatabase.getInstance().getReference();

            final ValueEventListener listener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    orderList = new HashMap<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("projectID").getValue(String.class).equals(projectUID)) {
                            orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                        }
                    }
                    myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
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

        } else {
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
            this.setTitle("All tasks");


            ref = FirebaseDatabase.getInstance().getReference();

            final ValueEventListener listener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    orderList = new HashMap<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                    }
                    myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
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
        }
        /**
         * @deprecated  !!!No longer needed
         * There is no filtering, it creates a row for each order in the database even if its not displayed

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID);
        mAdapter = new FirebaseListAdapter<Order>(
        SingleProjectHomeActivity.this,
        Order.class,
        android.R.layout.simple_list_item_single_choice,
        ref) {
        @Override protected void populateView(View v, Order model, int position) {
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
                        if (item.getItemId() == R.id.nav_edit_task) {
                            Intent i = new Intent(SingleProjectHomeActivity.this, OrderCreation.class);
                            if (selectedOrderId != null) {
                                i.putExtra("ORDER_ID", selectedOrderId);
                            } else if (listView.getItemAtPosition(0) != null) {
                                selectedOrderId = orderList.get(listView.getItemAtPosition(0));
                            }
                            if (project != null) {
                                i.putExtra("PROJECT", project);
                            } else {
                                i.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01, 01, 2017), new Date(12, 12, 20017)));
                            }
                            i.putExtra("COMPANY_ID", companyID);
                            i.putExtra("USER", user);
                            finish();
                            startActivity(i);
                        }
                        return true;
                    }
                });
            }

            @Override
            protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedOrder = orderList.get(parent.getItemAtPosition(position));
                Intent intent = new Intent(SingleProjectHomeActivity.this, OrderCreation.class);
                intent.putExtra("ORDER_ID", selectedOrder);
                intent.putExtra("COMPANY_ID", companyID);
                if (project != null) {
                    intent.putExtra("PROJECT", project);
                } else {
                    intent.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01, 01, 2017), new Date(12, 12, 20017)));

                }
                intent.putExtra("USER", user);
                finish();
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    if (project != null) {
                        Intent intent = new Intent(SingleProjectHomeActivity.this, SPChooseActivity.class).putExtra("PROJECT", project);
                        intent.putExtra("COMPANY_ID", companyID);
                        intent.putExtra("USER", user);
                        finish();
                        startActivity(intent);
                        break;
                    } else {
                        Intent intent = getIntent();
                        intent.setClass(SingleProjectHomeActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    }
                case R.id.nav_edit_task:
//                    Intent i = new Intent(SingleProjectHomeActivity.this, OrderCreation.class);
//                    if (selectedOrderId != null) {
//                        i.putExtra("ORDER_ID", selectedOrderId);
//                    } else if (listView.getItemAtPosition(0) != null) {
//                        selectedOrderId = orderList.get(listView.getItemAtPosition(0));
//                    }
//                    if (project != null) {
//                        i.putExtra("PROJECT", project);
//                    } else {
//                        i.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01, 01, 2017), new Date(12, 12, 20017)));
//                    }
//                    i.putExtra("COMPANY_ID", companyID);
//                    i.putExtra("USER", user);
//                    finish();
//                    startActivity(i);
                    Toast.makeText(this, "You must select a task.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_reorder_task:
                    final ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (projectUID != null) {
                                orderList = new HashMap<>();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.child("projectID").getValue(String.class).equals(projectUID)) {
                                        orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                                    }
                                }
                                myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                listView.setAdapter(myAdapter);
                                progressDialog.dismiss();
                            } else {
                                orderList = new HashMap<>();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                                }
                                myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                listView.setAdapter(myAdapter);
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SingleProjectHomeActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                        }
                    };
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    if (sorted) {
                        reference.child("companyWorkOrders").child(companyID).orderByChild("description").addValueEventListener(listener);
                        sorted = false;
                        break;
                    } else {
                        reference.child("companyWorkOrders").child(companyID).orderByChild("status").addValueEventListener(listener);
                        sorted = true;
                        break;
                    }

            }
        } catch (Exception e) {
            //
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Intent intent = new Intent(SingleProjectHomeActivity.this, OrderCreation.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user);
            if (project != null) {
                intent.putExtra("PROJECT", project);
            } else {
                intent.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01, 01, 2017), new Date(12, 12, 20017)));
            }
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (project != null) {
            Intent intent = getIntent();
            intent.setClass(SingleProjectHomeActivity.this, SPChooseActivity.class);
            finish();
            startActivity(intent);
        } else {
            Intent intent = getIntent();
            intent.setClass(SingleProjectHomeActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_single_project, menu);
        MenuItem item = menu.findItem(R.id.nav_search_task);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        orderList = new HashMap<>();
                        if (projectUID != null) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.child("projectID").getValue(String.class).equals(projectUID)) {
                                    orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                                }
                            }
                            myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            listView.setAdapter(myAdapter);
                            progressDialog.dismiss();
                        } else {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                            }
                            myAdapter = new ArrayAdapter<>(SingleProjectHomeActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            listView.setAdapter(myAdapter);
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SingleProjectHomeActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                    }
                };
                Query reference = FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID).orderByChild("description").startAt(newText);
                reference.addValueEventListener(listener);
                return false;
            }
        });
        return true;
    }
}
