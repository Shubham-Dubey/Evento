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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class CreateEventActivity extends AppCompatActivity {

    ProgressDialog pd;
    String eventype,eventurl;
    TextInputLayout Title,organisedby,Body;
    Button createevent;
    DatabaseReference eventdatabase;
    RadioButton url;
    EditText eurl;

    int statee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createevent);


        Title=findViewById(R.id.e_title);
        Body=findViewById(R.id.e_body);
        organisedby=findViewById(R.id.e_orga_by);
        createevent=findViewById(R.id.create);
        Spinner spinner=findViewById(R.id.spinner);
        url=findViewById(R.id.radioButton);
        eurl=findViewById(R.id.e_url);
        eurl.setVisibility(View.INVISIBLE);

        statee=0;
       url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statee==0) {

                    statee=1;
                }
                else if(statee==1)
                {

                    url.setChecked(false);
                    statee=0;
                }

            }
        });
        url.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    eurl.setVisibility(View.VISIBLE);
                    eventurl="url";
                   // eventurl=eurl.getText().toString();
                }
                else {
                    eurl.setVisibility(View.INVISIBLE);
                    eventurl = "default";
                }
            }
        });


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.EventType,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eventype=adapterView.getItemAtPosition(i).toString();
               // Toast.makeText(CreateEventActivity.this,""+text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title=Title.getEditText().getText().toString();
                final String body=Body.getEditText().getText().toString();
                String organised=organisedby.getEditText().getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy / h:mm a");
                String formattedDate = df.format(c.getTime());
                if(statee==1)
                    eventurl=eurl.getText().toString();
               // Toast.makeText(CreateEventActivity.this, formattedDate, Toast.LENGTH_SHORT).show();
                final HashMap<String,String> usermap=new HashMap<>();
                usermap.put("event_type",eventype);
                usermap.put("date_time",formattedDate);
                usermap.put("title",title);
                usermap.put("body",body);
                usermap.put("organisiedby",organised);
                usermap.put("Url",eventurl);

                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(body)&&!TextUtils.isEmpty(organised))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
                    builder.setMessage("Are u sure you want to create an event?")
                            .setTitle("Confirmation");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          //  String random=random();
                            pd=new ProgressDialog(CreateEventActivity.this);
                            pd.setTitle("Uploading Event");
                            pd.setMessage("Please Wait...");
                            pd.show();
                            eventdatabase= FirebaseDatabase.getInstance().getReference().child("events").push();
                            eventdatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(CreateEventActivity.this,"Event Created Successfully", Toast.LENGTH_SHORT).show();
                                    Title.getEditText().setText("");
                                    Body.getEditText().setText("");
                                    organisedby.getEditText().setText("");
                                    eurl.setText("");
                                }
                            });
                           //
                            // User clicked OK button
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                   AlertDialog dialog=builder.create();
                    dialog.show();

                }

            }
        });


    }

    public void url(View view) {

        if(statee==1)
        eventurl=eurl.getText().toString();
        Toast.makeText(CreateEventActivity.this, eventurl, Toast.LENGTH_SHORT).show();


    }

   /* public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(15);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }*/
}
