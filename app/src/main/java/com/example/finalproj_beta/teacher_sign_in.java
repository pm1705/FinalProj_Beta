package com.example.finalproj_beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import static com.example.finalproj_beta.DB_refs.refAuth;
import static com.example.finalproj_beta.DB_refs.refTeachers;

public class teacher_sign_in extends AppCompatActivity {

    String name;
    String school;
    String gmail;
    String uid;
    int perm = 2;

    String[] options = new String[]{"Teacher", "Admin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_in);

        Intent info = getIntent();
        name = info.getStringExtra("name");
        gmail = info.getStringExtra("gmail");
        school = info.getStringExtra("school");
        uid = info.getStringExtra("uid");

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Teacher")) {
                    perm = 2;
                } else if (selectedItem.equals("Admin")) {
                    perm = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void create_user(View view) {
        Teacher newTeacher = new Teacher(name, school, gmail, perm);
        refTeachers.child(uid).setValue(newTeacher);
        Intent intent = new Intent(this, home_screen.class);
        startActivity(intent);
    }
}