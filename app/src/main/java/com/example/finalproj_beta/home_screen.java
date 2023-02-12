package com.example.finalproj_beta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.finalproj_beta.DB_refs.refTeachers;
import static com.example.finalproj_beta.Teacher.GMAIL;
import static com.example.finalproj_beta.Teacher.LEVEL;
import static com.example.finalproj_beta.Teacher.NAME;

public class home_screen extends AppCompatActivity {

    ImageView pfp_image;
    TextView name, email, id;
    Button pending_btn;
    String uid;
    ListView reqs;
    String reqs_ids;
    String

    FirebaseAuth mAuth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    String lvl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pending_btn = findViewById(R.id.pending_btn);
        pfp_image = findViewById(R.id.pfp_image);

        pending_btn.setVisibility(INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        updateUI("Loading...", "", "2", user);

        if (acct != null) {
            uid = user.getUid();
            System.out.println(uid);
            ValueEventListener teacherListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dS) {
                    for (DataSnapshot data : dS.getChildren()) {
                        if (data.getKey().equals(uid)){
                            updateUI(data.child(NAME).getValue().toString(), data.child(GMAIL).getValue().toString(), data.child(LEVEL).getValue().toString(), user);
                            lvl1 = data.child(LEVEL).getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            };

            refTeachers.addValueEventListener(teacherListener);
        }
    }

    public void sign_out(View view) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(home_screen.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void request_print(View view) {
        Intent upload_file_screen = new Intent(this, upload_file.class);
        upload_file_screen.putExtra("uid", uid);
        upload_file_screen.putExtra("user_name", name.getText());
        startActivity(upload_file_screen);
    }

    public void sign_in_func(View view) {
        Intent google_auth_screen = new Intent(this, google_auth.class);
        startActivityForResult(google_auth_screen, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            user = mAuth.getCurrentUser();
            uid = user.getUid();
            System.out.println("indode");
            updateUI(data.getStringExtra("name"), data.getStringExtra("gmail"), data.getStringExtra("level"), user);
        }
        else{
            System.out.println("ahauahuah");
            updateUI("Error", "Error", "2", user);
        }
    }

    public void updateUI(String name1, String gmail1, String level1, FirebaseUser user) {
        System.out.println(name1 + gmail1 + level1);
        name.setText(name1);
        email.setText(gmail1);
        Picasso.get().load(user.getPhotoUrl()).into(pfp_image);

        System.out.println(level1);

        if (level1.equals("2")){
            pending_btn.setVisibility(INVISIBLE);
        }
        else if (level1.equals("1") || level1.equals("0")){
                pending_btn.setVisibility(VISIBLE);
        }
    }

    public void pending_requests(View view) {
        Intent intent = new Intent(this, pending_requests.class);
        intent.putExtra("uid", uid);
        intent.putExtra("lvl", lvl1);
        startActivity(intent);
    }

    public void my_pending_requests(View view) {
        Intent intent = new Intent(this, my_requests.class);
        intent.putExtra("uid", uid);
        intent.putExtra("lvl", lvl1);
        startActivity(intent);
    }

    public void create_school(View view) {

    }
}