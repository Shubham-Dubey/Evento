package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowMessageActivity extends AppCompatActivity {


    private RecyclerView auserslist;
    private ProgressDialog pd;
    private DatabaseReference auserdatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user=FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getUid();
        auserdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("messages");
        auserslist=findViewById(R.id.rec);
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


         FirebaseRecyclerAdapter<Message,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Message,UsersViewHolder>(
                Message.class,
                R.layout.message_single_layout,
                UsersViewHolder.class,
                auserdatabase
        ) {
            @Override
            protected void populateViewHolder(ShowMessageActivity.UsersViewHolder viewHolder, Message model, int position) {
                viewHolder.setTitle(model.getMtitle());
                viewHolder.setBody(model.getMbody());
                viewHolder.setFrom(model.getMfrom());
                viewHolder.setTime(model.getMtime());
               // final String User_id=getRef(position).getKey();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(EventsActivity.this, User_id, Toast.LENGTH_SHORT).show();

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
            TextView userName=view.findViewById(R.id.tit);
            userName.setText(titl);
        }
        public void setBody(String eventt)
        {
            TextView userStatus=view.findViewById(R.id.bod);
            userStatus.setText(eventt);
        }

        public void setFrom(String by) {
            TextView userStatus=view.findViewById(R.id.by);
            userStatus.setText(by);
        }
        public void setTime(String time) {
            TextView userStatus=view.findViewById(R.id.dat);
            userStatus.setText(time);
        }
    }
}
