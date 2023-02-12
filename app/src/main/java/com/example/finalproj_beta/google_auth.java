package com.example.finalproj_beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.finalproj_beta.DB_refs.refTeachers;
import static com.example.finalproj_beta.Teacher.GMAIL;
import static com.example.finalproj_beta.Teacher.LEVEL;
import static com.example.finalproj_beta.Teacher.NAME;

public class google_auth extends AppCompatActivity {

    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    String uid;
    boolean exists = false;
    DataSnapshot data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signin = findViewById(R.id.sign_in_button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Working!", "signInWithCredential:success");
                                    user = mAuth.getCurrentUser();
                                    updateUI(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Error!", "signInWithCredential:failure", task.getException());
                                }
                            }

                        });

            }


            // Signed in successfully, show authenticated UI.
//            startActivity(siTeacher);
        } catch (ApiException e) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            updateUInull();
        }
    }

    //    public void updateUI(GoogleSignInAccount account){
//        if (account != null) {
//            tvtest.setText(account.getEmail());
//        }}
    public void updateUI(FirebaseUser account) {
        if (account != null) {
            uid = account.getUid();
            refTeachers.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.getKey().equals(uid)) {
                            exists = true;
                            data1 = data;
                        }
                    }
                    if (!exists) {
                        Intent auth = new Intent(getApplicationContext(), first_signin.class);
                        auth.putExtra("gmail", account.getEmail());
                        auth.putExtra("name", account.getDisplayName());
                        auth.putExtra("uid", account.getUid());
                        startActivity(auth);
                    }
                    else{
                        Intent back = getIntent();
                        back.putExtra("name", data1.child(NAME).getValue().toString());
                        back.putExtra("gmail", data1.child(GMAIL).getValue().toString());
                        back.putExtra("level", data1.child(LEVEL).getValue().toString());
                        setResult(RESULT_OK, back);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
        else{
            System.out.println("null");
        }
    }

    public void updateUInull() {
        Toast.makeText(google_auth.this,"Error occurred, please contact admin", Toast.LENGTH_SHORT).show();
    }
}
