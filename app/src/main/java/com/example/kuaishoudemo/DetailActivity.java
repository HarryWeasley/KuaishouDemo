package com.example.kuaishoudemo;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Harry on 2019/4/15.
 * desc:
 */
public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;
    private float mTouchSlop;
    LinearLayout parent;
    private float mExitScalingRef; // 触摸退出进度
    private int viewHeight;
    private FrameLayout frameLayout;

    TypeEvaluator<Integer> mColorEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startColor = startValue;
            int endColor = endValue;

            int alpha = (int) (Color.alpha(startColor) + fraction * (Color.alpha(endColor) - Color.alpha(startColor)));
            int red = (int) (Color.red(startColor) + fraction * (Color.red(endColor) - Color.red(startColor)));
            int green = (int) (Color.green(startColor) + fraction * (Color.green(endColor) - Color.green(startColor)));
            int blue = (int) (Color.blue(startColor) + fraction * (Color.blue(endColor) - Color.blue(startColor)));
            return Color.argb(alpha, red, green, blue);
        }
    };
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        parent = findViewById(R.id.parent);
        frameLayout = findViewById(R.id.frame_layout);
        ImageView imageView = findViewById(R.id.image);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);
        mGestureDetector = new GestureDetector(this, this);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mExitScalingRef < 0.5) {
                //缩小到一定的程度，将其关闭
                onBackPressed();
            } else {
                final float moveX = parent.getTranslationX();
                final float moveY = parent.getTranslationY();
                final float scaleX = parent.getScaleX();
                final float scaleY = parent.getScaleY();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float p = (float) animation.getAnimatedValue();
                        float animationMoveX = moveX + (0 - moveX) * p;
                        parent.setTranslationX(animationMoveX);
                        parent.setTranslationY(moveY + (0 - moveY) * p);
                        parent.setScaleX(scaleX + (1 - scaleX) * p);
                        parent.setScaleY(scaleY + (1 - scaleY) * p);
                        frameLayout.setBackgroundColor(mColorEvaluator.evaluate(p, 0x00000000, 0xFF000000));
                    }
                });
                valueAnimator.start();
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        viewHeight = parent.getHeight();
        float moveX = e2.getX() - e1.getX();
        float moveY = e2.getY() - e1.getY();

        mExitScalingRef = 1;
        if (moveY > 0) {
            mExitScalingRef = mExitScalingRef - moveY / viewHeight;
        } else {
            mExitScalingRef = mExitScalingRef + (-moveY) / viewHeight;
        }
        parent.setTranslationX(moveX);
        parent.setTranslationY(moveY);
        parent.setScaleX(mExitScalingRef);
        parent.setScaleY(mExitScalingRef);
        if (mExitScalingRef > 1) {
            frameLayout.setBackgroundColor(mColorEvaluator.evaluate(2 - mExitScalingRef, 0x00000000, 0xFF000000));
        } else {
            frameLayout.setBackgroundColor(mColorEvaluator.evaluate(mExitScalingRef, 0x00000000, 0xFF000000));
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
