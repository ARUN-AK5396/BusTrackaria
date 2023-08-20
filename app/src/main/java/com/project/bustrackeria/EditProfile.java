package com.project.bustrackeria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private CircleImageView profileImage;
    private Button cancel , save;


//    private StorageReference storageProfilePic;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storage= FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        storageProfilePic = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImage = findViewById(R.id.profile_change_image);

        cancel = findViewById(R.id.profile_cancel_btn);
        save = findViewById(R.id.profile_save_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this , ProfileFragment.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,33);
            }
        });
    }

    private void uploadProfileImage() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){
            Uri profileUri = data.getData();
            profileImage.setImageURI(profileUri);
            final StorageReference reference  = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Profile pic Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}