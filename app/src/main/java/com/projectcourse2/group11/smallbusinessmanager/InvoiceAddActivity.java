package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.security.PrivateKey;

public class InvoiceAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText invoice_amount_add;
    private Button add_invoice,cancel_invoice;

    private DatabaseReference ref;

    private Person user;
    private String companyID;
    private Project project;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_add);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            user = (Person) getIntent().getSerializableExtra("USER");
            companyID = getIntent().getStringExtra("COMPANY_ID");
            project=(Project)getIntent().getSerializableExtra("PROJECT");
        }

        invoice_amount_add=(EditText)findViewById(R.id.invoice_amount_add);
        add_invoice=(Button)findViewById(R.id.add_invoice);
        cancel_invoice=(Button)findViewById(R.id.cancel_invoice);

        ref=FirebaseDatabase.getInstance().getReference().child("projectInvoices").child(project.getId());

        add_invoice.setOnClickListener(this);
        cancel_invoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==add_invoice){
            String invoiceID=ref.push().getKey();
            Invoice invoice=new Invoice(invoiceID,invoice_amount_add.getText().toString());
            ref.child(invoiceID).setValue(invoice).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(InvoiceAddActivity.this,"New invoice created!",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(InvoiceAddActivity.this, InvoiceActivity.class).putExtra("PROJECT",project).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
                    }
                }
            });
        }
        if (v==cancel_invoice){
            finish();
            startActivity(new Intent(InvoiceAddActivity.this, InvoiceActivity.class).putExtra("PROJECT",project).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(InvoiceAddActivity.this, InvoiceActivity.class).putExtra("PROJECT",project).putExtra("COMPANY_ID", companyID).putExtra("USER", user));
                break;
        }
        return true;
    }
}