package com.app.bookstylist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bookstylist.book.BookModal;
import com.app.bookstylist.databinding.ActivityFeedbackBinding;
import com.app.bookstylist.databinding.ActivityMainBinding;
import com.app.bookstylist.shop.Rates;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    private @NonNull ActivityFeedbackBinding binding;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FeedbackActivity.this, Schedule.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        Intent get = getIntent();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String key = dateFormat.format(date);
        int cmt = Integer.parseInt(get.getStringExtra("comment"));
        float rate = Float.parseFloat(get.getStringExtra("rate"));
        String bookId = get.getStringExtra("bookId");

        int shopId = Integer.parseInt(get.getStringExtra("shopId"));
        binding.tvShopNameRate.setText(get.getStringExtra("shopName"));
        Glide.with(FeedbackActivity.this).load(get.getStringExtra("shopImg"))
                .placeholder(R.drawable.profile)
                .into(binding.imgShop);
        getNameUser();
        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               validate(shopId, key, cmt, rate, bookId);
            }
        });
        binding.feedbackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachment();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.feedbackImg.setImageResource(R.drawable.ic_camera);
                imageUri = null;
                binding.feedbackImg.setImageURI(imageUri);
                binding.btnCancel.setVisibility(View.GONE);
            }
        });
    }

    private void getNameUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uName = ""+snapshot.child("name").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();

                binding.tvUserNameRate.setText(uName);
                Glide.with(FeedbackActivity.this).load(profileImage)
                        .placeholder(R.drawable.profile)
                        .into(binding.imgUserRate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void validate(int idShop, String key, int cmt, float rate, String bookId){
        if(binding.ratingBar.getRating() == 0.0){
            Toast.makeText(this, "Bạn chưa đánh giá sao", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null){
            Toast.makeText(this, "Bạn Phải thêm ít nhất 1 hình ảnh", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadImage(idShop, key, cmt, rate, bookId);
        }
    }
    private void addRate(int idShop, String key, String uri, int cmt, float rate, String bookId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rates");
        HashMap<String, Object> ratesHashMap = new HashMap<>();
            int rateNum = (int) binding.ratingBar.getRating();
            String comment = "";
            if(binding.etComment.getText() != null){
                comment = binding.etComment.getText().toString().trim();
            }
            String uid = firebaseAuth.getUid();
            String img = uri;
            int shopId = idShop;
            ratesHashMap.put("desc", comment);
            ratesHashMap.put("img", img);
            ratesHashMap.put("rate", rateNum);
            ratesHashMap.put("shopid", shopId);
            ratesHashMap.put("userid", uid);
        reference.child(key).setValue(ratesHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(FeedbackActivity.this,Schedule.class);
                intent.putExtra("success", true);
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedbackActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //update comment number and rating average in Shop
        DatabaseReference referenceShop = FirebaseDatabase.getInstance().getReference("Shop");
        HashMap<String, Object> rateShop = new HashMap<>();
        int rateCount = cmt +1;
        float rateAve = (float) (Math.round(((rate*cmt+rateNum)/rateCount)*10)/10.0);
        rateShop.put("comment", rateCount);
        rateShop.put("rating", rateAve);
        referenceShop.child(String.valueOf(shopId)).updateChildren(rateShop);

        //update status Rate in Book
        DatabaseReference referenceBook = FirebaseDatabase.getInstance().getReference("Books");
        HashMap<String, Object> book = new HashMap<>();
        book.put("complete", "Đã đánh giá");
        referenceBook.child(bookId).updateChildren(book);
    }
    private void showAttachment() {
        PopupMenu popupMenu = new PopupMenu(this,binding.feedbackImg);
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
                        Intent data = result.getData();
                        binding.feedbackImg.setImageURI(imageUri);
                        binding.btnCancel.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(FeedbackActivity.this, "Đã huỷ", Toast.LENGTH_SHORT).show();
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
                        Intent data = result.getData();
                        imageUri = data.getData();

                        binding.feedbackImg.setImageURI(imageUri);
                        binding.btnCancel.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(FeedbackActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );
    private void uploadImage(int idShop, String key, int cmt, float rate, String bookId ) {

        //Lay anh moi thay anh cu~
        String filePath = "ProfileImages/rateImg"+key;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePath);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String uploadedImageUrl = ""+uriTask.getResult();
                addRate(idShop, key, uploadedImageUrl, cmt, rate, bookId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedbackActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

}