package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView auserslist;
    private ProgressDialog pd;
    private DatabaseReference auserdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auserdatabase= FirebaseDatabase.getInstance().getReference().child("events");
        auserslist=findViewById(R.id.eventslist);
        auserslist.setHasFixedSize(true);
        pd=new ProgressDialog(this);
        pd.setTitle("Loading Events");
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        LinearLayoutManager l=new LinearLayoutManager(this);
        //l.setReverseLayout(true);
        l.setReverseLayout(true);
        l.setStackFromEnd(true);
        auserslist.setLayoutManager(l);

    }
    @Override
    protected void onStart() {
        super.onStart();


        final FirebaseRecyclerAdapter<Events,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Events, UsersViewHolder>(
                Events.class,
                R.layout.events_single_layout,
                UsersViewHolder.class,
                auserdatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Events model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setEvent(model.getEvent_type());
                viewHolder.setDate(model.getDate_time());
                 final String User_id=getRef(position).getKey();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(EventsActivity.this, User_id, Toast.LENGTH_SHORT).show();
                        Intent pi=new Intent(EventsActivity.this,EventDetailActivity.class);
                        pi.putExtra("user_id",User_id);
                       startActivity(pi);
                    }
                });
            }
        };
        auserslist.setAdapter(firebaseRecyclerAdapter);
        pd.dismiss();
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View view;
        public UsersViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setTitle(String titl){
            TextView userName=view.findViewById(R.id.e_title);
            userName.setText(titl);
        }
        public void setEvent(String eventt)
        {
            TextView userStatus=view.findViewById(R.id.e_type);
            userStatus.setText(eventt);
        }

        public void setDate(String time) {
            TextView userStatus=view.findViewById(R.id.e_date);
            userStatus.setText(time);
        }
    }
}
