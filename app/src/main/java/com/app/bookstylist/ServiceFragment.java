package com.app.bookstylist;

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

import com.app.bookstylist.shop.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {
    int shopId;
    Button btn_book;
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
        DatabaseReference myRef = database.getReference("services");
        myRef.orderByChild("shopid").equalTo(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot unit : snapshot.getChildren()){
                    serviceList.add( unit.getValue(Service.class));
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