package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceAdd extends Fragment implements View.OnClickListener{
    private FirebaseListAdapter<Message> adapter;
    private View view;
    private Button cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invoice_add,null);
        cancel = (Button) view.findViewById(R.id.cancel_invoice);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame,new InvoiceMenu()).commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
