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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupPageDriver extends AppCompatActivity {

    private EditText editTextFullname, editTextEmail, editTextPass;
    private ProgressBar progressBar;
    private TextView textLogin;
    private Spinner bus_no;
    private static final String TAG = "signUp";
    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        Toast.makeText(SignupPageDriver.this, "You can Register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.prograss);
        editTextFullname = findViewById(R.id.editText_fullanme);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPass = findViewById(R.id.editText_pass);
        textLogin = findViewById(R.id.textLogin);
        bus_no = findViewById(R.id.spinner_busNo);

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupPageDriver.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String textFullName = editTextFullname.getText().toString();
                String textEmail = editTextEmail.getText().toString();
                String textpass = editTextPass.getText().toString();
                String busnumber = bus_no.getSelectedItem().toString();

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(SignupPageDriver.this, "Please enter your Full name", Toast.LENGTH_LONG).show();
                    editTextFullname.setError("Full name required");
                    editTextFullname.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(SignupPageDriver.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Email required");
                    editTextEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(SignupPageDriver.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Enter Valid Email ");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(textpass)) {
                    Toast.makeText(SignupPageDriver.this, "Please enter your Password", Toast.LENGTH_LONG).show();
                    editTextPass.setError("Password is required");
                    editTextPass.requestFocus();
                } else if (textpass.length() < 6) {
                    Toast.makeText(SignupPageDriver.this, "Password must contain minimum 6 digits", Toast.LENGTH_LONG).show();
                    editTextPass.setError("Password must contain 6 letters");
                    editTextPass.requestFocus();
                }else {
                    View v = findViewById(R.id.prograss);
                    v.setVisibility(View.VISIBLE);

                    registerUser(textFullName, textEmail, textpass, busnumber);
                }

            }
        });

    }
        private void registerUser(String textFullName, String textEmail, String textpass,String busNumber ) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(textEmail , textpass).addOnCompleteListener(SignupPageDriver.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                    Toast.makeText(signUp.this, "User Registered SuccessFully",Toast.LENGTH_LONG).show();

                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //user data into firebase realtime database
                        ReadWriteUserDetails writeDetails = new ReadWriteUserDetails(textFullName);

                        //user refarence from database
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User Details");

                        referenceProfile.child(firebaseUser.getUid()).setValue(writeDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //send verification message to the email
                                    firebaseUser.sendEmailVerification();

                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                                    firebaseUser.updateProfile(profileChangeRequest);

                                    Toast.makeText(SignupPageDriver.this,"User registered successfully",Toast.LENGTH_LONG).show();

                                    //open user profile
                                    Intent intent = new Intent(SignupPageDriver.this ,LoginPage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(SignupPageDriver.this,"User registered failed",Toast.LENGTH_LONG).show();

                                }

                                String currentUser = auth.getCurrentUser().getUid();
                                userRef = FirebaseDatabase.getInstance().getReference().child("User Details").child(currentUser);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id",currentUser);
                                userInfo.put("bus_no",busNumber);
                                userInfo.put("name",textFullName);
                                userInfo.put("Role","Driver");

                                userRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignupPageDriver.this, "Stored Successful", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(SignupPageDriver.this , task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                            }
                        });



                    }else {
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            editTextEmail.setError("Your email is invalid in use,kindly re-enter");
                            editTextEmail.requestFocus();
                        }catch (FirebaseAuthUserCollisionException e){
                            editTextEmail.setError("user is already register with this mail. Use another email.");
                            editTextEmail.requestFocus();
                        }catch (Exception e){
                            Log.e(TAG,e.getMessage());
                            Toast.makeText(SignupPageDriver.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }

            });


    }
}