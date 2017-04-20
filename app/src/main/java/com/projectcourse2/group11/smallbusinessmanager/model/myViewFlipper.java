package com.projectcourse2.group11.smallbusinessmanager.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

import static com.projectcourse2.group11.smallbusinessmanager.R.color.holo_blue_dark;

public class myViewFlipper extends ViewFlipper {
    Paint paint = new Paint();

    public myViewFlipper(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        int width = getWidth();

        float margin = 8;
        float radius = 10;
        float cx = width / 2 - ((radius + margin) * 2 * getChildCount() / 2);
        float cy = getHeight() - 15;

        canvas.save();

        for (int i = 0; i < getChildCount(); i++)
        {
            if (i == getDisplayedChild())
            {
                paint.setColor(getResources().getColor(holo_blue_dark));
                canvas.drawCircle(cx, cy, radius, paint);

            } else
            {
                paint.setColor(Color.GRAY);
                canvas.drawCircle(cx, cy, radius, paint);
            }
            cx += 2 * (radius + margin);
        }
        canvas.restore();
    }
}
