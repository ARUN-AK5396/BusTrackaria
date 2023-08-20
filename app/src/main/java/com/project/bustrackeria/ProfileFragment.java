package com.project.bustrackeria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private TextView textViewWelcome , textViewFullName , textViewEmail, textViewRegNo, textViewBusNo , textViewRole, imageId;
    private ProgressBar progressBar;
    private CircleImageView imageView;
    private Button buttonLogOut;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getChildFragmentManager().findFragmentById(R.id.profile_fragment);
        textViewWelcome = view.findViewById(R.id.profile_textWelcome);
        textViewFullName = view.findViewById(R.id.textView_profile_fullname);
        textViewEmail = view.findViewById(R.id.textView_profile_email);
        textViewRegNo = view.findViewById(R.id.textView_profile_regNo);
        textViewBusNo = view.findViewById(R.id.textView_profile_busNo);
        imageView = view.findViewById(R.id.profile_profilePic);
        textViewRole = view.findViewById(R.id.textView_profile_role);
//        imageId = view.findViewById(R.id.imageId);


        progressBar = view.findViewById(R.id.prograss);

        buttonLogOut = view.findViewById(R.id.profile_logOut_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),EditProfile.class);
                startActivity(i);

            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext() , BeforeSignup.class);
                startActivity(i);
                auth.signOut();
            }
        });

        auth  = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid().toString();
        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        StorageReference profile_image_name = rootRef.child("profile_picture");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageId.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        profile_image_name.getDownloadUrl();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
//                    String wl = "Welcome";
                    String Name = snapshot.child("name").getValue().toString();

                    textViewWelcome.setText("Welcome "+Name);
                    textViewRole.setText(snapshot.child("Role").getValue().toString());
//                   textViewFullName.setText(snapshot.child("name").getValue().toString());
                    textViewEmail.setText(snapshot.child("email").getValue().toString());
                    textViewBusNo.setText(snapshot.child("bus_no").getValue().toString());
                    textViewRegNo.setText(snapshot.child("reg_no").getValue().toString());

//                    auth = FirebaseAuth.getInstance();
//                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    Uri uri = firebaseUser.getPhotoUrl();
//                    Picasso.with(getContext()).load(uri).into(imageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(view.ProfileFragment.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
//        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}





//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(getIntent());
//                finish();
//            }
//        });

//
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("User Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                   textViewFullName.setText(snapshot.child("full_name").getValue().toString());
//                   textViewEmail.setText(snapshot.child("email").getValue().toString());
//                   textViewRegNo.setText(snapshot.child("reg_no").getValue().toString());
//                   textViewBusNo.setText(snapshot.child("bus_no").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
