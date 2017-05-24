package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Invoice;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView_invoice;
    private TextView tv_addInvoice;
    private FloatingActionButton fabSend;

    private DatabaseReference ref;
    private ListAdapter adapter;

    private Person user;
    private Project project;
    private String companyID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarI);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            project=(Project)getIntent().getSerializableExtra("PROJECT");
            user = (Person) getIntent().getSerializableExtra("USER");
            companyID = this.getIntent().getStringExtra("COMPANY_ID");
        }

        tv_addInvoice = (TextView) findViewById(R.id.tv_addInvoice);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);
        tv_addInvoice.setOnClickListener(this);
        fabSend.setOnClickListener(this);
        listView_invoice = (ListView) findViewById(R.id.listview_invoice);
        ref = FirebaseDatabase.getInstance().getReference().child("projectInvoices").child(project.getId());
        adapter = new FirebaseListAdapter<Invoice>(
                this,
                Invoice.class,
                android.R.layout.simple_list_item_single_choice,
                ref
        ) {
            @Override
            protected void populateView(View v, Invoice model, int position) {
                TextView textview = (TextView) v.findViewById(android.R.id.text1);
                textview.setText(model.getAmount());
            }


        };

        listView_invoice.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView_invoice.setAdapter(adapter);

        listView_invoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               final Invoice selectedInvoice=(Invoice)parent.getItemAtPosition(position);
                fabSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","danfeng.trondset@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,"Invoice context" );
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });

                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.nav_delete_invoice){
                            ref.child(selectedInvoice.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(InvoiceActivity.this,"Invoice deleted successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == tv_addInvoice) {
            Intent intent=new Intent(InvoiceActivity.this,InvoiceAddActivity.class);
            intent.putExtra("PROJECT",project);
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
        if (v == fabSend) {
            Toast.makeText(this,"Please select an invoice from the list",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(InvoiceActivity.this, MainActivity.class).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
                break;
        }
        return true;
    }
}