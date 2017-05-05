package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectcourse2.group11.smallbusinessmanager.R;

import java.util.zip.Inflater;

/**
 * Created by Bjarni on 05/05/2017.
 */

public class MainScreenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main,container,false);
    }
}
