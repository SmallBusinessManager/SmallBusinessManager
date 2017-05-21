package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectcourse2.group11.smallbusinessmanager.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceMenu extends Fragment implements View.OnClickListener {
    private FirebaseListAdapter<Message> adapter;
    private Button addstuff;
    private String companyID;
    private ListView listView;
    private Button remove;
    private int position;
    private String deleteThisOne;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invoice, null);
        deleteThisOne = "";
        companyID = getActivity().getIntent().getStringExtra("COMPANY_ID");
        listView = (ListView) view.findViewById(R.id.invoice_item);
        Log.d(companyID, "<<<<<<<<companyId<<<<<<<<<<<<<");
        final ArrayList<Invoice> invoices = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("invoice").child(companyID);

        ListAdapter firebaseListAdapter = new FirebaseListAdapter<Invoice>(
                getActivity(),
                Invoice.class,
                android.R.layout.simple_list_item_multiple_choice,
                databaseReference) {
            @Override
            protected void populateView(View v, Invoice model, int position) {
                TextView textview = (TextView) v.findViewById(android.R.id.text1);
                String text = "Company : " + model.getCustomerId() + "\nAmount : " + model.getAmount() + "kr";
                textview.setText(text);
                invoices.add(model);
            }
        };
        addstuff = (Button) view.findViewById(R.id.add);
        addstuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, new InvoiceAdd()).commit();
            }
        });
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(firebaseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int place, long id) {
                deleteThisOne = invoices.get(place).getId();
                position=place;
                Log.d(String.valueOf(position),deleteThisOne);
            }
        });

        remove = (Button) view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deleteThisOne.equals("")) {
                    databaseReference.child(deleteThisOne).setValue(null);
                    invoices.remove(position);
                }
            }
        } );
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}