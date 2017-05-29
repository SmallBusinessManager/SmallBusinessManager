package com.projectcourse2.group11.smallbusinessmanager.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalendarViewActivity extends Activity implements OnClickListener {

    private Button selectedDayMonthYearButton;
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private TextView num_events_per_day;

    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    CheckBox checkBox;
    EditText et_startTime, et_endTime;
    TextView tv_showingTime;
    Button buttonSaveTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_calendar_view);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (Button) this.findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        et_startTime = (EditText) findViewById(R.id.et_startTime);
        et_endTime = (EditText) findViewById(R.id.et_endTime);
        tv_showingTime = (TextView) findViewById(R.id.tv_showingTime);
        buttonSaveTime = (Button) findViewById(R.id.buttonSaveTime);

        et_startTime.setVisibility(View.INVISIBLE);
        et_endTime.setVisibility(View.INVISIBLE);
        tv_showingTime.setVisibility(View.INVISIBLE);
        buttonSaveTime.setVisibility(View.INVISIBLE);
    }

    /**
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            setGridCellAdapterToDate(month, year);
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final int month, year;
        private final HashMap eventsPerMonthMap;
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<>();
            this.month = month;
            this.year = year;

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            printMonth(month, year);
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);

        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {

            int trailingSpaces;
            int daysInPrevMonth;
            int prevMonth;
            int prevYear;
            int nextMonth;
            int nextYear;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++daysInMonth;
            }

            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }

            for (int i = 1; i <= daysInMonth; i++) {
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }
        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap findNumberOfEventsPerMonth(int year, int month)
        {
            HashMap map = new HashMap<String, Integer>();
            // DateFormat dateFormatter2 = new DateFormat();
            //
            // String day = dateFormatter2.format("dd", dateCreated).toString();
            //
            // if (map.containsKey(day))
            // {
            // Integer val = (Integer) map.get(day) + 1;
            // map.put(day, val);
            // }
            // else
            // {
            // map.put(day, 1);
            // }
            return map;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
            }

            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            String[] day_color = list.get(position).split("-");

            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(Color.LTGRAY);
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(Color.WHITE);
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
            }

            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
            {
                if (eventsPerMonthMap.containsKey(theday))
                {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);

            return row;
        }

        @Override
        public void onClick(View view) {
            checkBox.setChecked(false);
            et_startTime.setVisibility(View.INVISIBLE);
            et_endTime.setVisibility(View.INVISIBLE);
            tv_showingTime.setVisibility(View.INVISIBLE);
            buttonSaveTime.setVisibility(View.INVISIBLE);
            final String date_month_year = (String) view.getTag();
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);
            final Button background = (Button) view.findViewById(R.id.calendar_day_gridcell);
            background.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        et_startTime.setVisibility(View.VISIBLE);
                        et_endTime.setVisibility(View.VISIBLE);
                        tv_showingTime.setVisibility(View.VISIBLE);
                        buttonSaveTime.setVisibility(View.VISIBLE);
                        background.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        et_startTime.setVisibility(View.INVISIBLE);
                        et_endTime.setVisibility(View.INVISIBLE);
                        tv_showingTime.setVisibility(View.INVISIBLE);
                        buttonSaveTime.setVisibility(View.INVISIBLE);
                        background.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }


                }
            });

            buttonSaveTime.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Schedule scheduleTo = new Schedule(et_startTime.getText().toString(), et_endTime.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("employeeSchedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date_month_year).setValue(scheduleTo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "schedule added successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }
}
