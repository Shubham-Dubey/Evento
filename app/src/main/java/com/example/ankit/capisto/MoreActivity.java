package com.example.ankit.capisto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class MoreActivity extends AppCompatActivity {

    ListView list;

    String[] maintitle =new String[]{
            "Reminders","Feedback",
            "Share","Donate",
            "About",
    };


    int[] imgid=new int[]{
            R.drawable.othericons_reminder,R.drawable.othericons_feedback,
            R.drawable.othericons_share,R.drawable.othericons_donate,
            R.drawable.othericons_about,
    };

    BottomNavigationView bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);



        bm=findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bm);
        Menu menu=bm.getMenu();
        MenuItem menuItem=menu.getItem(3);
        menuItem.setChecked(true);
        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Toast.makeText(MainActivity.this,urls[1],Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId())
                {
                    case R.id.hm:

                        startActivity(new Intent(MoreActivity.this,MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        customType(MoreActivity.this,"fadein-to-fadeout");
                        finish();
                        break;
                    case R.id.me:


                        startActivity(new Intent(MoreActivity.this,ProfileActivity.class));
                        customType(MoreActivity.this,"fadein-to-fadeout");
                        finish();
                        break;

                    case R.id.more:

                        break;


                }
                return true;
            }
        });

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", maintitle[i]);

            hm.put("listview_image", Integer.toString(imgid[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.icon, R.id.title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.more_single_layout, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list);
        androidListView.setAdapter(simpleAdapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> ad, View view, int position, long id) {
                switch (position) {
                    case 0:
                            Toast.makeText(MoreActivity.this,"item "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                            Toast.makeText(MoreActivity.this,"item "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                            Toast.makeText(MoreActivity.this,"item "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                            Toast.makeText(MoreActivity.this,"item "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                            Toast.makeText(MoreActivity.this,"item "+position,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    public void dothis(View v)
    {
        FloatingActionButton fab = findViewById(R.id.r1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(Intent.ACTION_SEND);

                final String appPackageName = getApplicationContext().getPackageName();
                String strAppLink = "";

                try
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                catch (android.content.ActivityNotFoundException anfe)
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                // this is the sharing part
                a.setType("text/link");
                String shareBody = "Hey! Download our app for free from the following link ." +
                        "\n"+""+strAppLink;
                String shareSub = "Evento";
                a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                a.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(a, "Share Using"));
            }
        });
    }
    }

