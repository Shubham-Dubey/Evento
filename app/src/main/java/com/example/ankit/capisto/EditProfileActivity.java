package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout mo,addr,course,linked;
    Button update;

    private DatabaseReference database;
    private FirebaseUser currentuser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mo=findViewById(R.id.ed_phone);
        addr=findViewById(R.id.ed_addres);
        course=findViewById(R.id.ed_course);
        linked=findViewById(R.id.ed_linked);

        update=findViewById(R.id.update);

        String mobi=getIntent().getStringExtra("mobi");
        String add=getIntent().getStringExtra("address");
        String cour=getIntent().getStringExtra("course");
        String link=getIntent().getStringExtra("linkedin");

        mo.getEditText().setText(mobi);
        addr.getEditText().setText(add);
        course.getEditText().setText(cour);
        linked.getEditText().setText(link);

        progressDialog=new ProgressDialog(this);
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        String uid=currentuser.getUid();
        database= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String mobi= mo.getEditText().getText().toString();
                String add=addr.getEditText().getText().toString();
                String cou= course.getEditText().getText().toString();
                String link=linked.getEditText().getText().toString();


                progressDialog.setTitle("Saving Status");
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                HashMap<String,String> usermap=new HashMap<>();
                usermap.put("mobileno",mobi);
                usermap.put("address",add);
                usermap.put("course",cou);
                usermap.put("linkedin",link);
                database.child("mobileno").setValue(mobi);
                database.child("address").setValue(add);
                database.child("course").setValue(cou);
                database.child("linkedin").setValue(link).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent i=new Intent(EditProfileActivity.this,ProfileActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(EditProfileActivity.this, "Details updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, "Check your Internet connnection", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });


    }
}
