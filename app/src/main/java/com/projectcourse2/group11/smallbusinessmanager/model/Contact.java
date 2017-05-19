package com.projectcourse2.group11.smallbusinessmanager.model;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class Contact extends Fragment implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invoice,null);
    }
}
