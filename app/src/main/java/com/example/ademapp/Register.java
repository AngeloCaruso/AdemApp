package com.example.ademapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private EditText txtNewUsername, txtEmail, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        txtNewUsername = (EditText)findViewById(R.id.txtNewUsername);
        txtEmail = (EditText)findViewById(R.id.txtNewEmail);
        txtPass = (EditText)findViewById(R.id.txtNewPass);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void registerUser(View view){

        final String username = txtNewUsername.getText().toString();
        final String email = txtEmail.getText().toString();
        String password = txtPass.getText().toString();

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, R.string.login_err, Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String id = mAuth.getCurrentUser().getUid();
                                User newUser = new User(username, email, id);
                                newUser.setUsername(username);
                                addUserToDB(newUser);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    public void addUserToDB(User user){
        DatabaseReference usersDb = firebaseDatabase.getReference("usuarios").child(user.getId());
        usersDb.setValue(user);
    }
}
