package com.project.bustrackeria;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BeforeSignup extends AppCompatActivity {

    private Button driver,student;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_signup);

        student = findViewById(R.id.student_SignUp);
        driver = findViewById(R.id.driver_SignUp);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeforeSignup.this , SignupPageStudent.class);
                startActivity(intent);
                finish();
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeforeSignup.this , SignupPageDriver.class);
                startActivity(intent);
                finish();
            }
        });

    }
}