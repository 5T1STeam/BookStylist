package com.app.bookstylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.shop.Service;
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
    private String serviceParent;
    private List<List<Service>> serviceShop;
    private RecyclerView searchShop;
    private ShopAdapter shopAdapter;
    private List<ShopModal> shopModals;
    private SearchView searchView;
    private TextView serParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        searchShop = findViewById(R.id.search_shop);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        serParent = findViewById(R.id.serParent);
        searchShop.setLayoutManager(flexboxLayoutManager);
        shopModals = new ArrayList<>();
        shopAdapter = new ShopAdapter(shopModals, getApplicationContext());
        searchView  = findViewById(R.id.search_view);
        Intent intent = getIntent();



        if(intent.getStringExtra("Service Parent")==null){
            //show bàn phím
            searchView.onActionViewExpanded();
            serParent.setVisibility(View.GONE);
            getListShops(serviceParent);
            searchShop.setAdapter(shopAdapter);

        }else{
            //không show bàn phím click vào danh mục
            serviceParent = intent.getStringExtra("Service Parent");
            serviceShop = new ArrayList<>();
            serParent.setText("Dịch vụ: "+serviceParent);
            serParent.setVisibility(View.VISIBLE);
            getListShops(serviceParent);
            searchShop.setAdapter(shopAdapter);
        }

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


    }

    private void getListShops(String serviceParent) {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        //Shop
        DatabaseReference myRef = database.getReference("Shop");
        //Service từng shop
        DatabaseReference myRef1 = database.getReference();
        if(serviceParent != null){
           List<ShopModal> tempShopModals = new ArrayList<>();
           List<Service> tempService = new ArrayList<>();

           myRef1.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot dataSnapshot : snapshot.child("Shop").getChildren()){
                       tempShopModals.add(dataSnapshot.getValue(ShopModal.class));
                   }
                   for (DataSnapshot dataSnapshot : snapshot.child("services").getChildren()){
                       tempService.add(dataSnapshot.getValue(Service.class));
                   }
                   for(ShopModal shopModal : tempShopModals){
                       List<Service> tempService2 = new ArrayList<>();
                       for(Service service : tempService){
                           if(shopModal.getId() == service.getShopId()){
                               tempService2.add(service);
                           }
                       }
                       serviceShop.add(tempService2);
                   }
                   for(int i=0; i<tempShopModals.size(); i++){
                       for(Service service : serviceShop.get(i)){
                           if(service.getName().contains(serviceParent)){
                               shopModals.add(tempShopModals.get(i));
                           }
                       }
                   }
                   shopAdapter.notifyDataSetChanged();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        }else {
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

}
