package com.project.bustrackeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

//    private TextView textViewWelcome , textViewFullName , textViewEmail, textViewRegNo, textViewBusNo , refresh;
//    private ProgressBar progressBar;
//    private CircleImageView imageView;
//    private Button buttonLogOut;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        textViewWelcome = findViewById(R.id.profile_textWelcome);
//        textViewFullName = findViewById(R.id.textView_profile_fullname);
//        textViewEmail = findViewById(R.id.textView_profile_email);
//        textViewRegNo = findViewById(R.id.textView_profile_regNo);
//        textViewBusNo = findViewById(R.id.textView_profile_busNo);
//        imageView = findViewById(R.id.profile_profilePic);
////        refresh = findViewById(R.id.text_refresh);
//
//        progressBar = findViewById(R.id.prograss);
//
//        buttonLogOut = findViewById(R.id.profile_logOut_button);
//
//
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("User Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    textViewFullName.setText(snapshot.child("full_name").getValue().toString());
//                    textViewEmail.setText(snapshot.child("email").getValue().toString());
//                    textViewRegNo.setText(snapshot.child("reg_no").getValue().toString());
//                    textViewBusNo.setText(snapshot.child("bus_no").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}