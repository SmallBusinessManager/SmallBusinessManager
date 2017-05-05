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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ProjectCreatActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvStartDate;
    private TextView tvEndDate;

    private int _day,_month,_year;
    private static final int DIALOG_ID_START=0;
    private static final int DIALOG_ID_END=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Calendar calendar=Calendar.getInstance();
        _year=calendar.get(Calendar.YEAR);
        _month=calendar.get(Calendar.MONTH);
        _day=calendar.get(Calendar.DAY_OF_MONTH);

        tvStartDate=(TextView)findViewById(R.id.tvStartDate);
        tvEndDate=(TextView)findViewById(R.id.tvEndDate);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==tvStartDate){
            showDialog(DIALOG_ID_START);
        }
        if (v==tvEndDate){
            showDialog(DIALOG_ID_END);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id==DIALOG_ID_START){
            return new DatePickerDialog(this,datePickerListener1,_year,_month,_day);
        }
        if (id==DIALOG_ID_END){
            return new DatePickerDialog(this,datePickerListener2,_year,_month,_day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener1=new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth){
            _year =year;
            _month=monthOfYear;
            _day=dayOfMonth;
            tvStartDate.setText(_day+"/"+_month+"/"+_year);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2=new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth){
            _year =year;
            _month=monthOfYear;
            _day=dayOfMonth;
            tvEndDate.setText(_day+"/"+_month+"/"+_year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
                finish();
                startActivity(new Intent(ProjectCreatActivity.this,ProjectActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
