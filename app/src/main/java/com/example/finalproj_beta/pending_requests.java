package com.example.finalproj_beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.finalproj_beta.DB_refs.refRequests;
import static com.example.finalproj_beta.Request.APPROVED;
import static com.example.finalproj_beta.Request.COPIES;
import static com.example.finalproj_beta.Request.DATE_PRINTED;
import static com.example.finalproj_beta.Request.PENDING;
import static com.example.finalproj_beta.Request.RELEVANT;
import static com.example.finalproj_beta.Request.USER_ID;
import static com.example.finalproj_beta.Request.USER_NAME;
import static java.lang.Long.parseLong;

public class pending_requests extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView pending_requests_lv;
    ArrayList<String> pending_requests, displays;
    ArrayAdapter<String> adp;

    Long current_millis;
    String uid, lvl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        pending_requests_lv = findViewById(R.id.requests);
        pending_requests_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        pending_requests_lv.setOnItemClickListener(this);
        pending_requests = new ArrayList<String>();
        displays = new ArrayList<String>();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        lvl = intent.getStringExtra("lvl");

        ValueEventListener requestListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dS) {
                pending_requests.clear();
                displays.clear();
                System.out.println("outside");
                for(DataSnapshot data : dS.getChildren()) {
                    if ((boolean) data.child(PENDING).getValue() && !(boolean) data.child(APPROVED).getValue() && (boolean) data.child(RELEVANT).getValue()){
                        System.out.println("inside");
                        pending_requests.add(data.getKey());

                        if (is_relevant(data.child(DATE_PRINTED).getValue().toString())){
                            displays.add("NAME: " + data.child(USER_NAME).getValue().toString() + "  COPIES: " + data.child(COPIES).getValue().toString());

                        }
                        else{
                            refRequests.child(data.getKey()).child(RELEVANT).setValue(false);
                        }

                    }
                }
                adp = new ArrayAdapter<String>(pending_requests.this, R.layout.support_simple_spinner_dropdown_item, displays);
                pending_requests_lv.setAdapter(adp);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };

        refRequests.addValueEventListener(requestListener);
    }

    public void home(View view) {
        Intent intent = new Intent(this, home_screen.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent view_request = new Intent(this, com.example.finalproj_beta.view_request.class);
        view_request.putExtra("RQID", pending_requests.get(position));
        view_request.putExtra("uid", uid);
        view_request.putExtra("lvl", lvl);
        startActivity(view_request);
    }

    public boolean is_relevant(String print_millis) {
        current_millis = System.currentTimeMillis();
        if (current_millis > parseLong(print_millis)){
            return false;
        }
        return true;
    }
}