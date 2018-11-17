package com.example.ankit.capisto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView auserslist;
    private DatabaseReference auserdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auserdatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        auserslist=findViewById(R.id.userslist);
        auserslist.setHasFixedSize(true);

        auserslist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                auserdatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setReg(model.getReg_no());
                viewHolder.setUserImage(model.getThumb_image());
                final String User_id=getRef(position).getKey();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UsersActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.send_message, null);
                        final TextInputLayout msg_et =  mView.findViewById(R.id.et_msg);
                        final TextInputLayout msg_et_by =  mView.findViewById(R.id.et_msg_by);
                        final EditText msg_ett =  mView.findViewById(R.id.aaa);


                        mBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String mtitle=msg_et.getEditText().getText().toString();
                                String mby=msg_et_by.getEditText().getText().toString();
                                String aa=msg_ett.getText().toString();
                                if(TextUtils.isEmpty(mtitle)||TextUtils.isEmpty(aa)||TextUtils.isEmpty(mby)) {
                                    Toast.makeText(UsersActivity.this, "Message not send :Either body or title is empty" , Toast.LENGTH_LONG).show();
                                }
                                if(!TextUtils.isEmpty(mtitle)&&!TextUtils.isEmpty(aa)&&!TextUtils.isEmpty(mby)) {
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("(dd-MM-yyyy / h:mm a)");
                                    String formattedDate = df.format(c.getTime());
                                    HashMap<String,String> m=new HashMap<>();
                                    m.put("mtitle",mtitle);
                                    m.put("mbody",aa);
                                    m.put("mtime",formattedDate);
                                    m.put("mfrom",mby);
                                    auserdatabase.child(User_id).child("messages").push().setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful())
                                               Toast.makeText(UsersActivity.this, "Message sent Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        });

                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(UsersActivity.this, "", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();

                            }
                        });
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();


                // Intent pi=new Intent(UsersActivity.this,ProfileActivity.class);
                       // pi.putExtra("user_id",User_id);
                        //startActivity(pi);
                    }
                });
            }
        };
        auserslist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View view;
        public UsersViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setName(String name){
            TextView userName=view.findViewById(R.id.users_single_name);
            userName.setText(name);
        }
        public void setReg(String status)
        {
            TextView userStatus=view.findViewById(R.id.users_single_status);
            userStatus.setText(status);
        }

        public void setUserImage(String thumb_image) {
            CircleImageView userimage=view.findViewById(R.id.users_single_image);
            Picasso.get().load(thumb_image).placeholder(R.drawable.avatar).into(userimage);

        }
    }
}