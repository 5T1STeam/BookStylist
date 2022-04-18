package com.app.bookstylist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.shop.ShopAdapter;
import com.app.bookstylist.shop.ShopModal;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchShop;
    private ShopAdapter shopAdapter;
    private List<ShopModal> shopModals;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        searchShop = findViewById(R.id.search_shop);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);

        searchShop.setLayoutManager(flexboxLayoutManager);
        shopModals = new ArrayList<>();
        shopAdapter = new ShopAdapter(shopModals, getApplicationContext());
        searchView  = findViewById(R.id.search_view);

        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shopAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shopAdapter.getFilter().filter(newText);
                return false;
            }
        });

        getListShops();
        searchShop.setAdapter(shopAdapter);

    }

    private void getListShops() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Shop");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopModal item = dataSnapshot.getValue(ShopModal.class);
                    shopModals.add(item);
                };
                shopAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}
