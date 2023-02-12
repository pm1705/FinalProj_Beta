package com.example.finalproj_beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.finalproj_beta.DB_refs.refRequests;
import static com.example.finalproj_beta.DB_refs.refTeachers;
import static com.example.finalproj_beta.Request.APPROVED;
import static com.example.finalproj_beta.Request.COLORFUL;
import static com.example.finalproj_beta.Request.COPIES;
import static com.example.finalproj_beta.Request.DATE_PRINTED;
import static com.example.finalproj_beta.Request.DATE_REQUESTED;
import static com.example.finalproj_beta.Request.DOUBLE_SIDED;
import static com.example.finalproj_beta.Request.RELEVANT;
import static com.example.finalproj_beta.Request.USER_NAME;
import static com.example.finalproj_beta.Request.VERTICAL;
import static com.example.finalproj_beta.Teacher.GMAIL;
import static com.example.finalproj_beta.Teacher.LEVEL;
import static com.example.finalproj_beta.Teacher.NAME;

public class view_request extends AppCompatActivity {

    String RQID, body1txt, body2txt, lvl;
    TextView title1, body1, body2;
    Button accept1, decline1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        Intent prev = getIntent();
        RQID = prev.getStringExtra("RQID");
        lvl = prev.getStringExtra("lvl");

        title1 = (TextView) findViewById(R.id.title1);
        body1 = (TextView) findViewById(R.id.body1);
        body2 = (TextView) findViewById(R.id.body2);

        title1.setText(RQID);

        accept1 = findViewById(R.id.accept_btn);
        decline1 = findViewById(R.id.decline_btn);

        if (lvl.equals("2")){
            accept1.setVisibility(View.INVISIBLE);
            decline1.setVisibility(View.INVISIBLE);
        }

        refRequests.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(RQID)) {
                        title1.setText("Request by " + data.child(USER_NAME).getValue().toString());
                        body1txt = "Copies: " + data.child(COPIES).getValue().toString();
                        if ((boolean) data.child(COLORFUL).getValue()){
                            body1txt += "\nColorful toner";
                        }
                        else{
                            body2txt += "\nBlack and white toner";
                        }

                        if ((boolean) data.child(VERTICAL).getValue()){
                            body1txt += "\nVertical orientation";
                        }
                        else{
                            body2txt += "\nHorizontal orientation";
                        }

                        if ((boolean) data.child(DOUBLE_SIDED).getValue()){
                            body1txt += "\nPrinting on both sides";
                        }
                        else{
                            body1txt += "\nPrinting on one side";
                        }

                        body1.setText(body1txt);

                        body2.setText("Requested on: " + data.child(DATE_REQUESTED).getValue().toString()
                                        + "\n Print on: " + data.child(DATE_PRINTED).getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void decline(View view) {
        refRequests.child(RQID).child(APPROVED).setValue(false);
        refRequests.child(RQID).child(RELEVANT).setValue(false);
        finish();
    }

    public void accept(View view) {
        refRequests.child(RQID).child(APPROVED).setValue(true);
        refRequests.child(RQID).child(RELEVANT).setValue(false);
        finish();
    }
}