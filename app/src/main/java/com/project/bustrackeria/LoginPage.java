package com.project.bustrackeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;

public class LoginPage extends AppCompatActivity {

    private EditText email, password;
    private Button btnLogin;
    private TextView signup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user!=null){
                        Intent i = new Intent(LoginPage.this, MainActivity.class);
                        startActivity(i);
                        finish();
                }
            }
        };
        email = findViewById(R.id.edit_loginEmail);
        password = findViewById(R.id.edit_loginPass);
        btnLogin = findViewById(R.id.login_button);
        signup = findViewById(R.id.text_signUp);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, BeforeSignup.class);
                startActivity(i);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String loginEmail = email.getText().toString().trim();
                final String loginPass = password.getText().toString().trim();

                if(TextUtils.isEmpty(loginEmail)){
                    email.setError("Email is required");
                }if (TextUtils.isEmpty(loginPass)){
                    password.setError("Password is required");
                }
                else {
                    Toast.makeText(LoginPage.this, "Your logging in ...", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             Toast.makeText(LoginPage.this, "Logged in", Toast.LENGTH_SHORT).show();
                             Intent i = new Intent(LoginPage.this, MainActivity.class);
                             startActivity(i);
                             finish();
                         }else {
                             Toast.makeText(LoginPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                         }
                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(authStateListener);
    }
}