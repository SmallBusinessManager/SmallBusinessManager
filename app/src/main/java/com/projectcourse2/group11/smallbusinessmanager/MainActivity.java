package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.calendar.Schedule;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ListView listView;
    private ListAdapter mAdapter;
    private HashMap<String, String> orderList = new HashMap<>();
    private TextView emailHeader;
    private ViewFlipper viewFlipperMain;
    private Toolbar toolbar;

    private String companyID;
    private Person user;

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
    Button buttonSaveTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewFlipperMain = (ViewFlipper) findViewById(R.id.viewFlipperMain);
        viewFlipperMain.setFlipInterval(0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_schedule:
                        Toast.makeText(MainActivity.this, "schedule selected", Toast.LENGTH_SHORT).show();
                        viewFlipperMain.setDisplayedChild(0);
                        break;
                    case R.id.nav_local_activity:
                        Toast.makeText(MainActivity.this, "activity selected", Toast.LENGTH_SHORT).show();
                        viewFlipperMain.setDisplayedChild(1);
                        activityViewHandling();
                        break;

                }

                return true;
            }

        });

        View headerView = navigationView.getHeaderView(0);
        emailHeader = (TextView) headerView.findViewById(R.id.emailHeader);
        emailHeader.setText(user.getEmail());

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        prevMonth = (ImageView) findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (Button) findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        et_startTime = (EditText) findViewById(R.id.et_startTime);
        et_endTime = (EditText) findViewById(R.id.et_endTime);
        buttonSaveTime = (Button) findViewById(R.id.buttonSaveTime);

        et_startTime.setVisibility(View.INVISIBLE);
        et_endTime.setVisibility(View.INVISIBLE);
        buttonSaveTime.setVisibility(View.INVISIBLE);
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"contact developer Danfeng for details",Toast.LENGTH_SHORT).show();
           // finish();
           // finishAffinity();
           // startActivity(new Intent(MainActivity.this, InvoiceActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_account) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, AccountActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_company) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, CompanyActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
        } else if (id == R.id.nav_project) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, ProjectActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        } else if (id == R.id.nav_order) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, SingleProjectHomeActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    private void activityViewHandling(){
        listView = (ListView) findViewById(R.id.MainListView);
        /**
         * If the logged in user is a worker or a team leader
         * load all the work orders that this worker has connected to it.
         */
        if (user.getPosition().equals(Position.WORKER) || user.getPosition().equals(Position.TEAM_LEADER)) {
            final ArrayList<String> projectIDs = new ArrayList<>();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("workerSSN").getValue(String.class).equals(user.getSsn())) {
                            orderList.put(ds.child("description").getValue(String.class), ds.getKey());
                            projectIDs.add(ds.child("projectID").getValue(String.class));
                        }
                    }
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<>(orderList.keySet()));
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listView.setAdapter(myAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to load orders", Toast.LENGTH_LONG).show();
                }
            };
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("companyWorkOrders").child(companyID).addValueEventListener(listener);

            listView.setOnItemClickListener(new DoubleClickListener() {
                @Override
                protected void onSingleClick(AdapterView<?> parent, View v, int position, long id) {
                    final String order = orderList.get(parent.getItemAtPosition(position));

                    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.nav_delete_project) {
                                FirebaseDatabase.getInstance().getReference().child("companyWorkOrders").child(companyID).child(order).removeValue();
                                reference.child(order).removeValue();
                            }
                            return true;
                        }
                    });
                }

                @Override
                protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                    String order = orderList.get(parent.getItemAtPosition(position));
                    Intent intent = new Intent(MainActivity.this, OrderCreation.class);
                    intent.putExtra("ORDER_ID", order);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("PROJECT_ID", projectIDs.get(position));
                    intent.putExtra("USER", user);
                    finish();
                    finishAffinity();
                    startActivity(intent);
                }
            });


        } else if (user.getPosition().equals(Position.ACCOUNTANT)) {
            finish();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, AccountantActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        } else {
            /**
             *  if logged in user is not a worker or a team leader
             *  that is if the user is manager,
             *  load all the projects for the company
             */
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("companyProjects").child(companyID);
            mAdapter = new FirebaseListAdapter<Project>(
                    MainActivity.this,
                    Project.class,
                    android.R.layout.simple_list_item_single_choice,
                    ref) {
                @Override
                protected void populateView(View v, Project model, int position) {
                    TextView textView = (TextView) v.findViewById(android.R.id.text1);
                    textView.setText(model.getName());
                    Log.e("hehe",model.getName());
                }
            };
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(mAdapter);

           /* listView.setOnItemClickListener(new DoubleClickListener() {
                @Override
                protected void onSingleClick(AdapterView<?> parent, View v, int position, long id) {
                    final Project project = (Project) parent.getItemAtPosition(position);

                    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.nav_delete_project) {
                                FirebaseDatabase.getInstance().getReference().child("projectOrders").child(project.getId()).removeValue();
                                ref.child(project.getId()).removeValue();
                            }
                            return true;
                        }
                    });
                }

                @Override
                protected void onDoubleClick(AdapterView<?> parent, View v, int position, long id) {
                    Project project = (Project) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, SingleProjectHomeActivity.class);
                    intent.putExtra("PROJECT", project);
                    intent.putExtra("COMPANY_ID", companyID);
                    intent.putExtra("USER", user);
                    finish();
                    finishAffinity();
                    startActivity(intent);
                }
            });*/
        }
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
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
                    list.add(String.valueOf(i) + "-PINK" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-BLACK" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            for (int i = 0; i < list.size() % 7; i++) {
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
            if (day_color[1].equals("BLACK")) {
                gridcell.setTextColor(Color.BLACK);
            }
            if (day_color[1].equals("PINK")) {
                gridcell.setTextColor(getResources().getColor(R.color.colorAccent));
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
            buttonSaveTime.setVisibility(View.INVISIBLE);
            final String date_month_year = (String) view.getTag();
            final Button background = (Button) view.findViewById(R.id.calendar_day_gridcell);
            background.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        et_startTime.setVisibility(View.VISIBLE);
                        et_endTime.setVisibility(View.VISIBLE);
                        buttonSaveTime.setVisibility(View.VISIBLE);
                        background.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        et_startTime.setVisibility(View.INVISIBLE);
                        et_endTime.setVisibility(View.INVISIBLE);
                        buttonSaveTime.setVisibility(View.INVISIBLE);
                        background.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }


                }
            });

            buttonSaveTime.setOnClickListener(new View.OnClickListener() {
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
