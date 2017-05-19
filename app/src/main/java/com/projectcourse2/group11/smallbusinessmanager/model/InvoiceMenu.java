package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseListAdapter;
import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class InvoiceMenu extends Fragment implements View.OnClickListener{
    private FirebaseListAdapter<Message> adapter;
    private Button addstuff;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invoice, null);

        addstuff = (Button) view.findViewById(R.id.add);
        addstuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, new InvoiceAdd()).commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
