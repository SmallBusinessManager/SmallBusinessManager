package com.projectcourse2.group11.smallbusinessmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.projectcourse2.group11.smallbusinessmanager.model.Date;

import java.util.Calendar;

public class TaskCreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etTaskName;
    private EditText etTaskDescription;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvAssignedTo;
    private TextView tvPriority;

    private int _day, _month, _year;
    private static final int DIALOG_ID_START = 0;
    private static final int DIALOG_ID_END = 1;

    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);


        etTaskName = (EditText) findViewById(R.id.etTaskTitle);
        etTaskDescription = (EditText) findViewById(R.id.etTaskDescription);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvProjectEndDate);
        tvAssignedTo = (TextView) findViewById(R.id.tvAssignedTo);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
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
            startDate = new com.projectcourse2.group11.smallbusinessmanager.model.Date(_day, _month, _year);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            tvEndDate.setText(_day + "/" + _month + "/" + _year);
            endDate = new com.projectcourse2.group11.smallbusinessmanager.model.Date(_day, _month, _year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                saveToDatabase();
                finish();
                startActivity(new Intent(TaskCreateActivity.this, ProjectActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String tasktName = etTaskName.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();

        //com.projectcourse2.group11.smallbusinessmanager.model.TestProject testProject = new TestProject(projectName, projectDescription, startDate, endDate);
        //get UID and store object under it
        String key = databaseReference.push().getKey();
       // databaseReference.child("project").child(key).setValue(testProject);


    }
}
