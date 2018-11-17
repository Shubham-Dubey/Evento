package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailActivity extends AppCompatActivity {

    private DatabaseReference anuserdatabase;
    private ProgressDialog pd;
    TextView eventtitle,eventbody,eventby;
    String url;
    Button participate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        participate=findViewById(R.id.part);
        pd=new ProgressDialog(this);
        pd.setTitle("Loading Event Details");
        pd.setMessage("Please Wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        eventtitle=findViewById(R.id.e_detail_title);
        eventbody=findViewById(R.id.e_detail_body);
        eventby=findViewById(R.id.e_detail_by);

        String user_id=getIntent().getStringExtra("user_id");

        anuserdatabase= FirebaseDatabase.getInstance().getReference().child("events").child(user_id);

        anuserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eventt=dataSnapshot.child("title").getValue().toString();
                String eventb=dataSnapshot.child("body").getValue().toString();
                String evento=dataSnapshot.child("organisiedby").getValue().toString();
                url=dataSnapshot.child("Url").getValue().toString();
                eventtitle.setText(eventt);
                eventbody.setText(eventb);
                eventby.setText(" "+evento);
                if(url.equals("default"))
                {
                    participate.setVisibility(View.GONE);
                    participate.setEnabled(false);
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetailActivity.this,BrowserActivity.class);
                i.putExtra("url","https://"+url);
                startActivity(i);
                //Toast.makeText(EventDetailActivity.this,url,Toast.LENGTH_LONG).show();
            }
        });
    }
}
