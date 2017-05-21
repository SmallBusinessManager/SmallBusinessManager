package com.projectcourse2.group11.smallbusinessmanager;



import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Invoice;
import com.projectcourse2.group11.smallbusinessmanager.model.InvoiceAdd;
import com.projectcourse2.group11.smallbusinessmanager.model.InvoiceMenu;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Person user;
    private DatabaseReference mRef;
    private ListAdapter mAdapter;
    private String companyID;
    private String currentScene="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        user = (Person) getIntent().getSerializableExtra("USER");
        companyID = this.getIntent().getStringExtra("COMPANY_ID");
        ListView listView = (ListView) findViewById(R.id.invoice_item);

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smallbusinessmanager-ddda6.firebaseio.com/invoice");
        FirebaseListAdapter<Invoice> firebaseListAdapter = new FirebaseListAdapter<Invoice>(
                this,
                Invoice.class,
                android.R.layout.expandable_list_content,
                firebaseDatabase
        ) {
            @Override
            protected void populateView(View v, Invoice model, int position) {
                TextView textview = (TextView) v.findViewById(android.R.id.text1);
                textview.setText(model.getCustomerId());
            }


        };
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new InvoiceMenu()).commit();
        currentScene = "main";

    }

    protected void addInvoice() {
            FragmentManager fm = getFragmentManager();
            currentScene = "add";
            fm.beginTransaction().replace(R.id.content_frame, new InvoiceAdd()).commit();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        if (currentScene.equals("add")) {
                FragmentManager fm = getFragmentManager();
                currentScene = "main";
                fm.beginTransaction().replace(R.id.content_frame, new InvoiceMenu()).commit();
        }else {

            startActivity(new Intent(InvoiceActivity.this, MainActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
            finish();
        }
    }
}