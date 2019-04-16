package com.example.kuaishoudemo;

import android.animation.TypeEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

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
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("test222", "onDown-----" + e.getAction());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("test222", "onSingleTapUp-----" + e.getAction());
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        viewHeight = parent.getHeight();
        float moveX = e2.getX() - e1.getX();
        float moveY = e2.getY() - e1.getY();
        parent.setTranslationX(moveX);
        parent.setTranslationY(moveY);
        mExitScalingRef = 1;
        if (moveY > 0) {
            mExitScalingRef -= moveY / viewHeight;
        }
        frameLayout.setBackgroundColor(mColorEvaluator.evaluate(mExitScalingRef, 0x00000000, 0xFF000000));
        Log.i("test222", "onSingleTapUp-----mExitScalingRef=" + mExitScalingRef);
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
