package com.app.bookstylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.databinding.ActivityDashboardUserBinding;
import com.app.bookstylist.home.Cate;
import com.app.bookstylist.home.CateAdapter;
import com.app.bookstylist.shop.Service;
import com.app.bookstylist.home.ServiceAdapter;
import com.app.bookstylist.home.ShopAdapter;
import com.app.bookstylist.shop.ShopModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DashboardUserActivity extends AppCompatActivity {
    private ActivityDashboardUserBinding binding;
    private RecyclerView rcvShop;
    private ShopAdapter mShopAdapter;
    private List<ShopModal> mListShop;

    private CateAdapter mCateAdapter;
    private RecyclerView rcvCate;
    private List<Cate> mListCate;

    private ServiceAdapter mServiceAdapter;
    private RecyclerView rcvService;
    private List<Service> mListService;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rcvShop= findViewById(R.id.rcvShop);
        mListShop=new ArrayList<>();
        mShopAdapter = new ShopAdapter(mListShop, getApplicationContext());
        rcvShop.setAdapter(mShopAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(DashboardUserActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvShop.setLayoutManager(horizontalLayoutManagaer);

        rcvCate = findViewById(R.id.rcvCate);
        mListCate = new ArrayList<>();
        mCateAdapter = new CateAdapter(mListCate,getApplicationContext());
        rcvCate.setAdapter(mCateAdapter);
        LinearLayoutManager hori = new LinearLayoutManager(DashboardUserActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvCate.setLayoutManager(hori);


        rcvService= findViewById(R.id.rcvService);
        mListService=new ArrayList<>();
        mServiceAdapter = new ServiceAdapter(mListService,getApplicationContext());
        rcvService.setAdapter(mServiceAdapter);
        LinearLayoutManager hor = new LinearLayoutManager(DashboardUserActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvService.setLayoutManager(hor);


        getShop();
        getCate();
        getService();


        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this, SearchActivity.class));
            }
        });
        //Chuyển tab
        binding.bottomNavi.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(DashboardUserActivity.this, SearchActivity.class));
                return false;
            }
        });
    }

    private void getShop(){
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Shop");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListShop.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopModal shop = dataSnapshot.getValue(ShopModal.class);
                    mListShop.add(shop);

                }
                mShopAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getCate(){  FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("category");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListCate.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Cate cate  = dataSnapshot.getValue(Cate.class);
                    mListCate.add(cate);

                }
                mCateAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getService(){
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListService.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Service service  = dataSnapshot.getValue(Service.class);
                    mListService.add(service);

                }
                mServiceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}


