package com.app.bookstylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.app.bookstylist.databinding.ActivityFavoriteBinding;
import com.app.bookstylist.shop.FavoriteShop;
import com.app.bookstylist.shop.ShopAdapter;
import com.app.bookstylist.shop.ShopModal;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;
    private RecyclerView yourFavorite;
    private ShopAdapter shopAdapter;
    private List<ShopModal> shopModals;
    private FirebaseAuth firebaseAuth;
    private List<FavoriteShop> favoriteModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoriteModal = new ArrayList<>();
        //Take UID
        firebaseAuth = FirebaseAuth.getInstance();

        shopModals = new ArrayList<>();
        shopAdapter = new ShopAdapter(shopModals, this.getApplicationContext());
        yourFavorite = findViewById(R.id.yourFavorite);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);

        yourFavorite.setLayoutManager(flexboxLayoutManager);


        getListShops();
        yourFavorite.setAdapter(shopAdapter);

    }
    private void getListShops() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Shop");

        reference.child(firebaseAuth.getUid()).child("Favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FavoriteShop favoriteShop = new FavoriteShop();
                    favoriteShop = dataSnapshot.getValue(FavoriteShop.class);
                    favoriteModal.add(favoriteShop);
                };


//                String shopId = ""+snapshot.child("shopId").getValue();
//                binding.getIdFavorite.setText(shopId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopModal item = dataSnapshot.getValue(ShopModal.class);
                    for(FavoriteShop a : favoriteModal) {
                        if (String.valueOf(item.getId()).equals(a.getShopId())) {
                            shopModals.add(item);
                        }
                    }
                    shopAdapter.notifyDataSetChanged();
                };

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}