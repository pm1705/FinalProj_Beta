package com.example.finalproj_beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class first_signin extends AppCompatActivity {

    TextView mail_view;
    String gmail, name, uid;
    Intent google_auth_signin;
    EditText name_et, school_id_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_signin);

        google_auth_signin = getIntent();
        gmail = google_auth_signin.getStringExtra("gmail");
        name = google_auth_signin.getStringExtra("name");
        uid = google_auth_signin.getStringExtra("uid");

        mail_view = (TextView) findViewById(R.id.set_mail);
        mail_view.setText(gmail);

        name_et = (EditText) findViewById(R.id.name_et);
        name_et.setText(name);

        school_id_et = (EditText) findViewById(R.id.school_id);
    }

    public void teacher_next(View view) {
        Intent teacher_sign_in = new Intent(getApplicationContext(), teacher_sign_in.class);

        teacher_sign_in.putExtra("gmail", gmail);
        teacher_sign_in.putExtra("uid", uid);
        teacher_sign_in.putExtra("name", name_et.getText().toString());
        teacher_sign_in.putExtra("school", school_id_et.getText().toString());
        startActivity(teacher_sign_in);
    }

    public void student_next(View view) {
    }
}