package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout ed_name,ed_mail,ed_mobile,ed_password,ed_reg;
    private ProgressDialog reg_progress_dialog;
    private DatabaseReference database;
    private FirebaseAuth mAuth;
    private Button rcreate;

    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  */
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();




        ed_name=findViewById(R.id.display_name);
        ed_mail=findViewById(R.id.email);
        ed_mobile=findViewById(R.id.phone);
        ed_password=findViewById(R.id.password);
        ed_reg=findViewById(R.id.reg_no);
        rcreate=findViewById(R.id.register);

        reg_progress_dialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        rcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                String display_name=ed_name.getEditText().getText().toString();
                String email=ed_mail.getEditText().getText().toString();
                String phone=ed_mobile.getEditText().getText().toString();
                String password=ed_password.getEditText().getText().toString();
                String regno=ed_reg.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name)&&!TextUtils.isEmpty(regno)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)) {
                    if(networkInfo!=null && networkInfo.isConnected()) {
                        reg_progress_dialog.setTitle("Registering User");
                        reg_progress_dialog.setMessage("Please Wait...");
                        reg_progress_dialog.setCanceledOnTouchOutside(false);
                        reg_progress_dialog.show();
                        register_user(display_name, email,phone, password,regno);
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Internet not available, Cross check your internet connectivity and try again")
                                .setTitle("INFO")
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }

                }
            }
        });

    }
    private void register_user(final String display_name, final String email, final String phone, String password, final String regno)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=current_user.getUid();
                    database=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                   // String devicetoken= FirebaseInstanceId.getInstance().getToken();

                    HashMap<String,String> usermap=new HashMap<>();
                   // usermap.put("device_token",devicetoken);
                    usermap.put("name",display_name);
                    usermap.put("email",email);
                    usermap.put("image","default");
                    usermap.put("mobileno",phone);
                    usermap.put("address","");
                    usermap.put("reg_no",regno);
                    usermap.put("course","");
                    usermap.put("linkedin","");
                    database.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                reg_progress_dialog.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                            else {
                                reg_progress_dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    reg_progress_dialog.hide();
                    Toast.makeText(RegisterActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(RegisterActivity.this,"Unable to register,check the details and try again",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void login(View v)
    {
        Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
