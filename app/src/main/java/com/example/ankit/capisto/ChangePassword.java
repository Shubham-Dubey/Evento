package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    FirebaseUser user;
    AuthCredential credential;
    ProgressDialog pdd;
    TextInputLayout old,new_p,c_new;
    Button b;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
         user = FirebaseAuth.getInstance().getCurrentUser();
         email=user.getEmail();
         old=findViewById(R.id.old_password);
         new_p=findViewById(R.id.new_password);
         c_new=findViewById(R.id.confirm_new_password);

         String oldp=old.getEditText().getText().toString();
        b=findViewById(R.id.change_password);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldp=old.getEditText().getText().toString();
                String new_pa=new_p.getEditText().getText().toString();
                final String c_n_p=c_new.getEditText().getText().toString();
                if(!TextUtils.isEmpty(oldp))
                {
                     credential = EmailAuthProvider
                            .getCredential(email, oldp);
                }
                else
                    Toast.makeText(ChangePassword.this, "Old password is empty", Toast.LENGTH_LONG).show();


// Prompt the user to re-provide their sign-in credentials
                if(!new_pa.equals(c_n_p))
                {
                    Toast.makeText(ChangePassword.this, "New Password is Not Matched", Toast.LENGTH_LONG).show();
                }
                else if(!TextUtils.isEmpty(oldp)&&!TextUtils.isEmpty(new_pa)&&!TextUtils.isEmpty(c_n_p)){
                     pdd=new ProgressDialog(ChangePassword.this);
                    pdd.setTitle("Updating Password");
                    pdd.setMessage("Please wait...");
                    pdd.setCanceledOnTouchOutside(false);
                    pdd.show();
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(c_n_p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //  Log.d(TAG, "Password updated");
                                                    pdd.dismiss();
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                                    builder.setMessage("Password Updated Successfuly")
                                                            .setIcon(android.R.drawable.ic_dialog_alert);
                                                    builder.setPositiveButton("OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent i=new Intent(ChangePassword.this,MainActivity.class);
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog dialog=builder.create();
                                                    dialog.show();
                                                } else {
                                                    Toast.makeText(ChangePassword.this, ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    //  Log.d(TAG, "Error password not updated")
                                                    pdd.dismiss();
                                                }
                                            }
                                        });
                                    } else {
                                        //  Log.d(TAG, "Error auth failed")
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                        if (errorCode.equals("ERROR_WRONG_PASSWORD"))
                                            Toast.makeText(ChangePassword.this, "Old Password is not Correct", Toast.LENGTH_LONG).show();
                                        pdd.dismiss();
                                    }
                                }
                            });

                }

            }
        });


    }
}
