package com.projectcourse2.group11.smallbusinessmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.util.Calendar;

public class ProjectCreatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etProjectName;
    private EditText etProjectDescription;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private Person user;
    private Project project;
    private Button saveBtn;

    private int _day, _month, _year;
    private static final int DIALOG_ID_START = 0;
    private static final int DIALOG_ID_END = 1;

    private Date startDate, endDate;
    private String company, projectUID;

    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        saveBtn = (Button) findViewById(R.id.saveButton);

        startDate = new Date(Calendar.getInstance().DAY_OF_MONTH, Calendar.getInstance().MONTH, Calendar.getInstance().YEAR);
        endDate = new Date(Calendar.getInstance().DAY_OF_MONTH, Calendar.getInstance().MONTH, Calendar.getInstance().YEAR);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        user = (Person) getIntent().getSerializableExtra("USER");
        company = getIntent().getStringExtra("COMPANY_ID");
        if (getIntent().getSerializableExtra("PROJECT") != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");
        }

        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);

        etProjectName = (EditText) findViewById(R.id.etTaskTitle);
        etProjectDescription = (EditText) findViewById(R.id.etTaskDescription);
        tvStartDate = (TextView) findViewById(R.id.tvProjectStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvProjectEndDate);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


        if (getIntent().getSerializableExtra("PROJECT") != null) {
            projectUID = project.getId();
            String projectName = project.getName();
            this.setTitle("Edit " + projectName);

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    project.setStartDate(dataSnapshot.child("startDate").getValue(Date.class));
                    project.setDueDate(dataSnapshot.child("dueDate").getValue(Date.class));
                    project.setManager(dataSnapshot.child("manager").getValue(String.class));

                    etProjectName.setText(project.getName());
                    etProjectDescription.setText(project.getDescription());
                    if (project.getStartDate() != null) {
                        tvStartDate.setText("Start Date:                          " + project.getStartDate().toString());
                    }
                    if (project.getDueDate() != null) {
                        tvEndDate.setText("End Date:                            " + project.getDueDate().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(projectUID).addValueEventListener(listener);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvStartDate) {
            showDialog(DIALOG_ID_START);
        }
        if (v == tvEndDate) {
            showDialog(DIALOG_ID_END);
        }
        if (v == saveBtn) {
            if (project != null) {
                saveEditProject();
            } else {
                saveToDatabase();
            }
            finish();
            startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", company));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_START) {
            return new DatePickerDialog(this, datePickerListener1, _year, _month, _day);
        }
        if (id == DIALOG_ID_END) {
            return new DatePickerDialog(this, datePickerListener2, _year, _month, _day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            tvStartDate.setText("Start Date:                          " + _day + "/" + _month + "/" + _year);
            startDate = new Date(_day, _month, _year);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            tvEndDate.setText("End Date:                            " + _day + "/" + _month + "/" + _year);
            endDate = new Date(_day, _month, _year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                finish();
                startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", company));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class));
    }

    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company);
        String projectName = etProjectName.getText().toString();
        String projectDescription = etProjectDescription.getText().toString();

        //get UID and store object under it
        String key = databaseReference.push().getKey();
        Project project = new Project(key, projectName, projectDescription, FirebaseAuth.getInstance().getCurrentUser().getUid(), startDate, endDate);

        //todo get company
        databaseReference.updateChildren(project.toHashMap());

        //write project key to projectOrders
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("projectOrders");
//        ref.child(key).setValue("tobe overwritten");
    }

    private void saveEditProject() {
        FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(projectUID).removeEventListener(listener);
        project.setName(etProjectName.getText().toString());
        project.setDescription(etProjectDescription.getText().toString());
        if (startDate != null) {
            project.setStartDate(startDate);
        }
        if (endDate != null) {
            project.setDueDate(endDate);
        }
        FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(project.getId()).setValue(project);
    }
}
