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

import com.app.bookstylist.databinding.ActivityProfileEditInfoBinding;
import com.bumptech.glide.Glide;
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

import java.util.HashMap;

public class ProfileEditInfoActivity extends AppCompatActivity {
    private ActivityProfileEditInfoBinding binding;
    private FirebaseAuth firebaseAuth;
    private String name="";
    private String address="";
    private static final String TAG = "PROFILE_TAG";
    private Uri imageUri = null;
    private ProgressDialog progressDialog;
    private String profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.editImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachment();
            }
        });
        loadUserInfo();
        //Xu li nut update
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();

            }
        });
        //Xu li nut backbtn

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileEditInfoActivity.this,ProfileUserActivity.class));
            }
        });



    }

    private void showAttachment() {
        PopupMenu popupMenu = new PopupMenu(this,binding.editImageProfile);
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

                        binding.editImageProfile.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileEditInfoActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
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

                        binding.editImageProfile.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(ProfileEditInfoActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    public void loadUserInfo() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = ""+snapshot.child("name").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();
                String address = ""+snapshot.child("address").getValue();
                binding.nameEt.setText(name);
                binding.addressEt.setText(address);

                //Xu li profileImage
                Glide.with(ProfileEditInfoActivity.this).load(profileImage)
                        .placeholder(R.drawable.ic_person_black)
                        .into(binding.editImageProfile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void validateData() {

        name = binding.nameEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
        }
        else {
            if (imageUri == null){
                updateProfile(profileImage);
            }
            else{

                uploadImage();
            }
        }

    }
    private void updateProfile(String imageUrl) { //
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("name",""+name);
        hashMap.put("address",""+address);
        if(imageUri !=null){
            hashMap.put("profileImage",""+imageUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Profile upload...");
                progressDialog.dismiss();
                Toast.makeText(ProfileEditInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failed update DB");
                progressDialog.dismiss();
                Toast.makeText(ProfileEditInfoActivity.this, "Failed to update"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                updateProfile(uploadedImageUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failed");
                progressDialog.dismiss();
                Toast.makeText(ProfileEditInfoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
//
}