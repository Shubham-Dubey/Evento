package com.example.ankit.capisto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import static maes.tech.intentanim.CustomIntent.customType;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        private FirebaseAuth firebaseAuth;
    BottomNavigationView bm;
    ConnectivityManager connectivityManager;

    //imageslider---
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private String[] urls;

    //navigationdrawer
    DrawerLayout dl;
    NavigationView nv;
    ActionBarDrawerToggle toggle;
    FirebaseUser user;
    TextView dname_tv,dregno;
    ImageView dimg_url;

    DatabaseReference sliderdata,navimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){

        }
        else
        {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Please Check your internet connection or try again later!!!", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }

        sliderdata=FirebaseDatabase.getInstance().getReference().child("Slider");



            sliderdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s1 = dataSnapshot.child("First").getValue().toString();
                    String s2 = dataSnapshot.child("Second").getValue().toString();
                    String s3 = dataSnapshot.child("Third").getValue().toString();
                    // Toast.makeText(MainActivity.this,"yes",Toast.LENGTH_SHORT).show();
                    pass(s1, s2, s3);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            user=FirebaseAuth.getInstance().getCurrentUser();
            String userid=user.getUid();
        navimage=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        navimage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String s1 = dataSnapshot.child("name").getValue().toString();
               // Toast.makeText(MainActivity.this,s1,Toast.LENGTH_LONG).show();

               String s2 = dataSnapshot.child("reg_no").getValue().toString();
                String s3 = dataSnapshot.child("image").getValue().toString();
                dname_tv=findViewById(R.id.tv_namenav);
                dregno=findViewById(R.id.tv_regd);
                dimg_url=findViewById(R.id.bn);
                Picasso.get().load(s3).placeholder(R.drawable.avatar).into(dimg_url);
                dregno.setText(s2);
                dname_tv.setText(s1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dl=findViewById(R.id.drawlayout);
        nv=findViewById(R.id.navigationview);
        nv.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        toggle=new ActionBarDrawerToggle(MainActivity.this,dl,R.string.nav_open,R.string.nav_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth=FirebaseAuth.getInstance();

        bm=findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bm);
        Menu menu=bm.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               // Toast.makeText(MainActivity.this,urls[1],Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId())
                {
                    case R.id.hm:


                        break;
                    case R.id.me:


                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        customType(MainActivity.this,"fadein-to-fadeout");
                       // finish();
                        break;

                    case R.id.more:
                        startActivity(new Intent(MainActivity.this,MoreActivity.class));
                        customType(MainActivity.this,"fadein-to-fadeout");
                       // finish();
                        break;


                }
                return true;
            }
        });


    }

    private void pass(String s1, String s2, String s3) {
        urls = new String[]{s1,s2,s3};
        init();
    }

    public  void participation(View v){
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
        Toast.makeText(MainActivity.this,"participation",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        if(menuItem.getItemId()==R.id.hm)
        {
            Toast.makeText(this,""+menuItem,Toast.LENGTH_SHORT).show();
        }
        if(menuItem.getItemId()==R.id.dash)
        {
            Toast.makeText(this,""+menuItem,Toast.LENGTH_SHORT).show();
        }
        if(menuItem.getItemId()==R.id.cnt)
        {
            Toast.makeText(this,""+menuItem,Toast.LENGTH_SHORT).show();
        }
        if(menuItem.getItemId()==R.id.share)
        {
            Intent i=new Intent(MainActivity.this,ChangePassword.class);
            startActivity(i);
           // Toast.makeText(this,""+menuItem,Toast.LENGTH_SHORT).show();
        }

        return false;
    }



    public void dothis(View v)
    {
        startActivity(new Intent(MainActivity.this,ShowMessageActivity.class));
       //show messages
    }
    public void showevents(View v)
    {
        startActivity(new Intent(this,EventsActivity.class));

    }


    @Override
    protected void onStart() {
        super.onStart();
      //  Toast.makeText(MainActivity.this,"in null",Toast.LENGTH_LONG).show();
        Menu menu=bm.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
      //  Toast.makeText(MainActivity.this,"in null",Toast.LENGTH_LONG).show();
       /* FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        if(currentuser==null){

            sendToStart();
        }
        else {
            Toast.makeText(MainActivity.this,"in null",Toast.LENGTH_LONG).show();
            String userid=currentuser.getUid();
            Log.d("user111","f4");
            if(userid.equals("zsF2vuWbu5Ohg1lyS8U5fOHnGBt1"))
            {
                Intent main = new Intent(MainActivity.this, AdminActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                finish();
            }
        }  */



    }
    private void sendToStart() {
        Intent startIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(startIntent);
        finish();
    }
    public void duck(View v)
    {
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        else
            Toast.makeText(this,"Check your internet connection or try again later",Toast.LENGTH_LONG).show();

    }
    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,urls));

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
