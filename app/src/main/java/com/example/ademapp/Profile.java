package com.example.ademapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private TextView viewUsername, viewEmail, viewID;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewUsername = (TextView)findViewById(R.id.viewUsername);
        viewEmail = (TextView)findViewById(R.id.viewEmail);
        viewID = (TextView)findViewById(R.id.viewID);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        setUserProfile(user);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setUserProfile(FirebaseUser user) {
        viewUsername.setText(user.getDisplayName());
        viewEmail.setText(user.getEmail());
        viewID.setText(user.getUid());
    }

    public void updateUser(View view){

    }
}
