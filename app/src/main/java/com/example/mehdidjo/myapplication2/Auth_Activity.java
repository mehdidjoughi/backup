package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Author;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.List;

public class Auth_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static final int RC_SIGN_IN = 123;
    private boolean exit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        if (exit){
            finish();
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null){
                    // déconncter


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(true)
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new AuthUI.IdpConfig.FacebookBuilder().build()
                                            )
                                    )
                                    .build(),
                            RC_SIGN_IN);


                    Log.v("Ath" , "---------> déconnecter");

                }else {
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();
                    String id = user.getUid();
                    Author auther = new Author(id ,name /*, email ,photoUrl.toString()*/);
                    myRef.child(id).setValue(auther);
                  // connecter
                    Intent intent = new Intent(Auth_Activity.this , PagerTable.class);
                    startActivity(intent);

                    Log.v("Ath" , "---------> connecter");

                }

            }
        };



    }
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        System.exit(0);
        exit=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(getApplicationContext() , " Successfully signed in" , Toast.LENGTH_SHORT).show();
                Log.v("Ath" , "---------> Successfully");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(Auth_Activity.this , PagerTable.class);
                startActivity(intent);
                // ...
            } else if (resultCode == RESULT_CANCELED){

                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext() , "Sign in failed" , Toast.LENGTH_SHORT).show();
                finish();
                Log.v("Ath" , "---------> Sign in failed");

                // ...
            }
        }
    }


}
