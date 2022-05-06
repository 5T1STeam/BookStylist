package com.app.bookstylist.detail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookstylist.R;
import com.app.bookstylist.shop.PictureAdapter;
import com.app.bookstylist.shop.Rates;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PictureFragment extends Fragment {
    public PictureFragment(int shopId) {
        this.shopId = shopId;
    }

    int shopId;
    RecyclerView recyclerView;
    PictureAdapter pictureAdapter;
    ArrayList<Rates> ratesList;
    Activity mActivity;

    public PictureFragment() {
        // Required empty public constructor
    }
    private void getImgRating() {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Rates");
        myRef.orderByChild("shopid").equalTo(shopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot unit : snapshot.getChildren()){
                    if(unit.child("img").getValue() != ""){
                        ratesList.add( unit.getValue(Rates.class));
                    }
                }
                pictureAdapter.notifyDataSetChanged();
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
        // Inflate the layout for this fragment
        mActivity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        recyclerView = view.findViewById(R.id.rcvCardImg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ratesList = new ArrayList<>();
        pictureAdapter = new PictureAdapter(ratesList, mActivity);
        recyclerView.setAdapter(pictureAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getImgRating();
        return view;
    }
}