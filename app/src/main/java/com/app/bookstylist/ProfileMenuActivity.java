package com.app.bookstylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.view.View;


import com.app.bookstylist.databinding.ActivityProfileMenuBinding;
import com.app.bookstylist.detail.FavoriteActivity;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileMenuActivity extends AppCompatActivity {
    private ActivityProfileMenuBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "PROFILE_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityProfileMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileMenuActivity.this, FavoriteActivity.class));
            }
        });

        loadUserInfo();
        binding.btnClickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileMenuActivity.this,ProfileUserActivity.class));
                finish();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileMenuActivity.this,DashboardUserActivity.class));
                finish();
            }
        });


    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: Loading User Info"+firebaseAuth.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = ""+snapshot.child("email").getValue();
                String name = ""+snapshot.child("name").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();
                String timestamp = ""+snapshot.child("timestamp").getValue();
                String uid = ""+snapshot.child("uid").getValue();
                String userType = ""+snapshot.child("userType").getValue();

                binding.inputName.setText(name);
                binding.inputGmail.setText(email);

                //Xu li profileImage
                Glide.with(getApplicationContext()).load(profileImage)
                        .placeholder(R.drawable.ic_person_black)
                        .into(binding.btnProfileImage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}