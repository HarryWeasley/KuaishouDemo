package com.example.kuaishoudemo;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Harry on 2019/4/19.
 * desc:父控件的拉动滑动动画，更易于集成
 */
public class AnimationFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    //当前自定义FrameLayout
    private FrameLayout frameLayout;
    //FrameLayout的第一个子view
    private View parent;
    private GestureDetector mGestureDetector;
    private float mExitScalingRef; // 触摸退出进度
    //子view的高度
    private int viewHeight;
    //结束的监听器
    private FinishListener finishListener;
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

    public AnimationFrameLayout(Context context) {
        this(context, null);
    }

    public AnimationFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        frameLayout = this;
        mGestureDetector = new GestureDetector(context, this);
    }

    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        boolean ret = super.dispatchTouchEvent(event);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mExitScalingRef < 0.5) {
                //缩小到一定的程度，将其关闭
                if (finishListener != null) {
                    finishListener.finish();
                }
            } else {
                //如果拉动距离不到某个角度，则将其动画返回原位置
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
                    }
                });
                valueAnimator.start();
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //必须要返回true，否则onScroll将不会被回调
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
        if (parent == null) {
            parent = getChildAt(0);
        }
        if (viewHeight == 0) {
            viewHeight = parent.getHeight();
        }
        float moveX = e2.getX() - e1.getX();
        float moveY = e2.getY() - e1.getY();

        mExitScalingRef = 1;
        mExitScalingRef = mExitScalingRef - moveY / viewHeight;
        parent.setTranslationX(moveX);
        parent.setTranslationY(moveY);
        parent.setScaleX(mExitScalingRef);
        parent.setScaleY(mExitScalingRef);
        if (mExitScalingRef > 1) {
            //当用户往上滑动的时候
            frameLayout.setBackgroundColor(mColorEvaluator.evaluate(2 - mExitScalingRef, 0x00000000, 0xFF000000));
        } else {
            //当用户往下滑动的时候
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

    public interface FinishListener {
        void finish();
    }
}
