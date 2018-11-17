package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileActivity extends AppCompatActivity {

    private StorageReference storeimage;
    private ImageView profile_image,profile_changeimage;

    private TextView profile_name, profile_mail, profile_number, profile_address, profile_reg, profile_course, profile_linkedin;
    private DatabaseReference anuserdatabase;
    private ProgressDialog pd;
    private FirebaseUser Current_user;
    ProgressDialog progressDialog;
    String no,addres,course,linked;
    BottomNavigationView bm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading Profile");
        pd.setMessage("Please Wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        profile_image = findViewById(R.id.profile);
        profile_changeimage=findViewById(R.id.change_image);
        profile_name = findViewById(R.id.tv_name);
        profile_mail = findViewById(R.id.tv_email);
        profile_number = findViewById(R.id.tv_Mobile);
        profile_address = findViewById(R.id.tv_address);
        profile_reg = findViewById(R.id.tv_regno);
        profile_course = findViewById(R.id.tv_course);
        profile_linkedin = findViewById(R.id.tv_linkedin);

        bm=findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bm);
        Menu menu=bm.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);
        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Toast.makeText(MainActivity.this,urls[1],Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId())
                {
                    case R.id.hm:

                        startActivity(new Intent(ProfileActivity.this,MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        customType(ProfileActivity.this,"fadein-to-fadeout");
                        finish();
                        break;
                    case R.id.me:

                        break;

                    case R.id.more:
                        startActivity(new Intent(ProfileActivity.this,MoreActivity.class));
                        customType(ProfileActivity.this,"fadein-to-fadeout");
                        finish();
                        break;


                }
                return true;
            }
        });

        storeimage= FirebaseStorage.getInstance().getReference();

        Current_user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = Current_user.getUid();
        anuserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        anuserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayname = dataSnapshot.child("name").getValue().toString();
                String mail = dataSnapshot.child("email").getValue().toString();
                 no = dataSnapshot.child("mobileno").getValue().toString();
                 addres = dataSnapshot.child("address").getValue().toString();
                String reg = dataSnapshot.child("reg_no").getValue().toString();
                course = dataSnapshot.child("course").getValue().toString();
                linked = dataSnapshot.child("linkedin").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                profile_name.setText(displayname);
                profile_mail.setText(mail);
                profile_number.setText(no);
                profile_address.setText(addres);
                profile_reg.setText(reg);
                profile_course.setText(course);
                profile_linkedin.setText(linked);
               if (!image.equals("default")) {
                    //Picasso.get().load(image).placeholder(R.drawable.avatar).into(displayimage);

                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avatar).into(profile_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            pd.dismiss();
                           // Toast.makeText(ProfileActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            Picasso.get().load(image).placeholder(R.drawable.avatar).into(profile_image);

                        }
                    });
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
               // Toast.makeText(ProfileActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
        profile_changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent galleryintent = new Intent();
                galleryintent.setType("Image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(Intent.createChooser(galleryintent, "SELECT IMAGE"), gallery_pick); */
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(ProfileActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading Image...");
                progressDialog.setMessage("Please wait while the image is uploading");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Uri resultUri = result.getUri();
                String current_user_id=Current_user.getUid();
                StorageReference filepath=storeimage.child("profile_images").child(current_user_id+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            final String download_url=task.getResult().getDownloadUrl().toString();
                            anuserdatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        progressDialog.dismiss();
                                       // Toast.makeText(ProfileActivity.this, "Image Uploading: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this,"Error in uploading",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                           // Toast.makeText(ProfileActivity.this, "Image Uploading: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
    public void editdetails(View v)
    {
        Intent i=new Intent(ProfileActivity.this,EditProfileActivity.class);
        i.putExtra("mobi",no);
        i.putExtra("address",addres);
        i.putExtra("course",course);
        i.putExtra("linkedin",linked);
        startActivity(i);
    }

}
