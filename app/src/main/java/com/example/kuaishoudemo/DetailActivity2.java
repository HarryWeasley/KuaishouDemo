package com.example.kuaishoudemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Harry on 2019/4/15.
 * desc:更易于集成的实现方式，layout里放入{@link AnimationFrameLayout}，通过LayoutInflater.from(this).inflate方式放入内部的布局
 */
public class DetailActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = getWindow().getDecorView();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
        }
        AnimationFrameLayout frameLayout = findViewById(R.id.frame_layout);
        //将内部的布局item_linear_layout，放入AnimationFrameLayout中
        View view = LayoutInflater.from(this).inflate(R.layout.item_linear_layout, frameLayout);
        ImageView imageView = view.findViewById(R.id.image);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);
        frameLayout.setFinishListener(new AnimationFrameLayout.FinishListener() {
            @Override
            public void finish() {
                onBackPressed();
            }
        });

    }


}
