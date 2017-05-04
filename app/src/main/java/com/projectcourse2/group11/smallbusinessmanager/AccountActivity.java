package com.projectcourse2.group11.smallbusinessmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        LinearLayout ll = (LinearLayout) findViewById(R.id.llMain);
        for (View view : ll.getTouchables()){
            if (view instanceof EditText){
                EditText editText = (EditText) view;
                editText.setEnabled(false);
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
        }

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Personal Information");
        listDataHeader.add("Account Information");
        listDataHeader.add("In Company Information");

        List<String> personalInfo = new ArrayList<>();
        personalInfo.add("SSN:Shawshank Redemption");
        personalInfo.add("FirstName:Shawshank Redemption");
        personalInfo.add("LastName:Shawshank Redemption");
        personalInfo.add("Email:Godfather Part II");
        personalInfo.add("Phone:Fiction");
        personalInfo.add("Age:Godfather");
        personalInfo.add("Gender:Godfather");
        personalInfo.add("Address:Good the Bad and the Ugly");
        personalInfo.add("PostCode:Dark Knight");
        personalInfo.add("City:Angry Men");
        personalInfo.add("Country:Angry Men");

        List<String> accountInfo = new ArrayList<>();
        accountInfo.add("UserName:Conjuring");
        accountInfo.add("Password:cable Me 2");

        List<String> inCompanyInfo = new ArrayList<>();
        inCompanyInfo.add("Contract ID:2 Gun,s");
        inCompanyInfo.add("Title:The Sm,urfs 2");
        inCompanyInfo.add("Salary:Spectacular Now");
        inCompanyInfo.add("WorkingHour:Cany,ons");

        listDataChild.put(listDataHeader.get(0), personalInfo);
        listDataChild.put(listDataHeader.get(1), accountInfo);
        listDataChild.put(listDataHeader.get(2), inCompanyInfo);
    }

    @Override
    public void onClick(View v) {

    }
}

