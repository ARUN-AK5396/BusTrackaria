package com.project.bustrackeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class SignupPageStudent extends AppCompatActivity {

    private EditText editTextFullname, editTextEmail, editTextPass, editRegNo;
    private ProgressBar progressBar;
    private TextView textLogin;
    private static final String TAG = "signUp";
    private DatabaseReference userRef;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page_student);

        Toast.makeText(SignupPageStudent.this, "You can Register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.prograss);
        editTextFullname = findViewById(R.id.editText_fullanme);
        editRegNo = findViewById(R.id.editText_regNo);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPass = findViewById(R.id.editText_pass);
        textLogin = findViewById(R.id.textLogin);
        spinner = findViewById(R.id.spinner_busNo);


        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupPageStudent.this, LoginPage.class);
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
                String textRegNo = editRegNo.getText().toString();
                String busNo = spinner.getSelectedItem().toString();
                String textpass = editTextPass.getText().toString();


                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(SignupPageStudent.this, "Please enter your Full name", Toast.LENGTH_LONG).show();
                    editTextFullname.setError("Full name required");
                    editTextFullname.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(SignupPageStudent.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Email required");
                    editTextEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(SignupPageStudent.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Enter Valid Email ");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(textpass)) {
                    Toast.makeText(SignupPageStudent.this, "Please enter your Password", Toast.LENGTH_LONG).show();
                    editTextPass.setError("Password is required");
                    editTextPass.requestFocus();
                } else if (textpass.length() < 6) {
                    Toast.makeText(SignupPageStudent.this, "Password must contain minimum 6 digits", Toast.LENGTH_LONG).show();
                    editTextPass.setError("Password must contain 6 letters");
                    editTextPass.requestFocus();
                } else if (TextUtils.isEmpty(textRegNo)) {
                    Toast.makeText(SignupPageStudent.this, "Please enter your Register Number", Toast.LENGTH_SHORT).show();
                    editRegNo.setError("Register Number Required");
                    editRegNo.requestFocus();
                } else if (TextUtils.isEmpty(busNo)) {
                    Toast.makeText(SignupPageStudent.this, "Please Enter the Bus number", Toast.LENGTH_SHORT).show();
                    spinner.requestFocus();
                } else {
                    View v = findViewById(R.id.prograss);
                    v.setVisibility(View.VISIBLE);

                    registerUser(textFullName, textEmail, textRegNo, busNo, textpass);
                }

            }
        });

    }


        private void registerUser(String textFullName, String textEmail, String textRegNo, String busNo, String textpass ) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(textEmail, textpass).addOnCompleteListener(SignupPageStudent.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                    Toast.makeText(signUp.this, "User Registered SuccessFully",Toast.LENGTH_LONG).show();

                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        ReadWriteUserDetails writeDetails = new ReadWriteUserDetails(textFullName);

                        //user refarence from database

                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User Details");

                        referenceProfile.child(firebaseUser.getUid()).setValue(writeDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //send verification message to the email
                                    firebaseUser.sendEmailVerification();

//                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
//                                    firebaseUser.updateProfile(profileChangeRequest);

                                    Toast.makeText(SignupPageStudent.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                    //open user profile
                                    Intent intent = new Intent(SignupPageStudent.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupPageStudent.this, "User registered failed", Toast.LENGTH_LONG).show();

                                }
                                String currentUser = auth.getCurrentUser().getUid();
                                userRef = FirebaseDatabase.getInstance().getReference().child("User Details").child(currentUser);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUser);
                                userInfo.put("reg_no",textRegNo);
                                userInfo.put("email",textEmail);
                                userInfo.put("bus_no",busNo);
                                userInfo.put("name", textFullName);
                                userInfo.put("Role", "Student");

                                userRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupPageStudent.this, "Stored Successful", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SignupPageStudent.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                            }
                        });


                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            editTextPass.setError("Your password is too weak.");
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            editTextEmail.setError("Your email is invalid in use,kindly re-enter");
                            editTextEmail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            editTextEmail.setError("user is already register with this mail. Use another email.");
                            editTextEmail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(SignupPageStudent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }

            });


        }

            //end

}