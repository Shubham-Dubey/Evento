package com.example.ankit.capisto;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimatedSplashActivity extends AppCompatActivity {

    Animation ani,ani2,ani3;
    ImageView splashiv,iv;

    @Override
    protected void onStart() {
        super.onStart();
        dothis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_splash);

        splashiv=findViewById(R.id.splashiv);
        iv=findViewById(R.id.vertoanimationtext);

        ani= AnimationUtils.loadAnimation(AnimatedSplashActivity.this, R.anim.animation);
        ani2= AnimationUtils.loadAnimation(AnimatedSplashActivity.this, R.anim.animation2);
        ani3=AnimationUtils.loadAnimation(AnimatedSplashActivity.this, R.anim.animation2);



        Handler h= new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }



        },4000);
    }
    public void dothis()
    {
        splashiv.startAnimation(ani);
        iv.startAnimation(ani2);

    }
}
