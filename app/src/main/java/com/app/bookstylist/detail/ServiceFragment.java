package com.app.bookstylist.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.shop.Service;
import com.app.bookstylist.shop.ShopModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment{
    int shopId;
    Button btn_book;
    ShopModal shopModal;
    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    public ServiceFragment( int shopId) {
        this.shopId = shopId;
    }
    public ServiceFragment() {
        // Required empty public constructor
    }

    private void getServiceShop() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myShop = database.getReference("Shop");
        myShop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ShopModal check = dataSnapshot.getValue(ShopModal.class);
                    if(shopId == check.getId()){
                        shopModal = check;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef = database.getReference("services");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot unit : snapshot.getChildren()){
                    Service service = unit.getValue(Service.class);
                    if(shopModal.getId() == service.getShopId()){
                        serviceList.add(service);
                    }
                }
                serviceAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        recyclerView = view.findViewById(R.id.rcvServiceDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(serviceList);
        recyclerView.setAdapter(serviceAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getServiceShop();
        return view;
    }


}