package com.app.bookstylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.app.bookstylist.databinding.ActivityShopBinding;
import com.app.bookstylist.detail.ViewPagerAdapter;
import com.app.bookstylist.home.ShopAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private ImageView imageView;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fabFavor;
    private ShopAdapter shopAdapter;
    private boolean isExpanded = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent get = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();




        String a = get.getStringExtra("name");
        String shopId = get.getStringExtra("id");
        float rateNum = Float.parseFloat((get.getStringExtra("rate")));
        String rateCount = get.getStringExtra("comment");
        String rateValue = String.valueOf((float) (Math.round(rateNum*10)/10.0));

        Intent intent = new Intent(ShopActivity.this,BookActivity.class);
        intent.putExtra("id", shopId);

        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        binding.shopName.setText(a);
        binding.shopAddress.setText(get.getStringExtra("address"));

        binding.rateNum.setText(rateValue);
        binding.rateCount.setText(rateCount + " đánh giá");
        //toolbar
        toolbar =  findViewById(R.id.toolbarRl);
        fabFavor = findViewById(R.id.btnFavor);
        imageView = findViewById(R.id.banner);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        Glide.with(this).load(get.getStringExtra("img")).into(imageView);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.black_trans));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.main));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setTitle(a);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.main));
                    isShow = true;
                } else if(isShow) {
                    toolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    toolbar.setBackgroundColor(getResources().getColor(R.color.trans));
                }
            }
        });
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

        //viewpager
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.view_pager2);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, Integer.parseInt(shopId));
        mViewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Dịch vụ");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
                case 2:
                    tab.setText("Thư viện");
                    break;
                case 3:
                    tab.setText("Chi tiết");
                    break;
            }
        }).attach();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(ShopActivity.this, DashboardUserActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void checkIsFavorite(Context context, String shopId ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Favorite").child(shopId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite = snapshot.exists();
                        if(isInMyFavorite){
                            binding.btnFavor.getDrawable().mutate().setTint(ContextCompat.getColor(context,R.color.red));
                        }
                        else{
                            binding.btnFavor.getDrawable().mutate().setTint(ContextCompat.getColor(context,R.color.black));
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

    }
}
