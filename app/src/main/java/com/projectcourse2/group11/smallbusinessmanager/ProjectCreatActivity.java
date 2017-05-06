package com.projectcourse2.group11.smallbusinessmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;
import com.projectcourse2.group11.smallbusinessmanager.model.TestProject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProjectCreatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etProjectName;
    private EditText etProjectDescription;
    private TextView tvStartDate;
    private TextView tvEndDate;

    private int _day, _month, _year;
    private static final int DIALOG_ID_START = 0;
    private static final int DIALOG_ID_END = 1;

    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);


        etProjectName = (EditText) findViewById(R.id.etProjectTitle);
        etProjectDescription = (EditText) findViewById(R.id.etProjectDescription);
        tvStartDate = (TextView) findViewById(R.id.tvProjectStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvProjectEndDate);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
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
            tvStartDate.setText(_day + "/" + _month + "/" + _year);
            startDate = new Date(_day, _month, _year);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            tvEndDate.setText(_day + "/" + _month + "/" + _year);
            endDate = new Date(_day, _month, _year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                saveToDatebase();
                finish();
                startActivity(new Intent(ProjectCreatActivity.this, ProjectActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDatebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String projectName = etProjectName.getText().toString();
        String projectDescription = etProjectDescription.getText().toString();

        TestProject testProject = new TestProject(projectName, projectDescription, startDate, endDate);
        //get UID and store object under it
        String key = databaseReference.push().getKey();
        databaseReference.updateChildren(testProject.toHashMap(key));


    }
}
