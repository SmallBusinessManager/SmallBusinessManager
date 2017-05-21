package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 21/05/2017.
 */

public class InvoiceSend extends Fragment implements View.OnClickListener {
    private Button send;
    private Button cancel;
    private String name,amount;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        name = args.getString("name");
        amount = args.getString("amount");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.invoice_send,null);
        send = (Button) view.findViewById(R.id.send_invoice);
        cancel = (Button) view.findViewById(R.id.cancel_send_invoice);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, new InvoiceMenu()).commit();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) view.findViewById(R.id.invoice_company_send);
                String text = "Company : "+ name + "total : "+amount;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice");
                emailIntent.putExtra(Intent.EXTRA_TEXT,text );
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
return view;
    }

    @Override
    public void onClick(View v) {

    }
}
