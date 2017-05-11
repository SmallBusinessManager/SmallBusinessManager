package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.OpeningActivity;
import com.projectcourse2.group11.smallbusinessmanager.ProjectActivity;
import com.projectcourse2.group11.smallbusinessmanager.R;
import com.projectcourse2.group11.smallbusinessmanager.TaskCreateActivity;
import com.projectcourse2.group11.smallbusinessmanager.model.Date;
import com.projectcourse2.group11.smallbusinessmanager.model.Task;

import java.util.Calendar;

import static android.R.attr.id;

/**
 * Created by Bjarni on 11/05/2017.
 */

public class CreateTaskFragment extends DialogFragment implements View.OnClickListener {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);
        View view = inflater.inflate(R.layout.activity_task_create, null);
        etTaskName = (EditText) view.findViewById(R.id.etTaskTitle);
        etTaskDescription = (EditText) view.findViewById(R.id.etTaskDescription);
        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        tvAssignedTo = (TextView) view.findViewById(R.id.tvAssignedTo);
        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        onCreateDialog(1);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        if (v == tvStartDate) {
            getActivity().showDialog(DIALOG_ID_START);
        }
        if (v == tvEndDate) {
            getActivity().showDialog(DIALOG_ID_END);
        }
    }
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_START) {
            return new DatePickerDialog(getActivity(), datePickerListener1, _year, _month, _day);
        }
        if (id == DIALOG_ID_END) {
            return new DatePickerDialog(getActivity(), datePickerListener2, _year, _month, _day);
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
   /* @Override
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
    }*/

    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String tasktName = etTaskName.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();
        Task task = new Task(tasktName, taskDescription, startDate, endDate);
        //get UID and store object under it
        String key = databaseReference.push().getKey();
        databaseReference.child("project").child("task").child(key).setValue(task);
    }
}