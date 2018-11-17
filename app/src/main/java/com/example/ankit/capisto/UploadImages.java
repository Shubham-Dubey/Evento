package com.example.ankit.capisto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UploadImages extends AppCompatActivity {

    String iposition;
    private StorageReference storeimage;
    private DatabaseReference anuserdatabase;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        storeimage= FirebaseStorage.getInstance().getReference();





        Spinner sp=findViewById(R.id.spinne);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Imageposition,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iposition=adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(UploadImages.this,""+iposition,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        anuserdatabase = FirebaseDatabase.getInstance().getReference().child("Slider");
    }
    public void slider(View v)
    {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(3,2)
            .start(UploadImages.this);

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
                StorageReference filepath=storeimage.child("slider_images").child(iposition+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            final String download_url=task.getResult().getDownloadUrl().toString();
                            anuserdatabase.child(iposition).setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(UploadImages.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(UploadImages.this, "Image Uploading: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            // Toast.makeText(ProfileActivity.this,"Error in uploading",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Toast.makeText(UploadImages.this, "Image Uploading: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

}
