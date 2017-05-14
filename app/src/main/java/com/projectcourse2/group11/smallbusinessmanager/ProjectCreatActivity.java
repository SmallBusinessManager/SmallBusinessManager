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

    private int _day, _month, _year;
    private static final int DIALOG_ID_START = 0;
    private static final int DIALOG_ID_END = 1;

    private Date startDate, endDate;
    private String projectUID,company;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        user = (Person)getIntent().getSerializableExtra("USER");
        company = getIntent().getStringExtra("COMPANY_ID");

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

        if (getIntent().getStringExtra("projectUID")!=null) {
            projectUID = bundle.getString("projectUID");
            final String projectName = bundle.getString("projectName");
            this.setTitle("Edit "+projectName);

            FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(projectUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Project project=dataSnapshot.getValue(Project.class);
                    etProjectName.setText(project.getName());
                    etProjectDescription.setText(project.getDescription());
                    tvStartDate.setText("Start Date:                          "+project.getStartDate().toString());
                    tvEndDate.setText("End Date:                            "+project.getDueDate().toString());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
            tvStartDate.setText("Start Date:                          "+_day + "/" + _month + "/" + _year);
            startDate = new Date(_day, _month, _year);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            tvEndDate.setText("End Date:                            "+_day + "/" + _month + "/" + _year);
            endDate = new Date(_day, _month, _year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                if (bundle!=null){
                    saveEditProject();
                }else {
                    saveToDatabase();
                }
                finish();
                startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class).putExtra("USER",user).putExtra("COMPANY_ID",company));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company);
        String projectName = etProjectName.getText().toString();
        String projectDescription = etProjectDescription.getText().toString();

        //get UID and store object under it
        String key = databaseReference.push().getKey();
        Project project = new Project(key,projectName, projectDescription, FirebaseAuth.getInstance().getCurrentUser().getUid(), startDate, endDate);

        //todo get company
        databaseReference.child(key).setValue(project);

        //write project key to projectOrders
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("projectOrders");
        ref.child(key).setValue("tobe overwritten");
    }
    private void saveEditProject(){
        String projectName = etProjectName.getText().toString();
        String projectDescription = etProjectDescription.getText().toString();
        Project project = new Project(projectUID,projectName, projectDescription, startDate, endDate);

        FirebaseDatabase.getInstance().getReference().child("companyProjects").child(company).child(projectUID).setValue(project);
    }
}
