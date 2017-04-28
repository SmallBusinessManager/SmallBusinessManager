package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Activity;
import android.os.Bundle;

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}

