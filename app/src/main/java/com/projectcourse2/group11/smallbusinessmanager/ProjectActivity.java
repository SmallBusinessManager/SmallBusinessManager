package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

public class ProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private ListView listView;
    private ListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private String companyID;
    private Person user;
    private Project selectedProject;

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
        progressDialog.setMessage("Loading projects");
        progressDialog.show();
        listView = (ListView) findViewById(R.id.listViewF);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (getIntent() != null) {
            user = (Person) getIntent().getSerializableExtra("USER");
            companyID = getIntent().getStringExtra("COMPANY_ID");
        }



        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID);
        mAdapter = new FirebaseListAdapter<Project>(
                ProjectActivity.this,
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
                selectedProject = project;
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.nav_delete_project) {
                            if (user.getPosition().equals(Position.WORKER)&&(!project.getManager().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                                Toast.makeText(ProjectActivity.this, "No.", Toast.LENGTH_SHORT).show();
                            }else {
                                FirebaseDatabase.getInstance().getReference().child("projectOrders").child(project.getId()).removeValue();
                                ref.child(project.getId()).removeValue();
                            }
                        }
                        if (item.getItemId()==R.id.nav_edit_project){
                            Intent i = new Intent(ProjectActivity.this, ProjectCreatActivity.class);
                            if (selectedProject!=null) {
                                i.putExtra("PROJECT", selectedProject);
                            }else if (mAdapter.getItem(0)!=null) {
                                selectedProject = (Project) mAdapter.getItem(0);
                                i.putExtra("PROJECT", selectedProject);
                            }else {
                                i.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01,01,2017), new Date(12,12,20017)));
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
                Project project = (Project) parent.getItemAtPosition(position);
                Intent intent = new Intent(ProjectActivity.this, SPChooseActivity.class).putExtra("PROJECT", project);
                intent.putExtra("COMPANY_ID", companyID);
                intent.putExtra("USER", user);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_project_home, menu);
        MenuItem item=menu.findItem(R.id.nav_search_project);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Query query = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID).orderByChild("name").startAt(newText);
                FirebaseListAdapter mnAdapter = new FirebaseListAdapter<Project>(
                        ProjectActivity.this,
                        Project.class,
                        android.R.layout.simple_list_item_single_choice,
                        query) {
                    @Override
                    protected void populateView(View v, Project model, int position) {
                        TextView textView = (TextView) v.findViewById(android.R.id.text1);
                        textView.setText(model.getName());
                    }
                };
                listView.setAdapter(mnAdapter);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(ProjectActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(ProjectActivity.this, MainActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
                break;
            case R.id.nav_reorder_project:
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID);
                FirebaseListAdapter mnAdapter = new FirebaseListAdapter<Project>(
                        ProjectActivity.this,
                        Project.class,
                        android.R.layout.simple_list_item_single_choice,
                        ref.orderByChild("name")) {
                    @Override
                    protected void populateView(View v, Project model, int position) {
                        TextView textView = (TextView) v.findViewById(android.R.id.text1);
                        textView.setText(model.getName());
                    }
                };
                listView.setAdapter(mnAdapter);
                break;
            case R.id.nav_edit_project:
//                Intent i = new Intent(ProjectActivity.this, ProjectCreatActivity.class);
//                if (selectedProject!=null) {
//                    i.putExtra("PROJECT", selectedProject);
//                }else if (mAdapter.getItem(0)!=null) {
//                    selectedProject = (Project) mAdapter.getItem(0);
//                    i.putExtra("PROJECT", selectedProject);
//                }else {
//                    i.putExtra("PROJECT", new Project("name", "description", "manager", new Date(01,01,2017), new Date(12,12,20017)));
//                }
//                i.putExtra("COMPANY_ID", companyID);
//                i.putExtra("USER", user);
//                finish();
//                startActivity(i);
                Toast.makeText(this, "You must select a project!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            finish();
            startActivity(new Intent(ProjectActivity.this, ProjectCreatActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        }
    }
}
