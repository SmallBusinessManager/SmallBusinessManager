package com.projectcourse2.group11.smallbusinessmanager.Fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.projectcourse2.group11.smallbusinessmanager.R;

/**
 * Created by Bjarni on 06/05/2017.
 */

public class OpeningFragment extends Fragment implements
        GestureDetector.OnGestureListener,View.OnClickListener {

    private GestureDetectorCompat mDetector;
    Button button_signUp, button_signIn;
    ViewFlipper viewFlipper;
    Animation slide_in_left, slide_out_right;
    Animation slide_in_right, slide_out_left;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetector = new GestureDetectorCompat(getActivity(), this);


                button_signUp = (Button) getActivity().findViewById(R.id.button_signUp);
        button_signIn = (Button) getActivity().findViewById(R.id.button_signIn);
        viewFlipper = (ViewFlipper) getActivity().findViewById(R.id.viewflipper);

        slide_in_left = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_right);

        slide_in_right = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_left);
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
                getActivity().finish();
                Fragment newFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment newFragment = new RegisterFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().finish();
                Fragment newFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onClick(View v) {

    }

}

