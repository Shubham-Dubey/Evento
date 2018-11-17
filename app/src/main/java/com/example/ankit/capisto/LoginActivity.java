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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout lEmail;
    private TextInputLayout lPassword;

    private Button login;
    private ProgressDialog Dialog;
    private FirebaseAuth auth;


    ConnectivityManager connectivityManager;
  //  private DatabaseReference userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        auth=FirebaseAuth.getInstance();
        //progress dialog
        Dialog=new ProgressDialog(this);

        lEmail=findViewById(R.id.login_email);
        lPassword=findViewById(R.id.login_password);
        login=findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

                    String email=lEmail.getEditText().getText().toString();
                    String password=lPassword.getEditText().getText().toString();
                    if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
                    {
                        if(networkInfo!=null && networkInfo.isConnected()) {
                            Dialog.setTitle("Logining In");
                            Dialog.setMessage("Please wait...");
                            Dialog.setCanceledOnTouchOutside(false);
                            Dialog.show();
                            loginUser(email, password);
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    else {
                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(LoginActivity.this,"Enter your email",Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(password)){
                            Toast.makeText(LoginActivity.this,"Enter your Password",Toast.LENGTH_SHORT).show();
                        }


                    }


            }
        });
    }
    public void register(View v)
    {
        Intent main=new Intent(LoginActivity.this,RegisterActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
    }


    private void loginUser(final String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Dialog.dismiss();
                    String current_user_id = auth.getUid();
                    String devicetoken = FirebaseInstanceId.getInstance().getToken();
                   /* userdatabase.child(current_user_id).child("device_token").setValue(devicetoken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {  */
                   if(email.equals("ankit@gmail.com"))
                   {
                       Intent main = new Intent(LoginActivity.this,AdminActivity.class);
                       main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(main);
                       finish();
                   }
                   else {
                       Intent maine = new Intent(LoginActivity.this, MainActivity.class);
                       maine.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(maine);
                       finish();
                   }
                       /* }
                    });
                */
                } else {
                    Toast.makeText(LoginActivity.this, "User Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Dialog.hide();
                   // Toast.makeText(LoginActivity.this, "Unable to register,check the details and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser!=null){
            String userid=currentuser.getUid();
            Log.d("user111","f4");
            if(userid.equals("zsF2vuWbu5Ohg1lyS8U5fOHnGBt1"))
            {
                Intent main = new Intent(LoginActivity.this, AdminActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                finish();
            }
            else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}
