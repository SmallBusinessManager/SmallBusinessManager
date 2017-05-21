package com.projectcourse2.group11.smallbusinessmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        saveBtn = (Button) findViewById(R.id.saveButton);

        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(_day, _month + 1, _year);
        endDate = startDate;

        if (getIntent() != null) {
            user = (Person) getIntent().getSerializableExtra("USER");
            company = getIntent().getStringExtra("COMPANY_ID");
        }
        if (getIntent().getSerializableExtra("PROJECT") != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");

        }

        etProjectName = (EditText) findViewById(R.id.etTaskTitle);
        etProjectDescription = (EditText) findViewById(R.id.etTaskDescription);
        tvStartDate = (TextView) findViewById(R.id.tvProjectStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvProjectEndDate);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


        if (getIntent().getSerializableExtra("PROJECT") != null) {
            projectUID = project.getId();
            final String projectName = project.getName();
            this.setTitle("Edit " + projectName);

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    project.setStartDate(dataSnapshot.child("startDate").getValue(Date.class));
                    project.setDueDate(dataSnapshot.child("dueDate").getValue(Date.class));
                    project.setManager(dataSnapshot.child("manager").getValue(String.class));

                    etProjectName.setText(projectName);
                    etProjectDescription.setText(project.getDescription());
                    tvStartDate.setText("Start Date:                          " + project.getStartDate().toString());
                    tvEndDate.setText("End Date:                            " + project.getDueDate().toString());

                    if (user.getPosition().equals(Position.WORKER) && (!project.getManager().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                        etProjectName.setEnabled(false);
                        etProjectDescription.setEnabled(false);
                        tvEndDate.setEnabled(false);
                        tvStartDate.setEnabled(false);
                        saveBtn.setText(R.string.ok);
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
                if (!TextUtils.isEmpty(etProjectName.getText())) {
                    saveToDatabase();
                } else {
                    Toast.makeText(this, "Enter project name at least", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                finish();
                startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", company));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDatabase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company);
        String projectName = etProjectName.getText().toString();
        String projectDescription = etProjectDescription.getText().toString();

        String key = databaseReference.push().getKey();
        project = new Project(key, projectName, projectDescription, FirebaseAuth.getInstance().getCurrentUser().getUid(), startDate, endDate);
        databaseReference.updateChildren(project.toHashMap());

    }

    private void saveEditProject() {
        if (user.getPosition().equals(Position.WORKER) && (!project.getManager().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(projectUID).removeEventListener(listener);
        project.setName(etProjectName.getText().toString());
        project.setDescription(etProjectDescription.getText().toString());
        project.setStartDate(startDate);
        project.setDueDate(endDate);

        FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(project.getId()).setValue(project);
    }
}