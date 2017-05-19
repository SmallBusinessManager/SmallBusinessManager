package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceAdd extends Fragment implements View.OnClickListener{
    private FirebaseListAdapter<Message> adapter;
    private View view;
    private Button cancel;
    private Button add;
    private FirebaseAuth mAuth;
    private EditText companyName;
    private EditText editTextamount;
    private DatabaseReference ref;
    private String company;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invoice_add,null);
        add = (Button) view.findViewById(R.id.add_invoice);
        fm = getFragmentManager();
        companyName = (EditText) view.findViewById(R.id.invoice_company_add);
        editTextamount = (EditText) view.findViewById(R.id.invoice_amount_add);
        company = getActivity().getIntent().getStringExtra("COMPANY_ID");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
                fm.beginTransaction().replace(R.id.content_frame,new InvoiceMenu()).commit();
            }
        });
        cancel = (Button) view.findViewById(R.id.cancel_invoice);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.content_frame,new InvoiceMenu()).commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

    }
    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("invoice").child(company);
        String companyId = companyName.getText().toString();
        String amount = editTextamount.getText().toString();
        String key = databaseReference.push().getKey();
        Invoice invoice = new Invoice(key, companyId, amount);
        databaseReference.updateChildren(invoice.toHashMap());
    }
    }
