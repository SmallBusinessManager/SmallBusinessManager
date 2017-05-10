package com.projectcourse2.group11.smallbusinessmanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.LoginFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.ProjectCreateFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.ProjectScreenFragment;
import com.projectcourse2.group11.smallbusinessmanager.Fragment.RegisterFragment;

import java.util.concurrent.Executor;

/**
 * An animated opening page
 */

public class OpeningActivity extends Activity implements
        GestureDetector.OnGestureListener {
    private FirebaseAuth firebaseAuth;
    private GestureDetectorCompat mDetector;
    private ProgressDialog progressDialog;

    Button button_signUp, button_signIn;
    ViewFlipper viewFlipper;

    Animation slide_in_left, slide_out_right;
    Animation slide_in_right, slide_out_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        mDetector = new GestureDetectorCompat(this, this);

        button_signUp = (Button) findViewById(R.id.button_signUp);
        button_signIn = (Button) findViewById(R.id.button_signIn);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        slide_in_left = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_right);

        slide_in_right = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_left);

        button_signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.opening_frame, new RegisterFragment()).commit();

            }
        });

        button_signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.opening_frame, new LoginFragment()).commit();

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float sensitivity = 50;

        if ((e1.getX() - e2.getX()) > sensitivity) {
            if (viewFlipper.getDisplayedChild() != 2) {
                viewFlipper.setInAnimation(slide_in_right);
                viewFlipper.setOutAnimation(slide_out_left);
                viewFlipper.showNext();
            } else {
                finish();
                Intent intent = new Intent(OpeningActivity.this, LoginActivity.class);
                OpeningActivity.this.startActivity(intent);
            }
        } else if ((e2.getX() - e1.getX()) > sensitivity) {
            if (viewFlipper.getDisplayedChild() != 0) {

                viewFlipper.setInAnimation(slide_in_left);
                viewFlipper.setOutAnimation(slide_out_right);
                viewFlipper.showPrevious();
            }
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
}