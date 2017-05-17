package com.projectcourse2.group11.smallbusinessmanager;



import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.ProjectActivity;
import com.projectcourse2.group11.smallbusinessmanager.R;
import com.projectcourse2.group11.smallbusinessmanager.model.Invoice;
import com.projectcourse2.group11.smallbusinessmanager.model.InvoiceFragment;


import static android.R.attr.fragment;
import static android.R.attr.mode;
import static com.projectcourse2.group11.smallbusinessmanager.R.id.listView;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mRef;
    private ListAdapter mAdapter;
    private String companyID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);

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
    }

    protected void addInvoice() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.invoice_frame, new InvoiceFragment()).commit();
    }

    @Override
    public void onClick(View v) {

    }
}