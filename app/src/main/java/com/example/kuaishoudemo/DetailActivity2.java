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
 * desc:
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
        View view = LayoutInflater.from(this).inflate(R.layout.item_linear_layoutl, frameLayout);
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
