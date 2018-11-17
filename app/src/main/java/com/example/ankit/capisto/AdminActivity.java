package com.example.ankit.capisto;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class AdminActivity extends AppCompatActivity {


    DatabaseReference sliderdata;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private String[] urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sliderdata=FirebaseDatabase.getInstance().getReference().child("Slider");
        sliderdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s1= dataSnapshot.child("First").getValue().toString();
                String s2= dataSnapshot.child("Second").getValue().toString();
                String s3= dataSnapshot.child("Third").getValue().toString();
               // Toast.makeText(AdminActivity.this,"yes",Toast.LENGTH_SHORT).show();
                pass(s1,s2,s3);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void pass(String s1, String s2, String s3) {
        urls = new String[]{s1,s2,s3};
        init();
    }

    public void dothis(View view)
    {
        Intent i=new Intent(AdminActivity.this,CreateEventActivity.class);
        startActivity(i);
    }
    public void movetocreateevent(View view)
    {
        Intent i=new Intent(AdminActivity.this,CreateEventActivity.class);
        startActivity(i);
    }
    public void movetoevents(View view)
    {
        startActivity(new Intent(AdminActivity.this,EventsActivity.class));
    }
    public void gosignout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    public void upload(View view)
    {
        startActivity(new Intent(AdminActivity.this,UploadImages.class));
    }
    public void showusers(View view)
    {
        startActivity(new Intent(AdminActivity.this,UsersActivity.class));
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(AdminActivity.this,urls));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }






}
