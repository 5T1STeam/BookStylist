package com.app.bookstylist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.bookstylist.databinding.ActivityProfileUserBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;


public class ProfileUserActivity extends AppCompatActivity {
    private ActivityProfileUserBinding binding;

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "PROFILE_TAG";
    private Uri imageUri = null;

    private ProgressDialog progressDialog;
    private String name="";
    private String email="";
    private String address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityProfileUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadUserInfo();
        binding.btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileUserActivity.this,ProfileEditInfoActivity.class));
            }
        });
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttachmenu();
            }
        });
        //Xu li nut update
//        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
        //Xu li nut backbtn
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileUserActivity.this,ProfileMenuActivity.class));
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
                String address = ""+snapshot.child("address").getValue();
                String timestamp = ""+snapshot.child("timestamp").getValue();
                String uid = ""+snapshot.child("uid").getValue();
                String userType = ""+snapshot.child("userType").getValue();

                binding.getName.setText(name);
                binding.inputEmail.setText(email);
                binding.inputAddress.setText(address);

                //Xu li profileImage
                Glide.with(ProfileUserActivity.this).load(profileImage)
                        .placeholder(R.drawable.profile)
                        .into(binding.profileImageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void validateData() {
        name = binding.getName.getText().toString().trim();
        email = binding.inputEmail.getText().toString().trim();
        address = binding.inputAddress.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
        }
        else {
            if (imageUri == null){
                updateProfile();
            }
            else{
                Toast.makeText(this, "Failed lam lai di", Toast.LENGTH_SHORT).show();
//                uploadImage();
            }
        }

    }

    private void updateProfile() {
        Log.d(TAG, "updateProfile: updateProfile: Updating user Profile");
        progressDialog.setMessage("Updating");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",""+name);
        hashMap.put("email",""+email);
        hashMap.put("address",""+address);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Profile updated...");
                progressDialog.dismiss();
                Toast.makeText(ProfileUserActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failed"+e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(ProfileUserActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
    private void uploadImage() {
        Log.d(TAG, "uploadImage: Updating Profile");
        progressDialog.setMessage("Updating your profile");
        progressDialog.show();

        //Lay anh moi thay anh cu~
        String filePath = "ProfileImages/"+firebaseAuth.getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePath);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: Profile image Uploaded");
                Log.d(TAG, "onSuccess: Getting url of uploaded image");
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String uploadedImageUrl = ""+uriTask.getResult();
                Log.d(TAG, "onSuccess: Uploaded image Url"+uploadedImageUrl);
//                updateProfile(uploadedImageUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failed");
                progressDialog.dismiss();
                Toast.makeText(ProfileUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void showImageAttachmenu() {
        //popup menu
        PopupMenu popupMenu = new PopupMenu(this,binding.profileImageView);
        popupMenu.getMenu().add(Menu.NONE, 0 , 0,"Camera" );
        popupMenu.getMenu().add(Menu.NONE, 1 , 1,"Gallery" );
        popupMenu.show();

        //xu li menu click
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //lay id cua item clicked
                int which = item.getItemId();
                if(which==0){
                    //Camera clicked
                    pickImageCamera();
                }
                else if (which ==1){
                    //Gallery clicked
                    pickImageGallery();
                }
                return false;
            }
        });

    }

    private void pickImageCamera() {
        //cbi de pick image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pick");//image title
        values.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityResultLauncher.launch(intent);


    }
    private void pickImageGallery() {
        //cbi de pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);


    }
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //xu li camera
                    //get url cua image
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Log.d(TAG, "onActivityResult:Picked From Camera "+imageUri);
                        Intent data = result.getData();

                        binding.profileImageView.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileUserActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //xu li camera
                    //get url cua image
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Log.d(TAG, "onActivityResult:Picked From Camera "+imageUri);
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: Picked from Gallery"+imageUri);

                        binding.profileImageView.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileUserActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );




}
