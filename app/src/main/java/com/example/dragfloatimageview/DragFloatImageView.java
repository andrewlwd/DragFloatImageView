package com.example.dragfloatimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

public class DragFloatImageView extends AppCompatImageView {
    private float lastX;
    private float lastY;
    private float parentHeight;
    private float parentWidth;
    private boolean isDrag;

    public DragFloatImageView(Context context) {
        this(context, null);
    }

    public DragFloatImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                isDrag = false;
                if (getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = rawX - lastX;
                float dy = rawY - lastY;
                if (Math.abs(dx * dx + dy * dy) > 1) {
                    float x = getX() + dx;
                    float y = getY() + dy;
                    //这样写在到达上下边界时会有抖动效果
                    y = getY() < 0 ? 0 : (getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y);
                    setX(x);
                    setY(y);
                    lastX = rawX;
                    lastY = rawY;
                    isDrag = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    setPressed(false);
                }
                if (rawX > parentWidth / 2) {
                    //靠右吸附
                    animateX(parentWidth - getWidth() - getX());
                } else {
                    //靠左吸附
                    animateX(-getX());
                }
                break;
        }
        return isDrag || super.onTouchEvent(event);
    }

    private void animateX(float dx) {
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .xBy(dx)
                .start();
    }
}
