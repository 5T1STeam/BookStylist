package com.app.bookstylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bookstylist.databinding.ActivityShopBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding binding;
    String shopId;
    boolean isInMyFavorite = false;
    private FirebaseAuth firebaseAuth;
    int images = R.drawable.ic_favorite_red;
    int image = R.drawable.ic_favorite_heart_black;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent get = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();

        String a = get.getStringExtra("name");
        String shopId = get.getStringExtra("id");

        Intent intent = new Intent(ShopActivity.this,BookActivity.class);
        intent.putExtra("name", a);
        intent.putExtra("id", shopId);
        intent.putExtra("service", get.getStringExtra("service"));
        intent.putExtra("address", get.getStringExtra("address"));
        intent.putExtra("img",get.getStringExtra("img"));

        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        binding.shop.setText(a);
        binding.idShop.setText(shopId);
        checkUser();


        checkIsFavorite(ShopActivity.this,shopId);

        binding.btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInMyFavorite){
                    removeFromFavorite(ShopActivity.this,shopId);

                }
                else {
                    addToFavorite(ShopActivity.this,shopId);
                }
            }
        });


    }

    public void checkIsFavorite(Context context, String shopId ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Favorite").child(shopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite = snapshot.exists();
                        if(isInMyFavorite){
                            binding.btnFavor.setImageResource(images);
                        }
                        else{
                            binding.btnFavor.setImageResource(image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void addToFavorite(Context context, String shopId  ){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("shopId",""+shopId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Favorite").child(shopId).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Add to your favorite", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to add your favorite"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void removeFromFavorite(Context context, String shopId){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Favorite").child(shopId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Remove from your favorite", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to remove from your favorite"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkUser() {
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(this, "Your are not loggin", Toast.LENGTH_SHORT).show();

        }
        else{
            binding.getUid.setText(firebaseAuth.getUid());
        }

    }
}
